package com.mycompany.javapet.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class GenericDAO <T>{
    protected Class<T> entidade = null;
    protected Connection connection = null;
    protected static PreparedStatement statement = null;
    protected static ResultSet resultSet = null;
    
    public GenericDAO(){
        conectar();
    }
    
    public GenericDAO(Class<T> entidade){
        conectar();
        this.entidade = entidade;
    }
    
    public abstract String getNomeTabela();
    
    public String getSqlBuscarTodos(){
        return "SELECT * FROM "+getNomeTabela();
    }
    
    public String getSqlBuscarPorId(){
        return "SELECT * FROM "+getNomeTabela()+" WHERE id = ?";
    }
    
    public String getSqlInserir(){
        if(entidade != null){
            return entidadeParaInsert();
        }
        System.err.println("Classe entidade nao definida. Metodo 'getSqlInserir' precisa ser reescrito.");
        return null;
    }
    
    public String getSqlAtualizar(){
        if(entidade != null){
            return entidadeParaUpdate();
        }
        System.err.println("Classe entidade nao definida. Metodo 'getSqlAtualizar' precisa ser reescrito.");
        return null;
    }
    
    public String getSqlRemover(){
        return "DELETE FROM "+getNomeTabela()+" WHERE id = ?";
    }
    
    public boolean buscarTodos(){
        String sqlBuscarTodos = getSqlBuscarTodos();
        
        try{
            statement = this.connection.prepareStatement(sqlBuscarTodos);
            resultSet = statement.executeQuery();
            return true;
        }
        catch(SQLException err){
            System.err.println("Erro durante a consulta: "+err.getMessage());
        }
        return false;
    }
    
    public boolean buscarPorId(int id){
        String sqlBuscarId = getSqlBuscarPorId();
        
        try{
            statement = this.connection.prepareStatement(sqlBuscarId);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            return true;
        }
        catch(SQLException err){
            System.err.println("Erro durante consulta por id: "+err.getMessage());
        }
        return false;
    }
    
    public boolean inserir(T objeto){
        String sqlInserir = getSqlInserir();
        try{
            statement = this.connection.prepareStatement(sqlInserir);
            definirParamsDeStatement(statement, sqlInserir, objeto);

            int updated = statement.executeUpdate();
            if(updated == 1){
                this.connection.commit();
                return true;
            }
            this.connection.rollback();
            return false;
        }
        catch(SQLException err){
            System.err.println("Erro ao tentar realizar insercao: "+err.getMessage());
        }
        return false;
    }
    
    public boolean atualizar(T objeto){
        String sqlAtualizar = getSqlAtualizar();
        try{
            statement = this.connection.prepareStatement(sqlAtualizar);
            definirParamsDeStatement(statement, sqlAtualizar, objeto);

            int updated = statement.executeUpdate();
            if(updated == 1){
                this.connection.commit();
                return true;
            }
            this.connection.rollback();
            return false;
        }
        catch(SQLException err){
            System.err.println("Erro ao tentar realizar atualizacao: "+err.getMessage());
        }
        return false;
    }
    
    public boolean remover(int id){
        String sqlRemover = getSqlRemover();
        
        try{
            statement = this.connection.prepareStatement(sqlRemover);
            statement.setInt(1, id);
            int updated = statement.executeUpdate();
            if(updated == 1){
                this.connection.commit();
                return true;
            }
            this.connection.rollback();
        }
        catch(SQLException err){
            System.err.println("Erro durante a consulta: "+err.getMessage());
        }
        return false;
    }
    
    public abstract ArrayList<T> retornarLista();
    public abstract T retornarSelecionado();
    
    // define os parametros de um statement de insert
    private PreparedStatement definirParamsDeStatement(PreparedStatement statement, String sql, T objeto){
        String operacao = sql.split(" ")[0];
        String[] campos = null;
        if(operacao.equalsIgnoreCase("INSERT")){
            campos = extrairAtributosDeInsert(sql);
        }
        else if(operacao.equalsIgnoreCase("UPDATE")){
            campos = extrairAtributosDeUpdate(sql);
        }
        
        if(campos != null){
            try{
                Method[] metodos = entidade.getDeclaredMethods();
                for(int i = 0; i < campos.length; i++){
                    Method getter = ClassUtil.encontrarGetterDeCampo(metodos, campos[i]);
                    Object valor = getter.invoke(objeto);
                    
                    // parsear id para int se necessário
                    if(campos[i].equalsIgnoreCase("id") && valor.getClass().getSimpleName().equalsIgnoreCase("String")){
                        valor = Integer.parseInt((String)valor);
                    }
                    
              statement.setObject(i+1, valor);
                }
                return statement;
            }
            
            catch(SQLException err){
                System.err.println("Erro ao acessar parametros de statement: "+err.getMessage());
            }
            catch(InvocationTargetException err){
                System.err.println("Erro ao tentar chamar metodo em "+objeto.getClass().toString()+": "+err.getMessage());
            }
            catch(IllegalAccessException err){
                System.err.println("Erro ao tentar acessar metodo em "+objeto.getClass().toString()+": "+err.getMessage());
            }
        }

        return null;
    }
    
    // extrai o nome dos atributos de uma string sql para insercao
    private String[] extrairAtributosDeInsert(String sql){
        int inicio = sql.indexOf('(')+1;
        int fim = sql.indexOf(')');
        String[] atributos = sql.substring(inicio, fim).split(",");
        for(int i = 0; i < atributos.length; i++){
            atributos[i] = atributos[i].trim();
        }
        return atributos;
    }
    
    // converte os atributos de uma classe entidade para uma string sql para insercao
    private String entidadeParaInsert(){
        String[] listaAtributos = ClassUtil.getNomesDeAtributos(entidade);
        
        StringBuilder atributos = new StringBuilder();
        int qtdAtributos = 0;
        for(int i = 0; i < listaAtributos.length; i++){
            if(!(listaAtributos[i].equalsIgnoreCase("id"))){
                atributos.append(listaAtributos[i]);
                if(i != (listaAtributos.length-1)){
                    atributos.append(", ");
                }
                qtdAtributos++;
            }
        }
        StringBuilder valores = new StringBuilder();
        for(int i = 0; i < qtdAtributos; i++){
            valores.append("? ");
            if(i < qtdAtributos-1){
                valores.append(", ");
            }
        }
        
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(entidade.getSimpleName())
                .append(" (")
                .append(atributos.toString())
                .append(") VALUES (")
                .append(valores.toString())
                .append(")");
        
        return sql.toString();
    }
    
    // extrai o nome dos atributos de uma string sql para atualizacao
    private String[] extrairAtributosDeUpdate(String sql){
        String[] arr = sql.split(" ");
        ArrayList<String> atributosList = new ArrayList<>();
        for(int i = 0; i < arr.length; i++){
            if(arr[i].contains("?")){
                atributosList.add(arr[i-2]);
            }
        }
        String[] atributos = atributosList.toArray(new String[0]);

        return atributos;
    }
    
    // converte os atributos de uma classe entidade para uma string update
    private String entidadeParaUpdate(){
        String[] listaAtributos = ClassUtil.getNomesDeAtributos(entidade);
        StringBuilder atributos = new StringBuilder();
        for(int i = 0; i < listaAtributos.length; i++){
            if(!(listaAtributos[i].equalsIgnoreCase("id"))){
                atributos.append(listaAtributos[i]);
                if(i != (listaAtributos.length-1)){
                    atributos.append(" = ?, ");
                }
                else{
                    atributos.append(" = ? ");
                }
            }
        }
        
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(entidade.getSimpleName())
                .append(" SET ")
                .append(atributos)
                .append("WHERE id = ?");
        
        return sql.toString();
    }
    
    public boolean conectar(){
        if(this.connection != null){
            return false;
        }
        else{
            this.connection = DBConnection.getConnection();
            if(this.connection != null){
                return true;
            }
        }
        return false;
    }
    
    public ResultSet getResultSet(){
        return resultSet;
    }
    
    public boolean fecharConnection(){
        try{
            if(this.connection != null){
                this.connection.close();
                this.connection = null;
            }
            return true;
        }
        catch(SQLException err){
            System.err.println("Erro ao fechar conexao: "+err.getMessage());
        }
        return false;
    }
    
    public boolean fecharStatement(){
        try{
            if(statement != null){
                statement.close();
                statement = null;
            }
            return true;
        }
        catch(SQLException err){
            System.err.println("Erro ao fechar statement: "+err.getMessage());
        }
        return false;
    }
    
    public boolean fecharResultSet(){
        try{
            if(resultSet != null){
                resultSet.close();
                resultSet = null;
            }
            return true;
        }
        catch(SQLException err){
            System.err.println("Erro ao fechar conexao: "+err.getMessage());
        }
        return false;
    }
    
    public boolean finalizarConexao(){
        if(fecharResultSet() && fecharStatement() && fecharConnection()){
            System.out.println("Conexao com banco de dados finalizada.");
            return true;
        }
        return false;
    }
}
