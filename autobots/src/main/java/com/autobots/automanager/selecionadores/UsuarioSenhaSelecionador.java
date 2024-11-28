package com.autobots.automanager.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;



@Component
public class UsuarioSenhaSelecionador  implements Selecionador<CredencialUsuarioSenha, String> {

	@Override
	public CredencialUsuarioSenha selecionar(List<CredencialUsuarioSenha> entidade, String id) {
		CredencialUsuarioSenha selecionado = null;
		for(CredencialUsuarioSenha credencial : entidade) {
			if(credencial.getNomeUsuario().equals(id)) {
				selecionado = credencial;
			}
		}
		return selecionado;
	}
}
