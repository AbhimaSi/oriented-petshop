package com.mycompany.javapet.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil{
    private static String url = null;
    private static String username = null;
    private static String password = null;
    
    public static void init(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream(file);
        props.load(in);
        url = props.getProperty("jdbc.url");
        username = props.getProperty("jdbc.username");
        password = props.getProperty("jdbc.password");
        String driver = props.getProperty("jdbc.driver");
        if (url == null) {
            url = "";
        }
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        if (driver != null) {
            Class.forName(driver);
        }
    }
    
    public static String getUrl(){
        return url;
    }
    public static String getUsername(){
        return username;
    }
    public static String getPassword(){
        return password;
    }
    
    public static boolean hasElements(ResultSet rsdados){
        try{
            if(rsdados != null){
                if(rsdados.first()){
                    rsdados.beforeFirst();
                    return true;
                }
                else{
                    return false;
                }
            }

        }
        catch(SQLException err){
            System.err.println("Erro ao movimentar ponteiro: "+err.getMessage());
        }
        return false;
    }

    public static boolean movInicial(ResultSet rsdados){
            try{
                if(rsdados != null){
                        rsdados.beforeFirst();
                        return true;
                }
            }
            catch(SQLException err){
                System.err.println("Erro ao movimentar ponteiro: "+err.getMessage());
            }
            return false;
        }

    public static boolean movProximo(ResultSet rsdados){
        try{
            if(rsdados != null){
                if(!rsdados.isLast()){
                    rsdados.next();
                    return true;
                }
            }
        }
        catch(SQLException err){
            System.err.println("Erro ao movimentar ponteiro: "+err.getMessage());
        }
        return false;
    }
    
    public static boolean movAnterior(ResultSet rsdados){
        try{
            if(rsdados != null){
                if(!rsdados.isFirst()){
                    rsdados.previous();
                    return true;
                }
            }
        }
        catch(SQLException err){
            System.err.println("Erro ao movimentar ponteiro: "+err.getMessage());
        }
        return false;
    }
    
    public static boolean movPrimeiro(ResultSet rsdados){
        try{
            if(rsdados != null){
                if(!rsdados.isFirst()){
                    rsdados.first();
                    return true;
                }
            }
        }
        catch(SQLException err){
            System.err.println("Erro ao movimentar ponteiro: "+err.getMessage());
        }
        return false;
    }
    
    public static boolean movUltimo(ResultSet rsdados){
        try{
            if(rsdados != null){
                if(!rsdados.isLast()){
                    rsdados.last();
                    return true;
                }
            }
        }
        catch(SQLException err){
            System.err.println("Erro ao movimentar ponteiro: "+err.getMessage());
        }
        return false;
    }
}

