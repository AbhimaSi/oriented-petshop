package com.mycompany.javapet.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Atendimento {
    private String id;
    private LocalDate data;
    private LocalTime hora;
    private String status;
    private String idPet;
    private int idFuncionario;
    private List<Servico> servicos;
    
    public Atendimento() { }
    
    public Atendimento(String id, LocalDate data, LocalTime hora, String status, String idPet, int idFuncionario, List<Servico> servicos) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.status = status;
        this.idPet = idPet;
        this.idFuncionario = idFuncionario;
        this.servicos = servicos;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdPet() {
        return idPet;
    }

    public void setIdPet(String idPet) {
        this.idPet = idPet;
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
    
    public List<Servico> getServicos() {
        return servicos;
    }
    
    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
}
