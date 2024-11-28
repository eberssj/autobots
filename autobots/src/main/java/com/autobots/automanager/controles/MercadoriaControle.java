package com.autobots.automanager.controles;

import com.autobots.automanager.adicionadores.AdicionadorLinkMercadoria;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.selecionadores.MercadoriaSelecionador;
import com.autobots.automanager.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.MercadoriaServico;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VendaServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaControle {

    @Autowired
    private MercadoriaServico mercadoriaServico;

    @Autowired
    private EmpresaServico empresaServico;

    @Autowired
    private UsuarioServico usuarioServico;

    @Autowired
    private VendaServico vendaServico;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorLinkMercadoria;


    @GetMapping
    public ResponseEntity<List<Mercadoria>> buscarMercadorias() {
        List<Mercadoria> mercadorias = mercadoriaServico.buscarMercadorias();
        if (mercadorias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        adicionadorLinkMercadoria.adicionarLink(mercadorias);
        return new ResponseEntity<>(mercadorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mercadoria> buscarMercadoria(@PathVariable Long id) {
        Mercadoria mercadoria = mercadoriaServico.buscarMercadoria(id);
        if (mercadoria == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        adicionadorLinkMercadoria.adicionarLink(mercadoria);
        return new ResponseEntity<>(mercadoria, HttpStatus.OK);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarMercadoria(@PathVariable Long id, @RequestBody Mercadoria mercadoriaAtualizada) {
        Mercadoria mercadoriaAtual = mercadoriaServico.buscarMercadoria(id);
        if (mercadoriaAtual == null) {
            return new ResponseEntity<>("Mercadoria não encontrada", HttpStatus.NOT_FOUND);
        }
        mercadoriaAtualizada.setId(id);
        mercadoriaServico.atualizarMercadoria(mercadoriaAtualizada);
        return new ResponseEntity<>("Mercadoria atualizada com sucesso", HttpStatus.OK);
    }

    @PostMapping("/cadastro/{idCliente}")
    public ResponseEntity<String> cadastrarMercadoria(@RequestBody Mercadoria novaMercadoria, @PathVariable Long idCliente) {
        Usuario cliente = usuarioServico.buscarUsuario(idCliente);
        if (cliente == null) {
            return new ResponseEntity<>("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        mercadoriaServico.salvarMercadoria(novaMercadoria);

        List<Empresa> empresasParaAtualizar = new ArrayList<>();
        for (Empresa empresa : empresaServico.buscarEmpresas()) {
            for (Usuario usuario : empresa.getUsuarios()) {
                if (usuario.getId().equals(cliente.getId())) {
                    empresasParaAtualizar.add(empresa);
                    break;
                }
            }
        }

        for (Empresa empresa : empresasParaAtualizar) {
            empresa.getMercadorias().add(novaMercadoria);
            empresaServico.salvarEmpresa(empresa);
        }

        cliente.getMercadorias().add(novaMercadoria);
        usuarioServico.salvarUsuario(cliente);

        return new ResponseEntity<>("Mercadoria cadastrada com sucesso", HttpStatus.CREATED);
    }


    @DeleteMapping("/deletar/{idMercadoria}")
    public ResponseEntity<String> deletarMercadoria(@PathVariable Long idMercadoria) {
        Mercadoria mercadoria = mercadoriaServico.buscarMercadoria(idMercadoria);
        if (mercadoria == null) {
            return new ResponseEntity<>("Mercadoria não encontrada", HttpStatus.NOT_FOUND);
        }

        for (Usuario usuario : usuarioServico.buscarUsuarios()) {
            if (usuario.getMercadorias().removeIf(m -> m.getId().equals(idMercadoria))) {
                usuarioServico.salvarUsuario(usuario);
            }
        }

        for (Empresa empresa : empresaServico.buscarEmpresas()) {
            if (empresa.getMercadorias().removeIf(m -> m.getId().equals(idMercadoria))) {
                empresaServico.salvarEmpresa(empresa);
            }
        }

        for (Venda venda : vendaServico.buscarVendas()) {
            if (venda.getMercadorias().removeIf(m -> m.getId().equals(idMercadoria))) {
                vendaServico.salvarVenda(venda);
            }
        }

        mercadoriaServico.deletarMercadoria(idMercadoria);
        return new ResponseEntity<>("Mercadoria deletada com sucesso", HttpStatus.OK);
    }
}
