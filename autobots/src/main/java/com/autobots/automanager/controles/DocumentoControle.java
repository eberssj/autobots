package com.autobots.automanager.controles;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.servicos.UsuarioServico;

@RestController
@RequestMapping("/documentos")
public class DocumentoControle {

    @Autowired
    private UsuarioServico usuarioServico;

    @Autowired
    private UsuarioSelecionador selecionador;

    @GetMapping
    public ResponseEntity<List<Documento>> buscarDocumentos() {
        List<Documento> documentos = usuarioServico.buscarDocumentos();
        if (documentos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(documentos, HttpStatus.OK);
        }
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Set<Documento>> buscarDocumentos(@PathVariable Long usuarioId) {
        List<Usuario> buscarUsuarios = usuarioServico.buscarUsuarios();
        Usuario selecionado = selecionador.selecionar(buscarUsuarios, usuarioId);
        if (selecionado != null) {
            Set<Documento> documentosUsuario = selecionado.getDocumentos();
            return new ResponseEntity<>(documentosUsuario, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{usuarioId}/atualizar/{idDoc}")
    public ResponseEntity<String> atualizarDocumento(
            @PathVariable Long usuarioId,
            @PathVariable Long idDoc,
            @RequestBody Documento atualizar) {
        List<Usuario> buscarUsuarios = usuarioServico.buscarUsuarios();
        Usuario selecionado = selecionador.selecionar(buscarUsuarios, usuarioId);
        if (selecionado != null) {
            atualizar.setId(idDoc);
            usuarioServico.atualizarDocumento(atualizar);
            return new ResponseEntity<>("Atualizado com sucesso", HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletar/{idCliente}/{idDocumento}")
    public ResponseEntity<String> deletarDocumento(@PathVariable Long idCliente, @PathVariable Long idDocumento) {
        Documento documento = usuarioServico.buscarDocumento(idDocumento);
        if (documento == null) {
            return new ResponseEntity<>("Documento não encontrado", HttpStatus.NOT_FOUND);
        } else {
            usuarioServico.deletarDocumento(idCliente, idDocumento);
            return new ResponseEntity<>("Documento excluído com sucesso", HttpStatus.ACCEPTED);
        }
    }
}
