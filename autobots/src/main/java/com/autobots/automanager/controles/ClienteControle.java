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

@RestController
@RequestMapping("/cliente")
public class ClienteControle {
    @Autowired
    private ClienteRepositorio repositorio;
    @Autowired
    private ClienteSelecionador selecionador;

    @GetMapping("/{id}")
    public EntityModel<Cliente> obterCliente(@PathVariable long id) {
        List<Cliente> clientes = repositorio.findAll();
        Cliente cliente = selecionador.selecionar(clientes, id);

        // Cria o link para a própria entidade
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterCliente(id)).withSelfRel();

        // Cria o link para a lista de todos os clientes
        Link allClientsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterClientes()).withRel("todos-clientes");

        // Cria o link para a atualização do cliente
        Link updateLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).atualizarCliente(cliente)).withRel("atualizar");

        // Cria o link para a exclusão do cliente
        Link deleteLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).excluirCliente(cliente)).withRel("excluir");

        // Retorna o cliente encapsulado com os links HATEOAS
        return EntityModel.of(cliente, selfLink, allClientsLink, updateLink, deleteLink);
    }

    @GetMapping
    public List<Cliente> obterClientes() {
        List<Cliente> clientes = repositorio.findAll();
        return clientes;
    }

    @PostMapping("/cadastro")
    public void cadastrarCliente(@RequestBody Cliente cliente) {
        cliente.setDataCadastro(Calendar.getInstance().getTime());
        repositorio.save(cliente);
    }

    @PutMapping("/atualizar")
    public EntityModel<Cliente> atualizarCliente(@RequestBody Cliente atualizacao) {
        Cliente cliente = repositorio.getById(atualizacao.getId());
        ClienteAtualizador atualizador = new ClienteAtualizador();
        atualizador.atualizar(cliente, atualizacao);
        repositorio.save(cliente);

        // Criar o link de auto-relacionamento (self)
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterCliente(cliente.getId())).withSelfRel();
        return EntityModel.of(cliente, selfLink);
    }

    @DeleteMapping("/excluir")
    public EntityModel<Cliente> excluirCliente(@RequestBody Cliente exclusao) {
        Cliente cliente = repositorio.getById(exclusao.getId());
        repositorio.delete(cliente);

        // Criar link de auto-relacionamento após exclusão
        Link allClientsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterClientes()).withRel("todos-clientes");
        return EntityModel.of(cliente, allClientsLink);
    }

}
