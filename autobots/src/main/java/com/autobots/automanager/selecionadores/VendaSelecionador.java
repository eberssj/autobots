package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Venda;


@Component
public class VendaSelecionador implements Selecionador<Venda, Long> {
	
	@Override
	public Venda selecionar(List<Venda> vendas, Long id) {
		for (Venda venda: vendas) {
			if (venda.getId().equals(id)) {
				return venda;
			}
		}
		return null;
	}
}