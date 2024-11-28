package com.autobots.automanager.adaptadores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.autobots.automanager.enumeracoes.Perfil;

public class UserDetailsImpl implements UserDetails {
private static final long serialVersionUID = 1L;
	
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> autenticacao;
	
	public UserDetailsImpl(String email, String senha, List<Perfil> roles) {
	    this.email = email;
	    this.password = senha;
	    this.gerarAutoridades(roles);
	  }
	
	private void gerarAutoridades(List<Perfil> perfis) {
		List<SimpleGrantedAuthority> autoridadesPerfis = new ArrayList<>();
		for (Perfil perfil : perfis) {
			autoridadesPerfis.add(new SimpleGrantedAuthority(perfil.name()));
		}
		this.autenticacao = autoridadesPerfis;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.autenticacao;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}		
}