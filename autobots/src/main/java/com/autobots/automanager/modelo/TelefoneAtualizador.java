package com.autobots.automanager.modelo;

import com.autobots.automanager.entidades.Telefone;
import java.util.List;
import java.util.Objects;

public class TelefoneAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(Telefone telefone, Telefone atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getDdd())) {
                telefone.setDdd(atualizacao.getDdd());
            }
            if (!verificador.verificar(atualizacao.getNumero())) {
                telefone.setNumero(atualizacao.getNumero());
            }
        }
    }

    public void atualizar(List<Telefone> telefones, List<Telefone> atualizacoes) {
        if (atualizacoes == null) return;

        // Atualizar ou adicionar novos telefones
        for (Telefone atualizacao : atualizacoes) {
            boolean encontrado = false;
            for (Telefone telefone : telefones) {
                if (Objects.equals(atualizacao.getId(), telefone.getId())) {
                    atualizar(telefone, atualizacao);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                telefones.add(atualizacao); // Novo telefone
            }
        }

        // Remover telefones que não estão na atualização
        telefones.removeIf(telefone -> 
            atualizacoes.stream().noneMatch(atualizacao -> 
                Objects.equals(atualizacao.getId(), telefone.getId())));
    }
}
