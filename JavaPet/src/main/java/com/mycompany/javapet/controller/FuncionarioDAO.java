package com.mycompany.javapet.controller;

import com.mycompany.javapet.model.Funcionario;
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
    public ArrayList<Funcionario> retornarLista() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Funcionario retornarSelecionado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
