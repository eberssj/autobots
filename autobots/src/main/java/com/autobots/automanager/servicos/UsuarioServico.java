package com.autobots.automanager.servicos;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioCredencialUsuarioSenha;
import com.autobots.automanager.repositorios.RepositorioDocumento;
import com.autobots.automanager.repositorios.RepositorioEmail;
import com.autobots.automanager.repositorios.RepositorioTelefone;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UsuarioServico {

    @Autowired
    private RepositorioUsuario repositorio;

    @Autowired
    private RepositorioEmail repositorioEmail;

    @Autowired
    private RepositorioDocumento repositorioDocumento;

    @Autowired
    private RepositorioCredencialUsuarioSenha repositorioCredencialUsuarioSenha;

    @Autowired
    private RepositorioTelefone repositorioTelefone;

    @Autowired
    private MercadoriaServico servicoMercadoria;

    @Autowired
    private VendaServico servicoVenda;

    @Autowired
    private VeiculoServico servicoVeiculo;

    @Autowired
    private UsuarioSelecionador usuarioSelecionador;

    public List<Usuario> buscarUsuarios() {
        return repositorio.findAll();
    }

    public Usuario buscarUsuario(Long id) {
        return repositorio.findById(id).orElse(null);
    }

    public void salvarUsuario(Usuario usuario) {
        repositorio.save(usuario);
    }

    public Usuario atualizarUsuario(Usuario obj) {
        Usuario novo = buscarUsuario(obj.getId());
        if (novo != null) {
            atualizarUsuarioDados(novo, obj);
            for (Documento docs : obj.getDocumentos()) {
                atualizarDocumento(docs);
            }
            for (Email emails : obj.getEmails()) {
                atualizarEmail(emails);
            }
            for (Telefone telefones : obj.getTelefones()) {
                atualizarTelefone(telefones);
            }
            return repositorio.save(novo);
        }
        return null;
    }

    private void atualizarUsuarioDados(Usuario novo, Usuario obj) {
        novo.setNome(obj.getNome());
        novo.setNomeSocial(obj.getNomeSocial());
        novo.getEndereco().setBairro(obj.getEndereco().getBairro());
        novo.getEndereco().setNumero(obj.getEndereco().getNumero());
        novo.getEndereco().setRua(obj.getEndereco().getRua());
        novo.getEndereco().setCidade(obj.getEndereco().getCidade());
        novo.getEndereco().setCodigoPostal(obj.getEndereco().getCodigoPostal());
        novo.getEndereco().setInformacoesAdicionais(obj.getEndereco().getInformacoesAdicionais());
        novo.getEndereco().setEstado(obj.getEndereco().getEstado());
    }

    public void deletar(Long id) {
        repositorio.deleteById(id);
    }

    
    
    public List<CredencialUsuarioSenha> buscarCredenciais() {
        return repositorioCredencialUsuarioSenha.findAll();
    }

    
    
    public List<Documento> buscarDocumentos() {
        return repositorioDocumento.findAll();
    }

    public Documento buscarDocumento(Long id) {
        return repositorioDocumento.findById(id).orElse(null);
    }

    public void salvarDocumento(Documento documento) {
        repositorioDocumento.save(documento);
    }

    public Documento atualizarDocumento(Documento obj) {
        Documento novo = buscarDocumento(obj.getId());
        if (novo != null) {
            atualizarDocumentoDados(novo, obj);
            return repositorioDocumento.save(novo);
        }
        return null;
    }

    private void atualizarDocumentoDados(Documento novo, Documento obj) {
    	novo.setTipo(obj.getTipo());
        novo.setNumero(obj.getNumero());
        novo.setDataEmissao(new Date());
    }
    
    public void deletarDocumento(Long idCliente, Long idDocumento) {
        Usuario usuario = buscarUsuario(idCliente);
        if (usuario != null) {
            Documento documento = buscarDocumento(idDocumento);
            if (documento != null) {
                usuario.getDocumentos().remove(documento);
                repositorio.save(usuario);
                repositorioDocumento.delete(documento);
            }
        }
    }

   
    
    public List<Email> buscarEmails() {
        return repositorioEmail.findAll();
    }

    public Email buscarEmail(Long id) {
        return repositorioEmail.findById(id).orElse(null);
    }

    public void salvarEmail(Email email) {
        repositorioEmail.save(email);
    }

    public Email atualizarEmail(Email obj) {
        Email novo = buscarEmail(obj.getId());
        if (novo != null) {
            atualizarEmailDados(novo, obj);
            return repositorioEmail.save(novo);
        }
        return null;
    }

    private void atualizarEmailDados(Email novo, Email obj) {
        novo.setEndereco(obj.getEndereco());
    }

    
    
    public List<Telefone> buscarTelefones() {
        return repositorioTelefone.findAll();
    }

    public Telefone buscarTelefone(Long id) {
        return repositorioTelefone.findById(id).orElse(null);
    }

    public Telefone atualizarTelefone(Telefone obj) {
        Telefone novo = buscarTelefone(obj.getId());
        if (novo != null) {
            atualizarTelefoneDados(novo, obj);
            return repositorioTelefone.save(novo);
        }
        return null;
    }

    private void atualizarTelefoneDados(Telefone novo, Telefone obj) {
        novo.setDdd(obj.getDdd());
        novo.setNumero(obj.getNumero());
    }

    
    
    public void deletarMercadoria(Long idCliente, Long idMercadoria) {
        Usuario selecionado = usuarioSelecionador.selecionar(buscarUsuarios(), idCliente);
        Mercadoria mercadoria = servicoMercadoria.buscarMercadoria(idMercadoria);
        if (selecionado != null && mercadoria != null) {
            selecionado.getMercadorias().remove(mercadoria);
            repositorio.save(selecionado);
        }
    }

  
    
    public void deletarVenda(Long idCliente, Long idVenda) {
        Usuario selecionado = usuarioSelecionador.selecionar(buscarUsuarios(), idCliente);
        Venda venda = servicoVenda.buscarVenda(idVenda);
        if (selecionado != null && venda != null) {
            selecionado.getVendas().remove(venda);
            repositorio.save(selecionado);
        }
    }

 
    
    public void deletarVeiculo(Long idCliente, Long idVeiculo) {
        Usuario selecionado = usuarioSelecionador.selecionar(buscarUsuarios(), idCliente);
        Veiculo veiculo = servicoVeiculo.buscarVeiculo(idVeiculo);
        if (selecionado != null && veiculo != null) {
            selecionado.getVeiculos().remove(veiculo);
            repositorio.save(selecionado);
        }
    }
}
