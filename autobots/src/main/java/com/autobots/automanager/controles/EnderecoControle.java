package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
    @Autowired
    private EnderecoRepositorio enderecoRepositorio;
    
    @GetMapping
    public List<Endereco> listarEnderecos() {
        return enderecoRepositorio.findAll();
    }
    
    @PostMapping("/cadastrar/{idCliente}")
    public void cadastrarEndereco(@PathVariable Long idCliente, @RequestBody Endereco endereco) {
        Cliente cliente = clienteRepositorio.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + idCliente));
        
        cliente.setEndereco(endereco);
        clienteRepositorio.save(cliente);
    }
    
    @PutMapping("/atualizar/{id}")
    public void atualizarEndereco(@PathVariable Long id, @RequestBody Endereco atualizacao) {
        Endereco endereco = enderecoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com id: " + id));
        
        EnderecoAtualizador atualizador = new EnderecoAtualizador();
        atualizador.atualizar(endereco, atualizacao);
        
        enderecoRepositorio.save(endereco);
    }
    
    @DeleteMapping("/deletar/{id}")
    public void deletarEndereco(@PathVariable Long id) {
        Endereco endereco = enderecoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com id: " + id));

        List<Cliente> clientes = clienteRepositorio.findAll();
        for (Cliente cliente : clientes) {
            if (cliente.getEndereco() != null && cliente.getEndereco().getId().equals(id)) {
                cliente.setEndereco(null);
                clienteRepositorio.save(cliente);
            }
        }
        enderecoRepositorio.delete(endereco);
    }
}