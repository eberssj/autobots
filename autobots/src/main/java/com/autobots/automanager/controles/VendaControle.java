package com.autobots.automanager.controles;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.adicionadores.AdicionadorLinkVenda;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.selecionadores.VendaSelecionador;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VeiculoServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/vendas")
public class VendaControle {

    @Autowired
    private VendaServico servico;

    @Autowired
    private VendaSelecionador selecionador;

    @Autowired
    private UsuarioServico servicoUsuario;

    @Autowired
    private VeiculoServico servicoVeiculo;

    @Autowired
    private EmpresaServico servicoEmpresa;

    @Autowired
    private EmpresaSelecionador selecionadorEmpresa;

    @Autowired
    private AdicionadorLinkVenda adicionadorLinkVenda;

    @GetMapping
    public ResponseEntity<List<Venda>> buscarVendas() {
      List<Venda> vendas = servico.buscarVendas();
      HttpStatus status = HttpStatus.CONFLICT;
      if (vendas.isEmpty()) {
        status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<List<Venda>>(status);
      } else {
        status = HttpStatus.FOUND;
        adicionadorLinkVenda.adicionarLink(vendas);
        ResponseEntity<List<Venda>> resposta = new ResponseEntity<List<Venda>>(vendas, status);
        return resposta;
      }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarVenda(@PathVariable Long id) {
      List<Venda> vendas = servico.buscarVendas();
      Venda select = selecionador.selecionar(vendas, id);
      if (select == null) {
        return new ResponseEntity<Venda>(HttpStatus.NOT_FOUND);
      } else {
    	  adicionadorLinkVenda.adicionarLink(select);
        return new ResponseEntity<Venda>(select, HttpStatus.FOUND);
      }
    }
    
    @PostMapping("/cadastro/{idEmpresa}")
    public ResponseEntity<?> cadastroVenda(
      @RequestBody Venda vendas,
      @PathVariable Long idEmpresa
    ) {
      List<Empresa> selecionarEmpresa = servicoEmpresa.buscarEmpresas();
      Empresa selecionada = selecionadorEmpresa.selecionar(
        selecionarEmpresa,
        idEmpresa
      );
      if (selecionada != null) {
        Usuario clienteSelecionado = servicoUsuario.buscarUsuario(
          vendas.getCliente().getId()
        );
        Usuario funcionarioSelecionado = servicoUsuario.buscarUsuario(
          vendas.getFuncionario().getId()
        );
        Veiculo veiculoSelecionador = servicoVeiculo.buscarVeiculo(
          vendas.getVeiculo().getId()
        );

        vendas.setCadastro(new Date()); 

        Set<Mercadoria> novasMercadorias = new HashSet<>();
        for (Mercadoria bodyMercadoria : vendas.getMercadorias()) {
          Mercadoria novaMercadoria = new Mercadoria();
          novaMercadoria.setDescricao(bodyMercadoria.getDescricao());
          novaMercadoria.setCadastro(bodyMercadoria.getCadastro());

          novaMercadoria.setFabricao(new Date()); 

          novaMercadoria.setNome(bodyMercadoria.getNome());
          novaMercadoria.setQuantidade(bodyMercadoria.getQuantidade());
          novaMercadoria.setValidade(bodyMercadoria.getValidade());
          novaMercadoria.setValor(bodyMercadoria.getValor());
          novasMercadorias.add(novaMercadoria);
        }
        vendas.setMercadorias(novasMercadorias);

        Set<Servico> novosServicos = new HashSet<>();
        for (Servico bodyServico : vendas.getServicos()) {
          Servico novoServico = new Servico();
          novoServico.setDescricao(bodyServico.getDescricao());
          novoServico.setNome(bodyServico.getNome());
          novoServico.setValor(bodyServico.getValor());
          novosServicos.add(novoServico);
        }
        vendas.setServicos(novosServicos);

        funcionarioSelecionado.getVendas().add(vendas);
        vendas.setCliente(clienteSelecionado);
        vendas.setFuncionario(funcionarioSelecionado);
        vendas.setVeiculo(veiculoSelecionador);
        selecionada.getVendas().add(vendas);

        servicoEmpresa.salvarEmpresa(selecionada);

        return new ResponseEntity<>(
          "Serviço cadastrado na empresa: " + selecionada.getNomeFantasia(),
          HttpStatus.CREATED
        );
      } else {
        return new ResponseEntity<>(
          "Empresa não encontrada",
          HttpStatus.NOT_FOUND
        );
      }
    }



    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarVenda(@PathVariable Long id, @RequestBody Venda vendaAtualizada) {
        Venda vendaAtual = servico.buscarVenda(id);
        if (vendaAtual == null) {
            return ResponseEntity.notFound().build();
        }

        if (vendaAtualizada.getCliente() == null || vendaAtualizada.getCliente().getNome() == null ||
            vendaAtualizada.getFuncionario() == null || vendaAtualizada.getFuncionario().getNome() == null) {
            return ResponseEntity.badRequest().body("Cliente e funcionário são obrigatórios e devem ter nome preenchido.");
        }

        vendaAtualizada.setId(id);
        servico.salvarVenda(vendaAtualizada);
        return ResponseEntity.ok("Venda atualizada com sucesso");
    }
    
	@DeleteMapping("/deletar/{idVenda}")
	public ResponseEntity<?> deletarVenda(@PathVariable Long idVenda) {
		List<Empresa> empresas = servicoEmpresa.buscarEmpresas();
		List<Usuario> usuarios = servicoUsuario.buscarUsuarios();
		List<Veiculo> veiculos = servicoVeiculo.buscarVeiculos();
		Venda venda = servico.buscarVenda(idVenda);

		if (venda == null) {
			return new ResponseEntity<>("Venda não encontrada...", HttpStatus.NOT_FOUND);
		} else {

			for (Empresa empresa : servicoEmpresa.buscarEmpresas()){
				if (!empresa.getVendas().isEmpty()) {
					for (Venda vendaEmpresa : empresa.getVendas()) {
						if (vendaEmpresa.getId() == idVenda) {
							for (Empresa empresaRegistrada : empresas) {
								empresaRegistrada.getVendas().remove(vendaEmpresa);
							}
						}
					}
				}
			}

			for (Usuario usuario : servicoUsuario.buscarUsuarios()) {
				if (!usuario.getVendas().isEmpty()) {
					for (Venda vendaUsuario : usuario.getVendas()) {
						if (vendaUsuario.getId() == idVenda) {
							for (Usuario usuarioRegistrado : usuarios) {
								usuarioRegistrado.getVendas().remove(vendaUsuario);
							}
						}
					}
				}
			}

			for (Veiculo veiculo : servicoVeiculo.buscarVeiculos()) {
				if (!veiculo.getVendas().isEmpty()) {
					for (Venda vendaVeiculo : veiculo.getVendas()) {
						if (vendaVeiculo.getId() == idVenda) {
							for (Veiculo veiculoRegistrado : veiculos) {
								veiculoRegistrado.getVendas().remove(vendaVeiculo);
							}
						}
					}
				}
			}

			empresas = servicoEmpresa.buscarEmpresas();
			usuarios = servicoUsuario.buscarUsuarios();
			veiculos = servicoVeiculo.buscarVeiculos();
			servico.deletarVenda(idVenda);
			return new ResponseEntity<>("Venda deletada com sucesso", HttpStatus.ACCEPTED);
		}
	}
}
