package com.autobots.automanager.servicos;

import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.selecionadores.VeiculoSelecionador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeiculoServico {

    @Autowired
    private RepositorioVeiculo repositorio;

    @Autowired
    private VeiculoSelecionador selecionador;

    @Autowired
    private VendaServico servicoVenda;

    public List<Veiculo> buscarVeiculos() {
        return repositorio.findAll();
    }

    public Veiculo buscarVeiculo(Long id) {
        return repositorio.getById(id);
    }
    
    public void salvarVeiculo(Veiculo veiculo) {
        repositorio.save(veiculo);
    }

    public Veiculo atualizarVeiculo(Veiculo veiculo) {
        Veiculo veiculoAtual = buscarVeiculo(veiculo.getId());
        if (veiculoAtual != null) {
            atualizarVeiculoDados(veiculoAtual, veiculo);
            return repositorio.save(veiculoAtual);
        }
        return null;
    }

    private void atualizarVeiculoDados(Veiculo veiculoAtual, Veiculo veiculo) {
        veiculoAtual.setModelo(veiculo.getModelo());
        veiculoAtual.setPlaca(veiculo.getPlaca());
        veiculoAtual.setTipo(veiculo.getTipo());
    }

    public void deletarVeiculo(Long id) {
        repositorio.deleteById(id);
    }

    public void deletarVenda(Long idVeiculo, Long idVenda) {
        Veiculo veiculo = buscarVeiculo(idVeiculo);
        Venda venda = servicoVenda.buscarVenda(idVenda);
        if (veiculo != null && venda != null && veiculo.getVendas().contains(venda)) {
            veiculo.getVendas().remove(venda);
            repositorio.save(veiculo);
        }
    }
}
