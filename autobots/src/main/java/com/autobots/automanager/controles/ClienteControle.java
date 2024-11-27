package com.autobots.automanager.controles;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

import org.hibernate.Hibernate;

@RestController
@RequestMapping("/cliente")
public class ClienteControle {
    @Autowired
    private ClienteRepositorio repositorio;
    @Autowired
    private ClienteSelecionador selecionador;

 

    @GetMapping
    public List<Cliente> obterClientes() {
        List<Cliente> clientes = repositorio.findAll();
        return clientes;
    }

    @GetMapping("/{id}")
    public Cliente obterCliente(@PathVariable long id) {
        return repositorio.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
    }

    @PostMapping("/cadastro")
    public void cadastrarCliente(@RequestBody Cliente cliente) {
        cliente.setDataCadastro(Calendar.getInstance().getTime());
        repositorio.save(cliente);
    }

    @PutMapping("/atualizar/{id}")
    public EntityModel<Cliente> atualizarCliente(@PathVariable long id, @RequestBody Cliente atualizacao) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente != null) {
            ClienteAtualizador atualizador = new ClienteAtualizador();
            atualizador.atualizar(cliente, atualizacao);
            repositorio.save(cliente);

            // Inicializar as relações antes de retornar
            Hibernate.initialize(cliente.getDocumentos());
            Hibernate.initialize(cliente.getEndereco());
            Hibernate.initialize(cliente.getTelefones());

            // Criar link para a entidade atualizada
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterCliente(cliente.getId())).withSelfRel();
            return EntityModel.of(cliente, selfLink);
        }
        // Caso o cliente não seja encontrado, lançar uma exceção ou retornar um erro adequado
        throw new RuntimeException("Cliente não encontrado com ID: " + id);
    }
    
    @DeleteMapping("/excluir/{id}")
    public EntityModel<Cliente> excluirCliente(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente != null) {
            repositorio.delete(cliente);
            // Criar link para a lista de todos os clientes após exclusão
            Link allClientsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterClientes()).withRel("todos-clientes");
            return EntityModel.of(cliente, allClientsLink);
        }
        // Caso o cliente não seja encontrado, lançar uma exceção ou retornar um erro adequado
        throw new RuntimeException("Cliente não encontrado com ID: " + id);
    }


}
