package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Mercadoria;

@Component
public class MercadoriaSelecionador implements Selecionador<Mercadoria, Long> {
	  
		@Override
		public Mercadoria selecionar(List<Mercadoria> mercadorias, Long id) {
			for (Mercadoria mercadoria : mercadorias) {
				if (mercadoria.getId().equals(id)) {
					return mercadoria;
				}
			}
			return null;
		}
	}