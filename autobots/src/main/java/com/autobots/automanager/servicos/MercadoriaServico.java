package com.autobots.automanager.servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@Service
public class MercadoriaServico {

    @Autowired
    private RepositorioMercadoria repositorio;

    public void salvarMercadoria(Mercadoria mercadoria) {
        repositorio.save(mercadoria);
    }

    public List<Mercadoria> buscarMercadorias() {
        return repositorio.findAll();
    }

    public Mercadoria buscarMercadoria(Long id) {
        Optional<Mercadoria> mercadoria = repositorio.findById(id);
        return mercadoria.orElse(null);
    }

    public Mercadoria atualizarMercadoria(Mercadoria mercadoria) {
        Mercadoria mercadoriaAtual = buscarMercadoria(mercadoria.getId());
        if (mercadoriaAtual != null) {
            atualizarDados(mercadoriaAtual, mercadoria);
            return repositorio.save(mercadoriaAtual);
        }
        return null;
    }

    public void deletarMercadoria(Long id) {
        repositorio.deleteById(id);
    }

    private void atualizarDados(Mercadoria mercadoriaExistente, Mercadoria mercadoria) {
        mercadoriaExistente.setNome(mercadoria.getNome());
        mercadoriaExistente.setDescricao(mercadoria.getDescricao());
        mercadoriaExistente.setCadastro(mercadoria.getCadastro());
        mercadoriaExistente.setFabricao(mercadoria.getFabricao());
        mercadoriaExistente.setQuantidade(mercadoria.getQuantidade());
        mercadoriaExistente.setValidade(mercadoria.getValidade());
        mercadoriaExistente.setValor(mercadoria.getValor());
    }
}
