package com.autobots.automanager.adicionadores;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.entitades.Servico;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico>{

    public void adicionarLink(List<Servico> servicos) {
        for (Servico servico : servicos) {
            adicionarLink(servico);
        }
    }

    public void adicionarLink(Servico servico) {
        long id = servico.getId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoControle.class)
                        .buscarServico(id))
                .withSelfRel();
        servico.add(selfLink);

        Link todosLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoControle.class)
                        .buscarServicos())
                .withRel("servicos");
        servico.add(todosLink);
    }
}
