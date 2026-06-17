package com.mycompany.javapet.controller;

import static com.mycompany.javapet.controller.GenericDAO.resultSet;
import com.mycompany.javapet.model.Servico;
import java.sql.SQLException;

public class ServicoDAO extends GenericDAO<Servico> {

    public ServicoDAO(){
        super(Servico.class);
    }
    
    @Override
    public String getNomeTabela() {
        return "servico";
    }

    @Override
    public String getSqlInserir() {
        //return "INSERT INTO "+getNomeTabela()+" (nome) VALUES (?)";
        return "INSERT INTO "+getNomeTabela()+" (nome, descricao, preco) VALUES (?, ?, ?)";
    }

    @Override
    public String getSqlAtualizarPorId() {
        return "UPDATE "+getNomeTabela()+" SET nome = ?, descricao = ?, preco = ? WHERE id = ?";
        //return "UPDATE "+getNomeTabela()+" SET nome = ?, preco = ?, duracao = ? WHERE id = ?";
    }
    
    @Override
    public String getSqlAtualizarPorUuid() {
        return "UPDATE "+getNomeTabela()+" SET nome = ?, descricao = ?, preco = ? WHERE uuid = ?";
        //return "UPDATE "+getNomeTabela()+" SET nome = ?, preco = ?, duracao = ? WHERE uuid = ?";
    }

    @Override
    public Servico retornarSelecionado() {
        try {
            Servico servico = null;
            if (resultSet != null) {
                if (resultSet.isBeforeFirst()){
                    JDBCUtil.movInicial(resultSet);
                }
                servico = new Servico();
                servico.setId(resultSet.getInt("id"));
                servico.setUuid(resultSet.getString("uuid"));
                servico.setNome(resultSet.getString("nome"));
                servico.setDescricao(resultSet.getString("descricao"));
                servico.setPreco(resultSet.getDouble("preco"));
            }
            return servico;
        } catch (SQLException err) {
            System.out.println("Erro ao acessar statement: " + err.getMessage());
        }
        return null;
    }
    
}
