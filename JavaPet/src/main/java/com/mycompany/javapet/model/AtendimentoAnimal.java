package com.mycompany.javapet.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AtendimentoAnimal extends Atendimento{
    private int idTabela;   // <-- representa ID de animal_atendimento
    private int idServico;
    private int idAnimal;
    private int idFuncionario;
    private int duracao;
    
    public AtendimentoAnimal() { }
    
    public AtendimentoAnimal(int id, int idTabela, String uuid, LocalDate data, LocalTime hora, String status, int idServico, int idAnimal, int idFuncionario, int duracao) {
        super(id, uuid, data, hora, status);
        this.idTabela = idTabela;
        this.idServico = idServico;
        this.idAnimal = idAnimal;
        this.idFuncionario = idFuncionario;
        this.duracao = duracao;
    }

    public int getIdTabela() {
        return idTabela;
    }

    public void setIdTabela(int idTabela) {
        this.idTabela = idTabela;
    }
    
    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }
    
    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }
    
}

