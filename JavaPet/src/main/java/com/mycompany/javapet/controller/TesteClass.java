package com.mycompany.javapet.controller;

import com.mycompany.javapet.model.Cliente;

public class TesteClass {
    public static void main(String arg[]){
//        Cliente c1 = new Cliente();
//        c1.setId("1");
//        c1.setNome("Gaspar");
//        c1.setEndereco("gaspar dutra, 598");
//        c1.setTelefone(439999999);
        
        Cliente c2 = new Cliente();
        c2.setId("7");
        c2.setNome("Teste");
        c2.setTelefone(0000000);
        c2.setEndereco("Teste, teste 123");
        
        ClienteDAO dao = new ClienteDAO();
        dao.atualizar(c2);
    }

}
