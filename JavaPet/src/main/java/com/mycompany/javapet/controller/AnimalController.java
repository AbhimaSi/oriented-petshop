package com.mycompany.javapet.controller;
import com.mycompany.javapet.controller.*;
import com.mycompany.javapet.model.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalDAO animalDAO = new AnimalDAO();

    @GetMapping("/listar")
    public List<Animal> listar() {
        animalDAO.buscarTodos();
        List<Animal> lista = animalDAO.retornarLista();

        System.out.println("Lista retornada: " + lista);
        System.out.println("resultSet = " + animalDAO.resultSet);

        return lista;
    }
    
    
    @GetMapping("/{id}")
    public Animal buscar(@PathVariable int id) {
        if (animalDAO.buscarPorId(id)) {
            return animalDAO.retornarSelecionado();
        }

        return null;
    }

    @PostMapping
    public boolean inserir(@RequestBody Animal animal) {
        return animalDAO.inserir(animal);
    }

    @PutMapping("/{id}")
    public boolean atualizar(
        @PathVariable int id,
        @RequestBody Animal animal) {
        animal.setId(id);
        return animalDAO.atualizar(animal);
    }

    @DeleteMapping("/{id}")
    public boolean remover(@PathVariable String id) {
        return animalDAO.remover(animalDAO.retornarSelecionado());
    }
}