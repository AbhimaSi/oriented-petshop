package com.mycompany.javapet.controller;

import com.mycompany.javapet.model.Servico;
import java.util.ArrayList;

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
        return "INSERT INTO "+getNomeTabela()+" (nome, preco, duracao) VALUES (?, ?, ?)";
    }

    @Override
    public String getSqlAtualizar() {
        return "UPDATE "+getNomeTabela()+" SET nome = ?, preco = ?, duracao = ? WHERE id = ?";
    }

    @Override
    public ArrayList<Servico> retornarLista() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Servico retornarSelecionado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
