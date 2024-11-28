package com.autobots.automanager.adicionadores;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.entitades.Mercadoria;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria>{

    public void adicionarLink(List<Mercadoria> mercadorias) {
        for (Mercadoria mercadoria : mercadorias) {
            long id = mercadoria.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(MercadoriaControle.class)
                            .buscarMercadoria(id))
                    .withSelfRel();
            mercadoria.add(linkProprio);
        }
    }

    public void adicionarLink(Mercadoria mercadoria) {
        long id = mercadoria.getId();

        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaControle.class)
                        .buscarMercadorias())
                .withRel("mercadorias");
        mercadoria.add(linkProprio);
    }
}
