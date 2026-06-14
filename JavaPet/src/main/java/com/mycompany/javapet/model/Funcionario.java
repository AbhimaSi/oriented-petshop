package com.mycompany.javapet.model;

public class Funcionario {
    private int id;
    private String uuid;
    private String nome;
    private String cargo;

    public Funcionario() { }
    
    public Funcionario(int id, String uuid, String nome, String cargo) {
        this.id = id;
        this.uuid = uuid;
        this.nome = nome;
        this.cargo = cargo;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
