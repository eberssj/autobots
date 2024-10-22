package com.autobots.automanager.modelo;

import com.autobots.automanager.entidades.Cliente;
import java.util.Objects;

public class ClienteAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();
    private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();
    private DocumentoAtualizador documentoAtualizador = new DocumentoAtualizador();
    private TelefoneAtualizador telefoneAtualizador = new TelefoneAtualizador();

    private void atualizarDados(Cliente cliente, Cliente atualizacao) {
        if (!verificador.verificar(atualizacao.getNome())) {
            cliente.setNome(atualizacao.getNome());
        }
        if (!verificador.verificar(atualizacao.getNomeSocial())) {
            cliente.setNomeSocial(atualizacao.getNomeSocial());
        }
        if (Objects.nonNull(atualizacao.getDataCadastro())) {
            cliente.setDataCadastro(atualizacao.getDataCadastro());
        }
        if (Objects.nonNull(atualizacao.getDataNascimento())) {
            cliente.setDataNascimento(atualizacao.getDataNascimento());
        }
    }

    public void atualizar(Cliente cliente, Cliente atualizacao) {
        atualizarDados(cliente, atualizacao);
        
        // Atualizando o endereço, documentos e telefones
        if (atualizacao.getEndereco() != null) {
            enderecoAtualizador.atualizar(cliente.getEndereco(), atualizacao.getEndereco());
        }
        if (atualizacao.getDocumentos() != null) {
            documentoAtualizador.atualizar(cliente.getDocumentos(), atualizacao.getDocumentos());
        }
        if (atualizacao.getTelefones() != null) {
            telefoneAtualizador.atualizar(cliente.getTelefones(), atualizacao.getTelefones());
        }
    }
}
