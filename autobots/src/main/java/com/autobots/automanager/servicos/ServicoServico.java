package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoServico {

    @Autowired
    private RepositorioServico repositorio;

    public List<Servico> buscarServicos() {
        return repositorio.findAll();
    }

    public Servico buscarServico(Long id) {
        return repositorio.getById(id);
    }
    
    public void salvarServico(Servico servico) {
        repositorio.save(servico);
    }

    public Servico atualizarServico(Servico servico) {
        Servico servicoAtual = buscarServico(servico.getId());
        if (servicoAtual != null) {
            atualizarServicoDados(servicoAtual, servico);
            return repositorio.save(servicoAtual);
        }
        return null;
    }

    private void atualizarServicoDados(Servico servicoAtual, Servico servico) {
        servicoAtual.setDescricao(servico.getDescricao());
        servicoAtual.setNome(servico.getNome());
        servicoAtual.setValor(servico.getValor());
    }

    public void deletarServico(Long id) {
        repositorio.deleteById(id);
    }
}
