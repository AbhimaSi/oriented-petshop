package com.mycompany.javapet.controller;

import com.mycompany.javapet.model.Animal;
import java.util.ArrayList;

public class AnimalDAO extends GenericDAO<Animal> {

    public AnimalDAO(){
        super(Animal.class);
    }
    
    @Override
    public String getNomeTabela() {
        return "animal";
    }

    @Override
    public String getSqlInserir() {
        return "INSERT INTO "+getNomeTabela()+" (idcliente, nome, especie, raca) VALUES (?, ?, ?, ?)";
    }

    @Override
    public String getSqlAtualizar() {
        return "UPDATE "+getNomeTabela()+" SET idcliente = ?, nome = ?, especie = ?, raca = ? WHERE id = ?";
    }

    @Override
    public ArrayList<Animal> retornarLista() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Animal retornarSelecionado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
