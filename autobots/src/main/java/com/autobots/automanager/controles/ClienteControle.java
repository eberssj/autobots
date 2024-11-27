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
    public EntityModel<Cliente> obterCliente(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));

        // Links principais
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterCliente(cliente.getId())).withSelfRel();
        Link allClientsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterClientes()).withRel("todos-clientes");

        // Links de navegação para ações relacionadas
        Link updateLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).atualizarCliente(cliente.getId(), cliente)).withRel("atualizar-cliente");
        Link deleteLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).excluirCliente(cliente.getId())).withRel("excluir-cliente");

        // Links para recursos relacionados (documentos, telefones, endereços, etc.)
        Link documentosLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentosPorCliente(cliente.getId())).withRel("documentos");
        Link telefonesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefonesPorCliente(cliente.getId())).withRel("telefones");
        Link enderecoLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecoPorCliente(cliente.getId())).withRel("endereco");

        // Retorna o cliente com todos os links
        return EntityModel.of(cliente, selfLink, allClientsLink, updateLink, deleteLink, documentosLink, telefonesLink, enderecoLink);
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

            // Links principais
            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterCliente(cliente.getId())).withSelfRel();
            Link allClientsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterClientes()).withRel("todos-clientes");

            // Links de navegação para ações relacionadas
            Link updateLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).atualizarCliente(cliente.getId(), cliente)).withRel("atualizar-cliente");
            Link deleteLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).excluirCliente(cliente.getId())).withRel("excluir-cliente");

            // Links para recursos relacionados (documentos, telefones, endereços, etc.)
            Link documentosLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentosPorCliente(cliente.getId())).withRel("documentos");
            Link telefonesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefonesPorCliente(cliente.getId())).withRel("telefones");
            Link enderecoLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecoPorCliente(cliente.getId())).withRel("endereco");

            // Retorna o cliente com todos os links
            return EntityModel.of(cliente, selfLink, allClientsLink, updateLink, deleteLink, documentosLink, telefonesLink, enderecoLink);
        }
        throw new RuntimeException("Cliente não encontrado com ID: " + id);
    }

    @DeleteMapping("/excluir/{id}")
    public EntityModel<Cliente> excluirCliente(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente != null) {
            // Excluindo o cliente
            repositorio.delete(cliente);

            // Links principais para navegação
            Link allClientsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterClientes()).withRel("todos-clientes");

            // Links para recursos relacionados (documentos, telefones, endereços, etc.)
            Link documentosLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumentosPorCliente(cliente.getId())).withRel("documentos");
            Link telefonesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefonesPorCliente(cliente.getId())).withRel("telefones");
            Link enderecoLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class).obterEnderecoPorCliente(cliente.getId())).withRel("endereco");

            // Link para obter a lista de clientes após a exclusão
            Link listClientsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteControle.class).obterClientes()).withRel("listar-clientes");

            // Retorna o cliente excluído com links para navegação
            return EntityModel.of(cliente, allClientsLink, documentosLink, telefonesLink, enderecoLink, listClientsLink);
        }

        // Caso o cliente não seja encontrado
        throw new RuntimeException("Cliente não encontrado com ID: " + id);
    }


}
