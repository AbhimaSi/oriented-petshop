package com.mycompany.javapet.controller;
import com.mycompany.javapet.controller.*;
import com.mycompany.javapet.model.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoDAO servicoDAO = new ServicoDAO();

    @GetMapping("/listar")
    public List<Servico> listar() {
        servicoDAO.buscarTodos();
        List<Servico> lista = servicoDAO.retornarLista();

        System.out.println("Lista retornada: " + lista);
        System.out.println("resultSet = " + servicoDAO.resultSet);

        return lista;
    }
    
    
    @GetMapping("/{id}")
    public Servico buscar(@PathVariable int id) {
        if (servicoDAO.buscarPorId(id)) {
            System.out.println("\nObjeto encontrado: " + servicoDAO.resultSet);
            return servicoDAO.retornarSelecionado();
        }

        return null;
    }

    @PostMapping
    public boolean inserir(@RequestBody Servico servico) {
        System.out.println("Nome: " + servico.getNome());
        System.out.println("Descricao: " + servico.getDescricao());
        System.out.println("Preco: " + servico.getPreco());
        return servicoDAO.inserir(servico);
    }

    @PutMapping("/{id}")
    public boolean atualizar(
        @PathVariable int id,
        @RequestBody Servico servico) {
        servico.setId(id);
        return servicoDAO.atualizar(servico);
    }

    @DeleteMapping("/{id}")
    public boolean remover(@PathVariable int id) {
        servicoDAO.buscarPorId(id);
        Servico s = servicoDAO.retornarSelecionado();
        return servicoDAO.remover(s);
    }
}