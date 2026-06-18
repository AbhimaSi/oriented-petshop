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
        return "SELECT a.id, at.id as idtabela, a.uuid, a.data, a.hora, a.status, at.idservico, at.idanimal, at.idfuncionario, at.duracao FROM atendimento a LEFT JOIN animal_atendimento at ON a.id = at.idatendimento WHERE a.id = ?";
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
    
    public boolean buscarAtendimentoAnimalPorId(int id){
        return buscarPorId(id, getSqlBuscarAtendimentoAnimalPorId());
    }

    public boolean buscarAtendimentoAnimalPorId(AtendimentoAnimal atendimento){
        return buscarAtendimentoAnimalPorId(atendimento.getId());
    }

    public boolean inserirAtendimentoCompleto(AtendimentoAnimal atendimento){
        String sql = "WITH novo_atendimento AS ("
            + "INSERT INTO atendimento (data, hora, status) VALUES (?, ?, ?) RETURNING id"
            + ") INSERT INTO animal_atendimento "
            + "(idatendimento, idservico, idanimal, idfuncionario, duracao) "
            + "SELECT id, ?, ?, ?, ? FROM novo_atendimento";

        try {
            statement = this.connection.prepareStatement(sql);
            statement.setObject(1, atendimento.getData());
            statement.setObject(2, atendimento.getHora());
            statement.setString(3, atendimento.getStatus());
            statement.setInt(4, atendimento.getIdServico());
            statement.setInt(5, atendimento.getIdAnimal());
            statement.setInt(6, atendimento.getIdFuncionario());
            statement.setInt(7, atendimento.getDuracao());
            return statement.executeUpdate() == 1;
        }
        catch(SQLException err){
            System.err.println("Erro ao inserir atendimento completo: "+err.getMessage());
        }
        return false;
    }

    public boolean atualizarAtendimentoCompleto(AtendimentoAnimal atendimento){
        String sql = "WITH atendimento_atualizado AS ("
            + "UPDATE atendimento SET data = ?, hora = ?, status = ? WHERE id = ? RETURNING id"
            + ") UPDATE animal_atendimento SET idservico = ?, idanimal = ?, "
            + "idfuncionario = ?, duracao = ? "
            + "WHERE idatendimento = (SELECT id FROM atendimento_atualizado)";

        try {
            statement = this.connection.prepareStatement(sql);
            statement.setObject(1, atendimento.getData());
            statement.setObject(2, atendimento.getHora());
            statement.setString(3, atendimento.getStatus());
            statement.setInt(4, atendimento.getId());
            statement.setInt(5, atendimento.getIdServico());
            statement.setInt(6, atendimento.getIdAnimal());
            statement.setInt(7, atendimento.getIdFuncionario());
            statement.setInt(8, atendimento.getDuracao());
            return statement.executeUpdate() == 1;
        }
        catch(SQLException err){
            System.err.println("Erro ao atualizar atendimento completo: "+err.getMessage());
        }
        return false;
    }

    public boolean removerAtendimentoCompleto(int id){
        String sql = "WITH vinculo_removido AS ("
            + "DELETE FROM animal_atendimento WHERE idatendimento = ? RETURNING idatendimento"
            + ") DELETE FROM atendimento WHERE id = ? "
            + "AND EXISTS (SELECT 1 FROM vinculo_removido)";

        try {
            statement = this.connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setInt(2, id);
            return statement.executeUpdate() == 1;
        }
        catch(SQLException err){
            System.err.println("Erro ao remover atendimento completo: "+err.getMessage());
        }
        return false;
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
