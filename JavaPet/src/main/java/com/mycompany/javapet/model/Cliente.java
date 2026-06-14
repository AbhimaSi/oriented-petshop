package com.mycompany.javapet.model;

import java.util.List;

public class Cliente {
    private int id;
    private String uuid;
    private String nome;
    private int telefone;
    private String endereco;
    private List<Animal> animais;
    
    public Cliente() { }
    
    public Cliente(int id, String uuid, String nome, int telefone, String endereco, List<Animal> animais) {
        this.id = id;
        this.uuid = uuid;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.animais = animais;
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

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<Animal> getAnimais() {
        return animais;
    }

    public void setAnimais(List<Animal> animais) {
        this.animais = animais;
    }
}
