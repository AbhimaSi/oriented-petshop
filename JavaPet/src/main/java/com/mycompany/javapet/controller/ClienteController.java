package com.mycompany.javapet.controller;
import com.mycompany.javapet.controller.*;
import com.mycompany.javapet.model.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @GetMapping("/listar")
    public List<Cliente> listar() {
        clienteDAO.buscarTodos();
        List<Cliente> lista = clienteDAO.retornarLista();

        System.out.println("Lista retornada: " + lista);
        System.out.println("resultSet = " + clienteDAO.resultSet);

        return lista;
    }
    
    
    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable int id) {
        if (clienteDAO.buscarPorId(id)) {
            return clienteDAO.retornarSelecionado();
        }

        return null;
    }

    @PostMapping
    public boolean inserir(@RequestBody Cliente cliente) {
        return clienteDAO.inserir(cliente);
    }

    @PutMapping("/{id}")
    public boolean atualizar(
        @PathVariable int id,
        @RequestBody Cliente cliente) {
        cliente.setId(id);
        return clienteDAO.atualizar(cliente);
    }

    @DeleteMapping("/{id}")
    public boolean remover(@PathVariable int id) {
        clienteDAO.buscarPorId(id);
        return clienteDAO.remover(clienteDAO.retornarSelecionado());
    }
}