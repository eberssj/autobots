package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/cadastrar/{idCliente}")
    public void cadastrarDocumento(@PathVariable Long idCliente, @RequestBody Documento documento) {
        Cliente cliente = clienteRepositorio.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + idCliente));

        cliente.getDocumentos().add(documento);
        clienteRepositorio.save(cliente);
    }

    @PutMapping("/atualizar/{id}")
    public void atualizarDocumento(@PathVariable Long id, @RequestBody Documento atualizacao) {
        Documento documento = documentoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado com id: " + id));

        DocumentoAtualizador atualizador = new DocumentoAtualizador();
        atualizador.atualizar(documento, atualizacao);

        documentoRepositorio.save(documento);
    }


    @DeleteMapping("/deletar/{id}")
    public void deletarDocumento(@PathVariable Long id) {
        Documento documento = documentoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado com id: " + id));
        List<Cliente> clientes = clienteRepositorio.findAll();
        for (Cliente cliente : clientes) {
            cliente.getDocumentos().removeIf(d -> d.getId().equals(id));
            clienteRepositorio.save(cliente);
        }

        documentoRepositorio.delete(documento);
    }
}
