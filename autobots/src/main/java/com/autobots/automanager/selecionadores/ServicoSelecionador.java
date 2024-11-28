package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Servico;

@Component
public class ServicoSelecionador implements Selecionador<Servico, Long> {
	
	@Override
	public Servico selecionar(List<Servico> servicos, Long id) {
		for (Servico servico : servicos) {
			if (servico.getId().equals(id)) {
				return servico;
			}
		}
		return null;
	}
}
