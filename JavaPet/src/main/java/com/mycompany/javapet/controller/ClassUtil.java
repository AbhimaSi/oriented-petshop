package com.mycompany.javapet.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ClassUtil {
    
    public static String[] getNomesDeAtributos(Class c){
        Field[] f = c.getDeclaredFields();
        ArrayList<String> atributos = new ArrayList<>();
        for(int i = 0; i < f.length; i++){
            atributos.add(f[i].getName());
        }
        
        return atributos.toArray(new String[0]);
    }
    
    public static Method encontrarGetterDeCampo(Method[] m, String campo){
        for(int i = 0; i < m.length; i++){
            String nomeMetodo = m[i].getName();
            
            if(nomeMetodo.startsWith("get")){
                nomeMetodo = nomeMetodo.replace("get", "");
                if(nomeMetodo.equalsIgnoreCase(campo)){
                    return m[i];
                }
            }
            else if(nomeMetodo.startsWith("is")){
                nomeMetodo = nomeMetodo.replace("is", "");
                if(nomeMetodo.equalsIgnoreCase(campo)){
                    return m[i];
                }
            }
        }
        return null;
    }
}
