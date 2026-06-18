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
    public List<AtendimentoAnimal> listar() {
        atendimentoDAO.buscarAtendimentoAnimal();
        List<AtendimentoAnimal> lista = atendimentoDAO.retornarListaAtendimentoAnimal();

        System.out.println("Lista retornada: " + lista);
        System.out.println("resultSet = " + atendimentoDAO.resultSet);

        return lista;
    }
    
    
    @GetMapping("/{id}")
    public AtendimentoAnimal buscar(@PathVariable int id) {
        if (atendimentoDAO.buscarAtendimentoAnimalPorId(id)) {
            return atendimentoDAO.retornarAtendimentoAnimalSelecionado();
        }

        return null;
    }

    @PostMapping
    public boolean inserir(@RequestBody AtendimentoAnimal atendimento) {
        return atendimentoDAO.inserirAtendimentoCompleto(atendimento);
    }

    @PutMapping("/{id}")
    public boolean atualizar(
        @PathVariable int id,
        @RequestBody AtendimentoAnimal atendimento) {
        atendimento.setId(id);
        return atendimentoDAO.atualizarAtendimentoCompleto(atendimento);
    }

    @DeleteMapping("/{id}")
    public boolean remover(@PathVariable int id) {
        return atendimentoDAO.removerAtendimentoCompleto(id);
    }
}
