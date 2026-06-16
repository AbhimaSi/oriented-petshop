package com.mycompany.javapet.controller;
import com.mycompany.javapet.controller.*;
import com.mycompany.javapet.model.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @GetMapping("/listar")
    public List<Funcionario> listar() {
        funcionarioDAO.buscarTodos();
        List<Funcionario> lista = funcionarioDAO.retornarLista();

        System.out.println("Lista retornada: " + lista);
        System.out.println("resultSet = " + funcionarioDAO.resultSet);

        return lista;
    }
    
    
    @GetMapping("/{id}")
    public Funcionario buscar(@PathVariable int id) {
        if (funcionarioDAO.buscarPorId(id)) {
            return funcionarioDAO.retornarSelecionado();
        }

        return null;
    }

    @PostMapping
    public boolean inserir(@RequestBody Funcionario funcionario) {
        return funcionarioDAO.inserir(funcionario);
    }

    @PutMapping("/{id}")
    public boolean atualizar(
        @PathVariable int id,
        @RequestBody Funcionario funcionario) {
        funcionario.setId(id);
        return funcionarioDAO.atualizar(funcionario);
    }

    @DeleteMapping("/{id}")
    public boolean remover(@PathVariable int id) {
        funcionarioDAO.buscarPorId(id);
        return funcionarioDAO.remover(funcionarioDAO.retornarSelecionado());
    }
}