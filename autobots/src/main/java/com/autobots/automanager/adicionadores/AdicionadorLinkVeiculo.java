package com.autobots.automanager.adicionadores;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.entitades.Veiculo;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<Veiculo>{

    public void adicionarLink(List<Veiculo> veiculos) {
        for (Veiculo veiculo : veiculos) {
            adicionarLink(veiculo);
        }
    }

    public void adicionarLink(Veiculo veiculo) {
        long id = veiculo.getId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VeiculoControle.class)
                        .buscarVeiculo(id))
                .withSelfRel();
        veiculo.add(selfLink);

        Link todosLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VeiculoControle.class)
                        .buscarVeiculos())
                .withRel("veiculos");
        veiculo.add(todosLink);
    }
}
