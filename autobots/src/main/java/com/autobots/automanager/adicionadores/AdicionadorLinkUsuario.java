package com.autobots.automanager.adicionadores;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entitades.Usuario;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario>{

    public void adicionarLink(List<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            adicionarLink(usuario);
        }
    }

    public void adicionarLink(Usuario usuario) {
        long id = usuario.getId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .buscarUsuario(id))
                .withSelfRel();
        usuario.add(selfLink);

        Link todosLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .buscarUsuarios())
                .withRel("usuarios");
        usuario.add(todosLink);
    }
}