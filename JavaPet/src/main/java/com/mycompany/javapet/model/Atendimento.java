package com.mycompany.javapet.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Atendimento implements Tabela {
    private int id;
    private String uuid;
    private LocalDate data;
    private LocalTime hora;
    private String status;
    
    public Atendimento() { }
    
    public Atendimento(int id, String uuid, LocalDate data, LocalTime hora, String status) {
        this.id = id;
        this.uuid = uuid;
        this.data = data;
        this.hora = hora;
        this.status = status;
        
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
}
