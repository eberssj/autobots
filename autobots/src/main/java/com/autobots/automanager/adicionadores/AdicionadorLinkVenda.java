package com.autobots.automanager.adicionadores;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.entitades.Venda;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<Venda>{

    @Override
    public void adicionarLink(List<Venda> lista) {
        for (Venda venda : lista) {
            adicionarLink(venda);
        }
    }

    @Override
    public void adicionarLink(Venda venda) {
        long id = venda.getId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaControle.class)
                        .buscarVenda(id))
                .withSelfRel();
        venda.add(selfLink);

        Link vendasLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaControle.class)
                        .buscarVendas())
                .withRel("vendas");
        venda.add(vendasLink);
    }
}
