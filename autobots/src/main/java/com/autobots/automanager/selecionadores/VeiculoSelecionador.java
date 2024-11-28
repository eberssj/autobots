package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Veiculo;

@Component
public class VeiculoSelecionador implements Selecionador<Veiculo, Long>{

	@Override
	public Veiculo selecionar(List<Veiculo> veiculos, Long id) {
		for (Veiculo veiculo : veiculos) {
			if (veiculo.getId().equals(id)) {
				return veiculo;
			}
		}
		return null;
	}
}
