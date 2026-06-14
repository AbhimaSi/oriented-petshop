package com.mycompany.javapet.model;

public class Animal implements Tabela {
    private int id;
    private String uuid;
    private String nome;
    private String especie;
    private String raca;
    private int idCliente;
    
    public Animal() {}
    
    public Animal(int id, String uuid, String nome, String especie, String raca, int idCliente) {
        this.id = id;
        this.uuid = uuid;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idCliente = idCliente;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() { 
        return uuid; 
    }
    
    public void setUuid(String uuid) { 
        this.uuid = uuid;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
