package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection jdbcunique = null;
    private static Connection connection = null;
    private static String path = System.getProperty("user.dir");
    private static File config = new File(path + "/src/jdbc.config");
    
    private DBConnection(){}
    
    public static DBConnection getInstance(){
        if(jdbcunique != null){
            return jdbcunique;
        }
        else{
            jdbcunique = new DBConnection();
            return jdbcunique;
        }
    }
    
    public boolean criarConexao(){
        try{
            JDBCUtil.init(config);
            connection = JDBCUtil.getConnection();
            connection.setAutoCommit(false);
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
    
    public boolean fecharConexao(){
        try{
            if(connection != null){
                connection.close();
                return true;
            }
        }
        catch(SQLException err){
            System.err.println("Erro ao fechar conexão com banco de dados: "+err.getMessage());
        }
        return false;
    }
}
