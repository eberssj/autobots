package com.autobots.automanager.controles;

import com.autobots.automanager.adicionadores.AdicionadorLinkServico;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.selecionadores.ServicoSelecionador;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.ServicoServico;
import com.autobots.automanager.servicos.VendaServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoControle {

    @Autowired
    private ServicoServico servicoServico;
    
    @Autowired
    private ServicoSelecionador servicoSelecionador;

    @Autowired
    private EmpresaServico empresaServico;

    @Autowired
    private VendaServico vendaServico;

    @Autowired
    private AdicionadorLinkServico adicionadorLinkServico;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @GetMapping
    public ResponseEntity<List<Servico>> buscarServicos() {
        List<Servico> todos = servicoServico.buscarServicos();
        if (todos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        adicionadorLinkServico.adicionarLink(todos);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarServico(@PathVariable Long id) {
      List<Servico> servicos = servicoServico.buscarServicos();
      Servico select = servicoSelecionador.selecionar(servicos, id);
      if (select == null) {
        return new ResponseEntity<Servico>(HttpStatus.NOT_FOUND);
      } else {
    	adicionadorLinkServico.adicionarLink(select);
        return new ResponseEntity<Servico>(select, HttpStatus.FOUND);
      }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @PostMapping("/cadastro/{idEmpresa}")
    public ResponseEntity<String> cadastrarServico(@RequestBody Servico novoServico, @PathVariable Long idEmpresa) {
        Empresa empresa = empresaServico.buscarEmpresa(idEmpresa);
        if (empresa == null) {
            return new ResponseEntity<>("Empresa não encontrada", HttpStatus.NOT_FOUND);
        }

        servicoServico.salvarServico(novoServico);
        empresa.getServicos().add(novoServico);
        empresaServico.salvarEmpresa(empresa);

        return new ResponseEntity<>("Serviço cadastrado com sucesso na empresa " + empresa.getNomeFantasia(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarServico(@PathVariable Long id, @RequestBody Servico servicoAtualizado) {
        Servico servicoAtual = servicoServico.buscarServico(id);
        if (servicoAtual == null) {
            return new ResponseEntity<>("Serviço não encontrado", HttpStatus.NOT_FOUND);
        }

        servicoAtualizado.setId(id);
        servicoServico.atualizarServico(servicoAtualizado);

        return new ResponseEntity<>("Serviço atualizado com sucesso", HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarServico(@PathVariable Long id) {
        Servico servico = servicoServico.buscarServico(id);
        if (servico == null) {
            return new ResponseEntity<>("Serviço não encontrado", HttpStatus.NOT_FOUND);
        }

        List<Empresa> empresas = empresaServico.buscarEmpresas();
        List<Venda> vendas = vendaServico.buscarVendas();

        for (Empresa empresa : empresas) {
            if (empresa.getServicos().removeIf(s -> s.getId().equals(id))) {
                empresaServico.salvarEmpresa(empresa);
            }
        }

        for (Venda venda : vendas) {
            if (venda.getServicos().removeIf(s -> s.getId().equals(id))) {
                vendaServico.salvarVenda(venda);
            }
        }

        servicoServico.deletarServico(id);
        return new ResponseEntity<>("Serviço deletado com sucesso", HttpStatus.OK);
    }
}
