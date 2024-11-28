package com.autobots.automanager.controles;

import com.autobots.automanager.adicionadores.AdicionadorLinkEmpresa;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.servicos.EmpresaServico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaControle {

    @Autowired
    private EmpresaServico empresaServico;

    @Autowired
    private EmpresaSelecionador selecionador;

    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;

    @Autowired
    private RepositorioEmpresa repositorio;
    
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<Empresa>> buscarEmpresas() {
        List<Empresa> empresas = empresaServico.buscarEmpresas();
        if (empresas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(empresas);
            return new ResponseEntity<>(empresas, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> buscarEmpresa(@PathVariable Long id) {
        Empresa empresa = selecionador.selecionar(empresaServico.buscarEmpresas(), id);
        if (empresa == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(empresa);
            return new ResponseEntity<>(empresa, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarEmpresa(@RequestBody Empresa empresa) {
        empresa.setCadastro(new Date());
        empresaServico.salvarEmpresa(empresa);
        return new ResponseEntity<>("Empresa: " + empresa.getNomeFantasia() + " cadastrada com sucesso", HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarEmpresa(@PathVariable Long id) {
        Empresa empresa = selecionador.selecionar(empresaServico.buscarEmpresas(), id);
        if (empresa != null) {
            empresaServico.deletarEmpresa(id);
            return new ResponseEntity<>("Deletado com sucesso", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Empresa não encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarEmpresa(@PathVariable Long id, @RequestBody Empresa empresaAtualizada) {
        Empresa empresaExistente = repositorio.findById(id).orElse(null);
        if (empresaExistente == null) {
            return ResponseEntity.notFound().build();
        }

        if (empresaAtualizada.getNomeFantasia() != null) {
            empresaExistente.setNomeFantasia(empresaAtualizada.getNomeFantasia());
        }
        
        if (empresaAtualizada.getRazaoSocial() != null) {
            empresaExistente.setRazaoSocial(empresaAtualizada.getRazaoSocial());
        }

        if (empresaAtualizada.getEndereco() != null) {
            Endereco enderecoAtualizado = empresaAtualizada.getEndereco();

            if (empresaExistente.getEndereco() == null) {
                empresaExistente.setEndereco(new Endereco());
            }

            empresaExistente.getEndereco().setEstado(enderecoAtualizado.getEstado());
            empresaExistente.getEndereco().setCidade(enderecoAtualizado.getCidade());
            empresaExistente.getEndereco().setBairro(enderecoAtualizado.getBairro());
            empresaExistente.getEndereco().setRua(enderecoAtualizado.getRua());
            empresaExistente.getEndereco().setNumero(enderecoAtualizado.getNumero());
            empresaExistente.getEndereco().setCodigoPostal(enderecoAtualizado.getCodigoPostal());
            empresaExistente.getEndereco().setInformacoesAdicionais(enderecoAtualizado.getInformacoesAdicionais());
        }

        repositorio.save(empresaExistente);

        return ResponseEntity.ok("Empresa atualizada com sucesso");
    }

}
