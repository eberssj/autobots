package com.autobots.automanager.servicos;


import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.selecionadores.VendaSelecionador;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendaServico {

    @Autowired
    private RepositorioVenda repositorio;

    @Autowired
    private VendaSelecionador vendaSelecionador;

    @Autowired
    private MercadoriaServico servicoMercadoria;

    public List<Venda> buscarVendas() {
        return repositorio.findAll();
    }

    public void salvarVenda(Venda venda) {
        repositorio.save(venda);
    }

    public Venda buscarVenda(Long id) {
        return repositorio.getById(id);
    }

    public Venda atualizarVenda(Venda venda) {
        Venda vendaAtual = buscarVenda(venda.getId());
        if (vendaAtual != null) {
            atualizarVendaDados(vendaAtual, venda);
            return repositorio.save(vendaAtual);
        }
        return null;
    }

    private void atualizarVendaDados(Venda vendaAtual, Venda venda) {
        vendaAtual.setIdentificacao(venda.getIdentificacao());
    }

    public void deletarVenda(Long id) {
        repositorio.deleteById(id);
    }

    public void removerMercadoria(Long idVenda, Long idMercadoria) {
        Venda venda = buscarVenda(idVenda);
        Mercadoria mercadoria = servicoMercadoria.buscarMercadoria(idMercadoria);
        if (venda != null && mercadoria != null) {
            venda.getMercadorias().remove(mercadoria);
            repositorio.save(venda);
        }
    }
}
