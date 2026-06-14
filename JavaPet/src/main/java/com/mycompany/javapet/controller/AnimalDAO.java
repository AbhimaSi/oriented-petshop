package com.mycompany.javapet.controller;

import static com.mycompany.javapet.controller.GenericDAO.resultSet;
import com.mycompany.javapet.model.Animal;
import java.sql.SQLException;

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
    public String getSqlAtualizarPorId() {
        return "UPDATE "+getNomeTabela()+" SET idcliente = ?, nome = ?, especie = ?, raca = ? WHERE id = ?";
    }

    @Override
    public Animal retornarSelecionado() {
        try {
            Animal animal = null;
            if (resultSet != null) {
                animal = new Animal();
                animal.setId(resultSet.getInt("id"));
                animal.setUuid(resultSet.getString("uuid"));
                animal.setNome(resultSet.getString("nome"));
                animal.setEspecie(resultSet.getString("especie"));
                animal.setRaca(resultSet.getString("raca"));
                animal.setIdCliente(resultSet.getInt("idcliente"));
            }
            return animal;
        } catch (SQLException err) {
            System.out.println("Erro ao acessar statement: " + err.getMessage());
        }
        return null;
    }
}
