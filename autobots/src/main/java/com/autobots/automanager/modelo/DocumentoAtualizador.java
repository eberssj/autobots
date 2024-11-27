package com.autobots.automanager.modelo;

import com.autobots.automanager.entidades.Documento;
import java.util.List;
import java.util.Objects;

public class DocumentoAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Documento documento, Documento atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getTipo())) {
                documento.setTipo(atualizacao.getTipo());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                documento.setNumero(atualizacao.getNumero());
            }
        }
    }

    public void atualizar(List<Documento> documentos, List<Documento> atualizacoes) {
        if (atualizacoes == null) return;

        // Atualizar ou adicionar novos documentos
        for (Documento atualizacao : atualizacoes) {
            boolean encontrado = false;
            for (Documento documento : documentos) {
                if (Objects.equals(atualizacao.getId(), documento.getId())) {
                    atualizar(documento, atualizacao);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                documentos.add(atualizacao); // Novo documento
            }
        }

        // Remover documentos que não estão na atualização
        documentos.removeIf(documento -> 
            atualizacoes.stream().noneMatch(atualizacao -> 
                Objects.equals(atualizacao.getId(), documento.getId())));
    }
}
