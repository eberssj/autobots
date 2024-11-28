package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Empresa;


@Component
public class EmpresaSelecionador implements Selecionador<Empresa, Long> {
	
	@Override
	public Empresa selecionar(List<Empresa> empresas, Long id) {
		for (Empresa empresa : empresas) {
			if (empresa.getId().equals(id)) {
				return empresa;
			}
		}
		return null;
	}
}