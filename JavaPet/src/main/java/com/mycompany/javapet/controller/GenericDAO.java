package com.mycompany.javapet.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class GenericDAO <T>{
    protected static Connection connection = null;
    protected static PreparedStatement statement = null;
    protected static ResultSet resultSet = null;
    
    public abstract String getNomeTabela();
    
    public String getSqlBuscarTodos(){
        return "SELECT * FROM "+getNomeTabela();
    }
    public String getSqlBuscarPorId(){
        return "SELECT * FROM "+getNomeTabela()+" WHERE id = ?";
    }
    public abstract String getSqlInserir();
    public abstract String getSqlAtualizar();
    public String getSqlRemover(){
        return "DELETE FROM "+getNomeTabela()+" WHERE id = ?";
    }
    
    public boolean buscarTodos(){
        conectar();
        String sqlBuscarTodos = getSqlBuscarTodos();
        
        try{
            statement = connection.prepareStatement(sqlBuscarTodos);
            resultSet = statement.executeQuery();
            return true;
        }
        catch(SQLException err){
            System.err.println("Erro durante a consulta: "+err.getMessage());
        }
        return false;
    }
    
    public boolean buscarPorId(int id){
        conectar();
        String sqlBuscarId = getSqlBuscarPorId();
        
        try{
            statement = connection.prepareStatement(sqlBuscarId);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            return true;
        }
        catch(SQLException err){
            System.err.println("Erro durante consulta por id: "+err.getMessage());
        }
        return false;
    }
    
    public abstract boolean inserir(T objeto);
    
    public abstract boolean atualizar(T objeto);
    
    public boolean remover(int id){
        conectar();
        String sqlRemover = getSqlRemover();
        
        try{
            statement = connection.prepareStatement(sqlRemover);
            statement.setInt(1, id);
            int updated = statement.executeUpdate();
            if(updated == 1){
            
            }
            return true;
        }
        catch(SQLException err){
            System.err.println("Erro durante a consulta: "+err.getMessage());
        }
        return false;
    }
    
    public abstract ArrayList<T> retornarLista();
    public abstract T retornarSelecionado();
    
    private boolean conectar(){
        if(connection != null){
            connection = DBConnection.getConnection();
            if(connection != null){
                return true;
            }
        }
        return false;
    }
    
    public boolean fecharConnection(){
        try{
            if(connection != null){
                connection.close();
                connection = null;
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
