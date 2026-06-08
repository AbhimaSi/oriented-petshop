package com.mycompany.javapet.controller;

import com.mycompany.javapet.model.Cliente;
import java.util.ArrayList;

public class ClienteDAO extends GenericDAO<Cliente> {

    public ClienteDAO(){
        super(Cliente.class);
    }
    
    @Override
    public String getNomeTabela() {
        return "cliente";
    }

    @Override
    public String getSqlInserir() {
        return  "INSERT INTO "+getNomeTabela()+" (nome, telefone, endereco) VALUES (?, ?, ?)";
    }

//    @Override
//    public String getSqlAtualizar() {
//        return "UPDATE "+getNomeTabela()+" SET nome = ?, telefone = ?, endereco = ? WHERE id = ?";
//    }

    @Override
    public ArrayList<Cliente> retornarLista() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Cliente retornarSelecionado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
