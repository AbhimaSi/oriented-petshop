package com.mycompany.javapet.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection = null;
    private static final String path = System.getProperty("user.dir");
    private static final File config = new File(path + "/.env");
    
    public DBConnection(){};
    
    public static Connection getConnection(){
        if(connection != null){
            return connection;
        }
        else{
            if(criarConexao()){
                return connection;
            }
        }
        return null;
    }
    
    public static boolean criarConexao(){
        try{
            JDBCUtil.init(config);
            String url = JDBCUtil.getUrl();
            String username = JDBCUtil.getUsername();
            String password = JDBCUtil.getPassword();
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(true);
            return true;
        }
        catch(FileNotFoundException err){
            System.err.println("Arquivo não encontrado: "+err.getMessage());
        }
        catch(IOException err){
            System.err.println("Erro ao ler arquivo: "+err.getMessage());
        }
        catch(ClassNotFoundException err){
            System.err.println("Driver não encontrado: "+err.getMessage());
        }
        catch(SQLException err){
            System.err.println("Erro ao inicializar conexão: "+err.getMessage());
        }
        return false;
    }
    
    public static boolean fecharConexao(){
        try{
            if(connection != null){
                connection.close();
                connection = null;
                return true;
            }
        }
        catch(SQLException err){
            System.err.println("Erro ao fechar conexão com banco de dados: "+err.getMessage());
        }
        return false;
    }
}
