package com.mycompany.javapet.controller;

import static com.mycompany.javapet.controller.GenericDAO.resultSet;
import com.mycompany.javapet.model.Funcionario;
import java.sql.SQLException;
import java.util.ArrayList;

public class FuncionarioDAO extends GenericDAO<Funcionario> {
    
    public FuncionarioDAO(){
        super(Funcionario.class);
    }
    
    @Override
    public String getNomeTabela() {
        return "funcionario";
    }

    @Override
    public String getSqlInserir() {
        return "INSERT INTO "+getNomeTabela()+" (nome, cargo) VALUES (?, ?)";
    }

    @Override
    public String getSqlAtualizar() {
        return "UPDATE "+getNomeTabela()+" SET nome = ?, cargo = ? WHERE id = ?";
    }

    @Override
    public Funcionario retornarSelecionado() {
        try {
            Funcionario funcionario = null;
            if (resultSet != null) {
                funcionario = new Funcionario();
                funcionario.setId(resultSet.getInt("id"));
                funcionario.setUuid(resultSet.getString("uuid"));
                funcionario.setNome(resultSet.getString("nome"));
                funcionario.setCargo(resultSet.getString("cargo"));
            }
            return funcionario;
        } catch (SQLException err) {
            System.out.println("Erro ao acessar statement: " + err.getMessage());
        }
        return null;
    }

}
