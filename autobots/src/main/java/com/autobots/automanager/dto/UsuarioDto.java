package com.autobots.automanager.dto;


import java.util.List;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.enumeracoes.PerfilUsuario;

import lombok.Data;

@Data
public class UsuarioDto {

	String razaoSocial;
	Usuario usuario;
	Endereco endereco;
	List<Telefone> telefones;
	List<Documento> documentos;
	List<Email> emails;
	CredencialUsuarioSenha credencial;
	PerfilUsuario perfilUsuario;
	List<Mercadoria> mercadorias;
	
}