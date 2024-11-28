package com.autobots.automanager.entitades;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@JsonDeserialize(as = CredencialUsuarioSenha.class)
public class CredencialUsuarioSenha extends Credencial {

    @Column(nullable = false, unique = true)
    private String nomeUsuario;

    @Column(nullable = false)
    private String senha;

    // Construtores, getters e setters
    public CredencialUsuarioSenha() {
    }

    public CredencialUsuarioSenha(String nomeUsuario, String senha) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
