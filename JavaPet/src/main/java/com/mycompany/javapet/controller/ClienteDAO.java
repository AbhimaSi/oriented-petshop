package com.mycompany.javapet.controller;

import com.mycompany.javapet.model.Cliente;
import java.sql.SQLException;
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

    @Override
    public String getSqlAtualizar() {
        return "UPDATE "+getNomeTabela()+" SET nome = ?, telefone = ?, endereco = ? WHERE id = ?";
    }

    @Override
    public ArrayList<Cliente> retornarLista() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Cliente retornarSelecionado() {
        try{
            Cliente cliente = null;
            if(resultSet != null){
                cliente = new Cliente();
                cliente.setId(String.valueOf(resultSet.getInt("id")));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setTelefone(resultSet.getInt("telefone"));
                cliente.setEndereco(resultSet.getString("endereco"));
            }
            return cliente;
        }
        catch(SQLException err){
            System.out.println("Erro ao acessar statement: "+err.getMessage());
        }
        return null;
    }
}
