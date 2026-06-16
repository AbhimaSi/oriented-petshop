package com.mycompany.javapet.controller;
import com.mycompany.javapet.controller.*;
import com.mycompany.javapet.model.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {

    private final AtendimentoDAO atendimentoDAO = new AtendimentoDAO();

    @GetMapping("/listar")
    public List<Atendimento> listar() {
        atendimentoDAO.buscarTodos();
        List<Atendimento> lista = atendimentoDAO.retornarLista();

        System.out.println("Lista retornada: " + lista);
        System.out.println("resultSet = " + atendimentoDAO.resultSet);

        return lista;
    }
    
    
    @GetMapping("/{id}")
    public Atendimento buscar(@PathVariable int id) {
        if (atendimentoDAO.buscarPorId(id)) {
            return atendimentoDAO.retornarSelecionado();
        }

        return null;
    }

    @PostMapping
    public boolean inserir(@RequestBody Atendimento atendimento) {
        return atendimentoDAO.inserir(atendimento);
    }

    @PutMapping("/{id}")
    public boolean atualizar(
        @PathVariable int id,
        @RequestBody Atendimento atendimento) {
        atendimento.setId(id);
        return atendimentoDAO.atualizar(atendimento);
    }

    @DeleteMapping("/{id}")
    public boolean remover(@PathVariable int id) {
        atendimentoDAO.buscarPorId(id);
        return atendimentoDAO.remover(atendimentoDAO.retornarSelecionado());
    }
}