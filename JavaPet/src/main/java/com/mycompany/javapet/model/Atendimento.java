package com.mycompany.javapet.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Atendimento {
    private int id;
    private String uuid;
    private LocalDate data;
    private LocalTime hora;
    private String status;
    private String idPet;
    private int idFuncionario;
    private List<Servico> servicos;
    
    public Atendimento() { }
    
    public Atendimento(int id, String uuid, LocalDate data, LocalTime hora, String status, String idPet, int idFuncionario, List<Servico> servicos) {
        this.id = id;
        this.uuid = uuid;
        this.data = data;
        this.hora = hora;
        this.status = status;
        this.idPet = idPet;
        this.idFuncionario = idFuncionario;
        this.servicos = servicos;
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
