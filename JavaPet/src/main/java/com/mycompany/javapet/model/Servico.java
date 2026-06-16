package com.mycompany.javapet.model;

public class Servico implements Tabela{
    private int id;
    private String uuid;
    private String nome;
    private String descricao;
    private double preco;
    
    public Servico() { }
    
    public Servico(String nome, String uuid, String descricao, double preco) {
        this.nome = nome;
        this.uuid = uuid;
        this.descricao = descricao;
        this.preco = preco;
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

    public String getDescricao() { 
        return descricao; 
    }
    
    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
    }

    public double getPreco() { 
        return preco; 
    }
    
    public void setPreco(double preco) { 
        this.preco = preco; 
    }
}
