package com.mycompany.javapet.controller;

import static com.mycompany.javapet.controller.GenericDAO.resultSet;
import static com.mycompany.javapet.controller.GenericDAO.statement;
import com.mycompany.javapet.model.Funcionario;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FuncionarioDAO extends GenericDAO<Funcionario> {
    
    public FuncionarioDAO(){
        super(Funcionario.class);
    }
    
    @Override
    public String getNomeTabela() {
        return "funcionario";
    }

    @Override
    public String getSqlInserir() {
        return "INSERT INTO "+getNomeTabela()+" (nome, email, senha, cargo) VALUES (?, ?, ?, ?)";
    }

    @Override
    public String getSqlAtualizarPorId() {
        return "UPDATE "+getNomeTabela()+" SET nome = ?, email = ?, senha = ?, cargo = ? WHERE id = ?";
    }

    @Override
    public String getSqlAtualizarPorUuid() {
        return "UPDATE "+getNomeTabela()+" SET nome = ?, email = ?, senha = ?, cargo = ? WHERE uuid = ?";
    }

    public String getSqlBuscarPorEmail(){
        return "SELECT * FROM "+getNomeTabela()+" WHERE email = ?";
    }
    
    public boolean buscarPorEmail(String email){
        String sqlBuscarEmail = getSqlBuscarPorEmail();
        
        try{
            int type = ResultSet.TYPE_SCROLL_SENSITIVE;
            int conc = ResultSet.CONCUR_READ_ONLY;
            statement = this.connection.prepareStatement(sqlBuscarEmail, type, conc);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            return true;
        }
        catch(SQLException err){
            System.err.println("Erro durante consulta por uuid: "+err.getMessage());
        }
        return false;
    }
    
    @Override
    public Funcionario retornarSelecionado() {
        try {
             if (resultSet.isBeforeFirst()){
                    JDBCUtil.movProximo(resultSet);
            }
            Funcionario funcionario = null;
            if (resultSet != null) {
                
                funcionario = new Funcionario();
                funcionario.setId(resultSet.getInt("id"));
                funcionario.setUuid(resultSet.getString("uuid"));
                funcionario.setNome(resultSet.getString("nome"));
                funcionario.setEmail(resultSet.getString("email"));
                funcionario.setSenha(resultSet.getString("senha"));
                funcionario.setCargo(resultSet.getString("cargo"));
            }
            return funcionario;
        } catch (SQLException err) {
            System.out.println("Erro ao acessar statement: " + err.getMessage());
        }
        return null;
    }

}
