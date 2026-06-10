package com.mycompany.javapet.controller;

import com.mycompany.javapet.model.Atendimento;
import java.util.ArrayList;

public class AtendimentoDAO extends GenericDAO<Atendimento> {

    public AtendimentoDAO(){
        super(Atendimento.class);
    }
    
    @Override
    public String getNomeTabela() {
        return "atendimento";
    }

    @Override
    public String getSqlInserir() {
        return "INSERT INTO "+getNomeTabela()+" (data_atendimento, hora_atendimento, status) VALUES (?, ?, ?)";
    }

    @Override
    public String getSqlAtualizar() {
        return "UPDATE "+getNomeTabela()+" SET data_atendimento = ?, hora_atendimento = ?, status = ? WHERE id = ?";
    }

    @Override
    public ArrayList<Atendimento> retornarLista() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Atendimento retornarSelecionado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
