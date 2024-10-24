package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private DocumentoRepositorio documentoRepositorio;

    @GetMapping
    public List<Documento> listarDocumentos() {
        return documentoRepositorio.findAll();
    }

    @GetMapping("/{id}")
    public EntityModel<Documento> obterDocumento(@PathVariable Long id) {
        Documento documento = documentoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado com id: " + id));

        // Criar o link para o próprio documento
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumento(id)).withSelfRel();

        // Criar o link para a listagem de documentos
        Link allDocumentsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).listarDocumentos()).withRel("todos-documentos");

        return EntityModel.of(documento, selfLink, allDocumentsLink);
    }

    @PostMapping("/cadastrar/{idCliente}")
    public EntityModel<Documento> cadastrarDocumento(@PathVariable Long idCliente, @RequestBody Documento documento) {
        Cliente cliente = clienteRepositorio.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + idCliente));

        cliente.getDocumentos().add(documento);
        clienteRepositorio.save(cliente);

        // Criar o link para o documento recém-criado
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumento(documento.getId())).withSelfRel();

        return EntityModel.of(documento, selfLink);
    }

    @PutMapping("/atualizar/{id}")
    public EntityModel<Documento> atualizarDocumento(@PathVariable Long id, @RequestBody Documento atualizacao) {
        Documento documento = documentoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado com id: " + id));

        DocumentoAtualizador atualizador = new DocumentoAtualizador();
        atualizador.atualizar(documento, atualizacao);

        documentoRepositorio.save(documento);

        // Criar o link para o documento atualizado
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).obterDocumento(id)).withSelfRel();

        return EntityModel.of(documento, selfLink);
    }

    @DeleteMapping("/deletar/{id}")
    public EntityModel<Documento> deletarDocumento(@PathVariable Long id) {
        Documento documento = documentoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado com id: " + id));
        List<Cliente> clientes = clienteRepositorio.findAll();
        for (Cliente cliente : clientes) {
            cliente.getDocumentos().removeIf(d -> d.getId().equals(id));
            clienteRepositorio.save(cliente);
        }

        documentoRepositorio.delete(documento);

        // Criar o link para a lista de documentos após a exclusão
        Link allDocumentsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class).listarDocumentos()).withRel("todos-documentos");

        return EntityModel.of(documento, allDocumentsLink);
    }
}
