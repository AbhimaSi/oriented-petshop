package com.mycompany.javapet.controller;

import com.mycompany.javapet.model.Funcionario;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    
    @PostMapping("/login")
    public Funcionario login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");
        
        Funcionario funcionarioBanco = null;
        boolean cond = funcionarioDAO.buscarPorEmail(email);
        if(cond){
            funcionarioBanco = funcionarioDAO.retornarSelecionado();
            if(funcionarioBanco.getSenha().equals(senha)){
                return funcionarioBanco;
            }
        }
        return null;
    }

}
