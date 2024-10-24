package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired
	private TelefoneRepositorio telefoneRepositorio;
	
	@GetMapping
	public List<EntityModel<Telefone>> listarTelefones() {
		List<Telefone> telefones = telefoneRepositorio.findAll();
		return telefones.stream()
				.map(telefone -> {
					EntityModel<Telefone> model = EntityModel.of(telefone);
					Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).buscarTelefone(telefone.getId())).withSelfRel();
					model.add(selfLink);
					return model;
				})
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public EntityModel<Telefone> buscarTelefone(@PathVariable Long id) {
		Telefone telefone = telefoneRepositorio.findById(id)
				.orElseThrow(() -> new RuntimeException("Telefone não encontrado com id: " + id));

		EntityModel<Telefone> model = EntityModel.of(telefone);
		Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).buscarTelefone(id)).withSelfRel();
		model.add(selfLink);
		return model;
	}
	
	@PostMapping("/cadastrar/{idCliente}")
	public void cadastrarTelefone(@PathVariable Long idCliente, @RequestBody Telefone telefone) {
	    Cliente cliente = clienteRepositorio.findById(idCliente)
	            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + idCliente));

	    cliente.getTelefones().add(telefone);
	    clienteRepositorio.save(cliente);
	}
	
	@PutMapping("/atualizar/{id}")
	public void atualizarTelefone(@PathVariable Long id, @RequestBody Telefone atualizacao) {
		Telefone telefone = telefoneRepositorio.findById(id)
				.orElseThrow(() -> new RuntimeException("Telefone não encontrado com id: " + id));

		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefone, atualizacao);

		telefoneRepositorio.save(telefone);
	}
	
	@DeleteMapping("/deletar/{id}")
	public void deletarTelefone(@PathVariable Long id) {
		Telefone telefone = telefoneRepositorio.findById(id)
				.orElseThrow(() -> new RuntimeException("Telefone não encontrado com id: " + id));

		List<Cliente> clientes = clienteRepositorio.findAll();
		for (Cliente cliente : clientes) {
			cliente.getTelefones().removeIf(t -> t.getId().equals(id));
			clienteRepositorio.save(cliente);
		}

		telefoneRepositorio.delete(telefone);
	}
}
