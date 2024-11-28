package com.autobots.automanager.adicionadores;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entitades.Empresa;

@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<Empresa> {
	
	@Override
    public void adicionarLink(List<Empresa> empresas) {
        for (Empresa empresa : empresas) {
            long id = empresa.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(EmpresaControle.class)
                            .buscarEmpresa(id))
                    .withSelfRel();
            empresa.add(linkProprio);
        }
    }

	@Override
    public void adicionarLink(Empresa empresa) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaControle.class)
                        .buscarEmpresas())
                .withRel("empresas");
        empresa.add(linkProprio);
    }
}