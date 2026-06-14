package com.mycompany.javapet.controller;

import com.mycompany.javapet.model.Atendimento;
import java.sql.SQLException;

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
        return "INSERT INTO "+getNomeTabela()+" (data, hora, status) VALUES (?, ?, ?)";
    }

    @Override
    public String getSqlAtualizarPorId() {
        return "UPDATE "+getNomeTabela()+" SET data = ?, hora = ?, status = ? WHERE id = ?";
    }
    
    @Override
    public String getSqlAtualizarPorUuid() {
        return "UPDATE "+getNomeTabela()+" SET data = ?, hora = ?, status = ? WHERE uuid = ?";
    }

    @Override
    public Atendimento retornarSelecionado() {
        try {
            Atendimento atendimento = null;
            if (resultSet != null) {
                atendimento = new Atendimento();
                atendimento.setUuid(resultSet.getString("uuid"));
                atendimento.setId(resultSet.getInt("id"));
                atendimento.setData(resultSet.getDate("data").toLocalDate());
                atendimento.setHora(resultSet.getTime("hora").toLocalTime());
                atendimento.setStatus(resultSet.getString("status"));
                // falta de campos na tabela
                // alterar consulta p incluir atributos?
                /*
                    ##BANCO DE DADOS##

                    CREATE TABLE IF NOT EXISTS atendimento (
                            id SERIAL UNIQUE PRIMARY KEY,
                            data_atendimento DATE DEFAULT CURRENT_DATE,
                            hora_atendimento TIME DEFAULT CURRENT_TIME,
                            status VARCHAR(30)
                    );

                
                    ##CLASSE ENTIDADE##

                    private String id;
                    private LocalDate data;
                    private LocalTime hora;
                    private String status;
                    private String idPet;
                    private int idFuncionario;
                    private List<Servico> servicos;
                */
            }
            return atendimento;
        } catch (SQLException err) {
            System.out.println("Erro ao acessar statement: " + err.getMessage());
        }
        return null;
    }
}
