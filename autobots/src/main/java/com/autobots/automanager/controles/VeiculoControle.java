package com.autobots.automanager.controles;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.adicionadores.AdicionadorLinkVeiculo;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.selecionadores.VeiculoSelecionador;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VeiculoServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/veiculos")
public class VeiculoControle {
    
    @Autowired
    private VeiculoServico veiculoServico;
    
    @Autowired
    private VeiculoSelecionador selecionador;
    
    @Autowired
    private UsuarioSelecionador usuarioSelecionador;
    
    @Autowired
    private UsuarioServico servicoUsuario;

    @Autowired
    private VendaServico servicoVenda;
    
    @Autowired
    private AdicionadorLinkVeiculo adicionadorLinkVeiculo;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Veiculo>> buscarVeiculos() {
        List<Veiculo> todos = veiculoServico.buscarVeiculos();
        if (todos.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
        	adicionadorLinkVeiculo.adicionarLink(todos);
            return ResponseEntity.ok(todos);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<Veiculo> buscarVeiculo(@PathVariable Long id){
		List<Veiculo> veiculos = veiculoServico.buscarVeiculos();
		Veiculo select = selecionador.selecionar(veiculos, id);
		if(select == null) {
			return new ResponseEntity<Veiculo>(HttpStatus.NOT_FOUND);
		}else {
			adicionadorLinkVeiculo.adicionarLink(select);
			return new ResponseEntity<Veiculo>(select, HttpStatus.FOUND);
		}
	}
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/cadastro/{idUsuario}")
    public ResponseEntity<?> cadastroVeiculo(@PathVariable Long idUsuario, @RequestBody Veiculo body) {
        Usuario usuario = usuarioSelecionador.selecionar(servicoUsuario.buscarUsuarios(), idUsuario);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        body.setProprietario(usuario);
        veiculoServico.salvarVeiculo(body);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("Veiculo cadastrado para o usuário: " + usuario.getNome());
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarVeiculo(@PathVariable Long id, @RequestBody Veiculo atualizador) {
        Veiculo veiculo = veiculoServico.buscarVeiculo(id);
        if (veiculo == null) {
            return ResponseEntity.notFound().build();
        }
        
        atualizador.setId(id);
        veiculoServico.atualizarVeiculo(atualizador);
        
        return ResponseEntity.ok("Veiculo atualizado com sucesso");
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarVeiculo(@PathVariable Long id) {
        Veiculo veiculo = veiculoServico.buscarVeiculo(id);
        if (veiculo == null) {
            return ResponseEntity.notFound().build();
        }

        Usuario proprietario = veiculo.getProprietario();
        if (proprietario != null) {
            proprietario.getVeiculos().remove(veiculo);
            servicoUsuario.salvarUsuario(proprietario);
        }

        List<Venda> vendas = servicoVenda.buscarVendas(); 
        for (Venda venda : vendas) {
            if (venda.getVeiculo() != null && venda.getVeiculo().getId().equals(id)) {
                venda.setVeiculo(null);
                servicoVenda.salvarVenda(venda);
            }
        }
        
        veiculoServico.deletarVeiculo(id);
        
        return ResponseEntity.ok("Veiculo deletado com sucesso");
    }

}
