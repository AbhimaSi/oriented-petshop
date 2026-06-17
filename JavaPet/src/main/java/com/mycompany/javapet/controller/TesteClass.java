package com.mycompany.javapet.controller;
import com.mycompany.javapet.model.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@SpringBootApplication
public class TesteClass {
    public static void main(String args[]){
        testarTudo(true);
        //SpringApplication.run(TesteClass.class, args);
        
    }

    public static void testarTudo(boolean limpar){
        testarCliente(limpar);
        testarAnimal(limpar);
        testarFuncionario(limpar);
        testarAtendimento(limpar);
        testarServico(limpar);
    }
    
    public static void testarCliente(boolean limpar){
        if(limpar){
            limparTabela("cliente");
        }
        Cliente c1 = new Cliente();
        c1.setNome("nome");
        c1.setTelefone("123");
        c1.setEndereco("teste rua");
        
        ClienteDAO dao = new ClienteDAO();
        
        dao.inserir(c1);
        dao.buscarTodos();
        JDBCUtil.movPrimeiro(dao.getResultSet());
        c1 = dao.retornarSelecionado();
        System.out.println("\nCliente inserido: ");
        printCliente(c1);
        
        c1.setNome("novo nome");
        dao.atualizar(c1);
        dao.buscarPorId(c1.getId());
        JDBCUtil.movPrimeiro(dao.getResultSet());
        c1 = dao.retornarSelecionado();
        System.out.println("\nCliente atualizado: ");
        printCliente(c1);
        
        if(dao.remover(c1)){
            System.out.println("\nCliente removido.");
        }
        else{
            System.out.println("\nCliente nao foi removido.");
        }
        System.out.println("\n");
    }
    public static void testarListaClientes(boolean limpar){
        if(limpar){
            limparTabela("cliente");
        }
        Cliente c1 = new Cliente();
        c1.setNome("juan");
        c1.setTelefone("727");
        c1.setEndereco("rua luis de lima, 347");
        Cliente c2 = new Cliente();
        c2.setNome("pedro");
        c2.setTelefone("420");
        c2.setEndereco("rua albert einstein, 142");
        Cliente c3 = new Cliente();
        c3.setNome("pablo");
        c3.setTelefone("123");
        c3.setEndereco("rua los angeles, 67");

        ClienteDAO dao = new ClienteDAO();
        dao.inserir(c1);
        dao.inserir(c2);
        dao.inserir(c3);
        dao.buscarTodos();
        ArrayList<Cliente> lista = dao.retornarLista();
        
        for(Cliente c: lista){
            printCliente(c);
        }
        
        System.out.println("\n");
        if(limpar){
            limparTabela("cliente");
        }
    }
    public static void testarAnimal(boolean limpar){
        if(limpar){
            limparTabela("animal");
            limparTabela("cliente");
        }
        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente c = new Cliente();
        c.setNome("ab");
        clienteDAO.inserir(c);
        clienteDAO.buscarTodos();
        JDBCUtil.movPrimeiro(clienteDAO.getResultSet());
        c = clienteDAO.retornarSelecionado();
        
        Animal a1 = new Animal();
        
        a1.setNome("nome");
        a1.setEspecie("cachorro");
        a1.setRaca("husky");
        a1.setIdCliente(c.getId());
        
        AnimalDAO dao = new AnimalDAO();
        dao.inserir(a1);
        dao.buscarTodos();
        JDBCUtil.movPrimeiro(dao.getResultSet());
        a1 = dao.retornarSelecionado();
        System.out.println("\nAnimal inserido: ");
        printAnimal(a1);
        
        a1.setNome("novo nome");
        dao.atualizar(a1);
        dao.buscarPorId(a1.getId());
        JDBCUtil.movPrimeiro(dao.getResultSet());
        a1 = dao.retornarSelecionado();
        System.out.println("\nAnimal atualizado: ");
        printAnimal(a1);
        
        if(dao.remover(a1)){
            System.out.println("\nAnimal removido.");
        }
        else{
            System.out.println("\nAnimal nao foi removido.");
        }
        System.out.println("\n");
    }
    //public static void testeProduto(){}
    public static void testarFuncionario(boolean limpar){
        if(limpar){
            limparTabela("funcionario");
        }
        Funcionario f1 = new Funcionario();
        f1.setNome("nome");
        f1.setEmail("email@javapet.com");
        f1.setSenha("1234");
        f1.setCargo("veterinario");
        
        FuncionarioDAO dao = new FuncionarioDAO();
        
        dao.inserir(f1);
        dao.buscarTodos();
        JDBCUtil.movPrimeiro(dao.getResultSet());
        f1 = dao.retornarSelecionado();
        System.out.println("\nFuncionario inserido: ");
        printFuncionario(f1);
        
        f1.setId(0); // definir 0 para testar busca por uuid
        f1.setNome("richard");
        f1.setEmail("richrd@javapet.com");
        f1.setSenha("SenhaMuitoComplexa123Zebra");
        dao.atualizar(f1);
        dao.buscarPorEmail(f1.getEmail());
        JDBCUtil.movPrimeiro(dao.getResultSet());
        f1 = dao.retornarSelecionado();
        System.out.println("\nFuncionario atualizado: ");
        printFuncionario(f1);
        
        if(dao.remover(f1)){
            System.out.println("\nFuncionario removido.");
        }
        else{
            System.out.println("\nFuncionario nao foi removido.");
        }
        System.out.println("\n");
    }
    public static void testarAtendimento(boolean limpar){
        if(limpar){
            limparTabela("animal_atendimento");
            limparTabela("animal");
            limparTabela("cliente");
            limparTabela("servico");
            limparTabela("funcionario");
            limparTabela("atendimento");
        }
        
        Cliente cliente = new Cliente();
        cliente.setNome("nome");
        cliente.setTelefone("123");
        cliente.setEndereco("teste rua");
        ClienteDAO daocliente = new ClienteDAO();
        daocliente.inserir(cliente);
        daocliente.buscarTodos();
        cliente = daocliente.retornarSelecionado();
        
        Animal animal = new Animal();
        animal.setNome("nome");
        animal.setEspecie("cachorro");
        animal.setRaca("husky");
        animal.setIdCliente(cliente.getId());
        AnimalDAO daoanimal = new AnimalDAO();
        daoanimal.inserir(animal);
        daoanimal.buscarTodos();
        animal = daoanimal.retornarSelecionado();
        
        Servico servico = new Servico();
        servico.setNome("Banho");
        servico.setDescricao("Banho com shampoo/condicionador");
        servico.setPreco(43.30);
        ServicoDAO daoservico = new ServicoDAO();
        daoservico.inserir(servico);
        daoservico.buscarTodos();
        servico = daoservico.retornarSelecionado();
        
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("richard");
        funcionario.setEmail("richrd@javapet.com");
        funcionario.setSenha("SenhaMuitoComplexa123Zebra");
        FuncionarioDAO daofuncionario = new FuncionarioDAO();
        daofuncionario.inserir(funcionario);
        daofuncionario.buscarTodos();
        funcionario = daofuncionario.retornarSelecionado();
        
        Atendimento atendimento = new Atendimento();
        atendimento.setData(LocalDate.now());
        atendimento.setHora(LocalTime.now());
        atendimento.setStatus("Em andamento");
        
        AtendimentoDAO dao = new AtendimentoDAO();
        
        dao.inserir(atendimento);
        dao.buscarTodos();
        JDBCUtil.movPrimeiro(dao.getResultSet());
        atendimento = dao.retornarSelecionado();
        System.out.println("\nAtendimento inserido: ");
        printAtendimento(atendimento);
        
        atendimento.setStatus("concluido");
        dao.atualizar(atendimento);
        dao.buscarPorId(atendimento.getId());
        atendimento = dao.retornarSelecionado();
        System.out.println("\nAtendimento atualizado: ");
        printAtendimento(atendimento);
        
        AtendimentoAnimal at = new AtendimentoAnimal();
        at.setId(atendimento.getId());
        at.setIdServico(servico.getId());
        at.setIdAnimal(animal.getId());
        at.setIdFuncionario(funcionario.getId());
        at.setDuracao(10);
        dao.inserirAtendimentoAnimal(at);
        dao.buscarAtendimentoAnimal();
        at = dao.retornarAtendimentoAnimalSelecionado();
        System.out.println(":::: Atendimento de Animal :::: <--- INSERCAO\n");
        printAtendimentoAnimal(at);
        
        at.setDuracao(60);
        dao.atualizarAtendimentoAnimalPorId(at);
        dao.buscarAtendimentoAnimalPorId(at);
        at = dao.retornarAtendimentoAnimalSelecionado();
        System.out.println(":::: Atendimento de Animal :::: <--- ATUALIZACAO\n");
        printAtendimentoAnimal(at);
        
        if(
                dao.removerAtendimentoAnimal(at)
                && daoanimal.remover(animal)
                && daoservico.remover(servico)
                && daofuncionario.remover(funcionario)
                && dao.remover(atendimento)
        )
        {
            System.out.println("\nTabelas foram removidas.");
        }
        else{
            System.out.println("\nTabelas nao foram removidas.");
        }
        System.out.println("\n");
    }
    public static void testarServico(boolean limpar){
        if(limpar){
            limparTabela("servico");
        }
        Servico s1 = new Servico();
        s1.setNome("Banho");
        s1.setDescricao("Serviços de banho e higienizacao de pet");
        s1.setPreco(43.30);
        
        
        ServicoDAO dao = new ServicoDAO();
        
        dao.inserir(s1);
        dao.buscarTodos();
        JDBCUtil.movPrimeiro(dao.getResultSet());
        s1 = dao.retornarSelecionado();
        System.out.println("\nServico inserido: ");
        printServico(s1);
        
        s1.setNome("Busca de animal");
        s1.setDescricao("Busca do pet em casa");
        s1.setPreco(199.00);
        dao.atualizar(s1);
        dao.buscarPorId(s1.getId());
        JDBCUtil.movPrimeiro(dao.getResultSet());
        s1 = dao.retornarSelecionado();
        System.out.println("\nServico atualizado: ");
        printServico(s1);
        
        if(dao.remover(s1)){
            System.out.println("\nServico removido.");
        }
        else{
            System.out.println("\nServico nao foi removido.");
        }
        System.out.println("\n");
    }
    
    public static void printCliente(Cliente c){
        System.out.println("......id: "+c.getId());
        System.out.println("....nome: "+c.getNome());
        System.out.println("telefone: "+c.getTelefone());
        System.out.println("endereco: "+c.getEndereco());
    }
    public static void printAnimal(Animal a){
        System.out.println("......id: "+a.getId());
        System.out.println("...uuid: " +a.getUuid());
        System.out.println("....nome: "+a.getNome());
        System.out.println(".especie: "+a.getEspecie());
        System.out.println("....raca: "+a.getRaca());
        System.out.println(".cliente: "+a.getIdCliente());
    }
    public static void printFuncionario(Funcionario f){
        System.out.println("........id: "+f.getId());
        System.out.println("......uuid: "+f.getUuid());
        System.out.println("......nome: "+f.getNome());
        System.out.println(".....email: "+f.getEmail());
        System.out.println(".....senha: "+f.getSenha());
        System.out.println(".....cargo: "+f.getCargo());
    }
    public static void printAtendimento(Atendimento a){
        System.out.println("......id: "+a.getId());
        System.out.println("....uuid: "+a.getUuid());
        System.out.println("....data: "+a.getData());
        System.out.println("....hora: "+a.getHora());
        System.out.println("..status: "+a.getStatus());
    }
    public static void printAtendimentoAnimal(AtendimentoAnimal a){
        System.out.println("......id: "+a.getId());
        System.out.println("idtabela: "+a.getIdTabela());
        System.out.println("....uuid: "+a.getUuid());
        System.out.println("....data: "+a.getData());
        System.out.println("....hora: "+a.getHora());
        System.out.println("..status: "+a.getStatus());
        System.out.println(".servico: "+a.getIdServico());
        System.out.println("..animal: "+a.getIdAnimal());
        System.out.println("..idfunc: "+a.getIdFuncionario());
        System.out.println(".duracao: "+a.getDuracao());
    }
    public static void printServico(Servico s){
        System.out.println(".......id: "+s.getId());
        System.out.println(".....uuid: "+s.getUuid());
        System.out.println(".....nome: "+s.getNome());
        System.out.println("descricao: "+s.getDescricao());
        System.out.println("....preco: "+s.getPreco());
    }
    
    
    public static void limparTabela(String nome){
        try{
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("DELETE FROM "+nome);
            pst.executeUpdate();
            conn.commit();
            pst.close();
        }
        catch(SQLException err){}
    }
    
    public static void limparTabelas(){
            limparTabela("animal_atendimento");
            limparTabela("animal");
            limparTabela("cliente");
            limparTabela("servico");
            limparTabela("funcionario");
            limparTabela("atendimento");
    }

}
