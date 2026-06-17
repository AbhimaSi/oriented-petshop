package com.mycompany.javapet.controller;

import static com.mycompany.javapet.controller.GenericDAO.resultSet;
import static com.mycompany.javapet.controller.GenericDAO.statement;
import com.mycompany.javapet.model.Atendimento;
import com.mycompany.javapet.model.AtendimentoAnimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AtendimentoDAO extends GenericDAO<Atendimento> {

    public AtendimentoDAO(){
        super(Atendimento.class);
    }
    
    @Override
    public String getNomeTabela() {
        return "atendimento";
    }

    @Override
    public String getSqlBuscarTodos(){
        return "SELECT * FROM "+getNomeTabela();
    }
    
    @Override
    public String getSqlBuscarPorId(){
        return "SELECT * FROM "+getNomeTabela()+" WHERE id = ?";
    }
    
    @Override
    public String getSqlBuscarPorUuid(){
        return "SELECT * FROM "+getNomeTabela()+" WHERE uuid = ?";
    }
    
    @Override
    public String getSqlInserir() {
        return "INSERT INTO "+getNomeTabela()+" (data, hora, status) VALUES (?, ?, ?)";
    }

    @Override
    public String getSqlAtualizarPorId() {
        return "UPDATE "+getNomeTabela()+" SET data = ?, hora = ?, status = ? WHERE id = ?";
    }
    
    @Override
    public String getSqlAtualizarPorUuid() {
        return "UPDATE "+getNomeTabela()+" SET data = ?, hora = ?, status = ? WHERE uuid = ?";
    }
    
    public String getSqlBuscarAtendimentosAnimal(){
        return "SELECT a.id, at.id as idtabela, a.uuid, a.data, a.hora, a.status, at.idservico, at.idanimal, at.idfuncionario, at.duracao FROM atendimento a LEFT JOIN animal_atendimento at ON a.id = at.idatendimento";
    }
    
    public String getSqlBuscarAtendimentoAnimalPorId(){
        return "SELECT a.id, at.id as idtabela, a.uuid, a.data, a.hora, a.status, at.idservico, at.idanimal, at.idfuncionario, at.duracao FROM atendimento a LEFT JOIN animal_atendimento at ON a.id = at.idatendimento WHERE at.id = ?";
    }
    
    public String getSqlInserirAtendimentoAnimal(){
        return "INSERT INTO animal_atendimento (idatendimento, idservico, idanimal, idfuncionario, duracao) VALUES (?, ?, ?, ?, ?)";
    }
    
    public String getSqlAtualizarAtendimentoAnimalPorId(){
        return "UPDATE animal_atendimento SET idatendimento = ?, idservico = ?, idanimal = ?, idfuncionario = ?, duracao = ? WHERE id = ?";
    }

    public String getSqlRemoverAtendimentoAnimalPorId(){
        return "DELETE FROM animal_atendimento WHERE id = ?";
    }
    
    public boolean buscarAtendimentoAnimal(){
        return buscarTodos(getSqlBuscarAtendimentosAnimal());
    }
    
    public boolean buscarAtendimentoAnimalPorId(AtendimentoAnimal at){
        return buscarPorId(at.getIdTabela(), getSqlBuscarAtendimentoAnimalPorId());
    }
    
    public boolean inserirAtendimentoAnimal(AtendimentoAnimal atendimento){
        String sqlInserir = getSqlInserirAtendimentoAnimal();
        try{
            statement = this.connection.prepareStatement(sqlInserir);
            definirParamsDeStatement(statement, sqlInserir, atendimento);
            statement.setInt(1, atendimento.getId());
            statement.setInt(2, atendimento.getIdServico());
            statement.setInt(3, atendimento.getIdAnimal());
            statement.setInt(4, atendimento.getIdFuncionario());
            statement.setInt(5, atendimento.getDuracao());
            int updated = statement.executeUpdate();
            if(updated == 1){
                //this.connection.commit();
                return true;
            }
            this.connection.rollback();
            return false;
        }
        catch(SQLException err){
            System.err.println("Erro ao tentar realizar insercao de atendimento de animal: "+err.getMessage());
        }
        return false;
        
    }

    public boolean atualizarAtendimentoAnimalPorId(AtendimentoAnimal atendimento){
        String sqlAtualizar = getSqlAtualizarAtendimentoAnimalPorId();
        try{
            statement = this.connection.prepareStatement(sqlAtualizar);
            statement.setInt(1, atendimento.getId());
            statement.setInt(2, atendimento.getIdServico());
            statement.setInt(3, atendimento.getIdAnimal());
            statement.setInt(4, atendimento.getIdFuncionario());
            statement.setInt(5, atendimento.getDuracao());
            statement.setInt(6, atendimento.getIdTabela());
            int updated = statement.executeUpdate();
            if(updated == 1){
                //this.connection.commit();
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

    public boolean removerAtendimentoAnimal(AtendimentoAnimal at){
        return remover(at.getIdTabela(), getSqlRemoverAtendimentoAnimalPorId());
    }
    
    @Override
    public Atendimento retornarSelecionado() {
        try {
            Atendimento atendimento = null;
            if (resultSet != null) {
                if (resultSet.isBeforeFirst()) {
                    JDBCUtil.movProximo(resultSet);
                }
                atendimento = new Atendimento();
                atendimento.setId(resultSet.getInt("id"));
                atendimento.setUuid(resultSet.getString("uuid"));
                atendimento.setData(resultSet.getDate("data").toLocalDate());
                atendimento.setHora(resultSet.getTime("hora").toLocalTime());
                atendimento.setStatus(resultSet.getString("status"));
            }
            return atendimento;
        } catch (SQLException err) {
            System.out.println("Erro ao acessar statement: " + err.getMessage());
        }
        return null;
    }
    
    public AtendimentoAnimal retornarAtendimentoAnimalSelecionado() {
        try {
            AtendimentoAnimal atendimento = null;
            if (resultSet != null) {
                if (resultSet.isBeforeFirst()) {
                    JDBCUtil.movProximo(resultSet);
                }
                atendimento = new AtendimentoAnimal();
                atendimento.setId(resultSet.getInt("id"));
                atendimento.setIdTabela(resultSet.getInt("idtabela"));
                atendimento.setData(resultSet.getDate("data").toLocalDate());
                atendimento.setHora(resultSet.getTime("hora").toLocalTime());
                atendimento.setStatus(resultSet.getString("status"));
                atendimento.setIdServico(resultSet.getInt("idservico"));
                atendimento.setIdAnimal(resultSet.getInt("idanimal"));
                atendimento.setIdFuncionario(resultSet.getInt("idfuncionario"));
                atendimento.setDuracao(resultSet.getInt("duracao"));
            }
            return atendimento;
        } catch (SQLException err) {
            System.out.println("Erro ao acessar statement: " + err.getMessage());
        }
        return null;
    }
    
    public ArrayList<AtendimentoAnimal> retornarListaAtendimentoAnimal(){
        ArrayList<AtendimentoAnimal> lista = null;
        if(resultSet != null && JDBCUtil.hasElements(resultSet)){
            lista = new ArrayList<>();
            while (JDBCUtil.movProximo(resultSet)) {
                lista.add(retornarAtendimentoAnimalSelecionado());
            }
        }
        return lista;
    };
}
