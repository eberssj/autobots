package com.autobots.automanager.servicos;

import com.autobots.automanager.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioEndereco;
import com.autobots.automanager.repositorios.RepositorioTelefone;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpresaServico {

    @Autowired
    private RepositorioEmpresa repositorio;

    @Autowired
    private EmpresaSelecionador selecionador;

    @Autowired
    private RepositorioTelefone repositorioTelefone;

    @Autowired
    private MercadoriaServico servicoMercadoria;

    @Autowired
    private VendaServico servicoVenda;

    @Autowired
    private UsuarioServico servicoUsuario;

    @Autowired
    private VeiculoServico veiculoServico;

    @Autowired
    private ServicoServico servicoServico;

    @Autowired
    private RepositorioEndereco repositorioEndereco;

    public List<Empresa> buscarEmpresas() {
        List<Empresa> empresas = repositorio.findAll();
        return empresas;
    }


    public Empresa buscarEmpresa(Long id) {
        Empresa empresa = repositorio.getById(id);
        return empresa;
    }
    
    public void salvarEmpresa(Empresa empresa) {
        repositorio.save(empresa);
    }

    public Empresa atualizarEmpresa(Empresa empresa) {
        Empresa novaEmpresa = buscarEmpresa(empresa.getId());
        atualizarEmpresaDados(novaEmpresa, empresa);
        for (Telefone telefone : empresa.getTelefones()) {
            telefone.setId(telefone.getId());
            atualizarTelefone(telefone);
        }
        return repositorio.save(novaEmpresa);
    }

    private void atualizarEmpresaDados(Empresa novaEmpresa, Empresa empresa) {
        novaEmpresa.setNomeFantasia(empresa.getNomeFantasia());
        novaEmpresa.setRazaoSocial(empresa.getRazaoSocial());
        novaEmpresa.getEndereco().setBairro(empresa.getEndereco().getBairro());
        novaEmpresa.getEndereco().setNumero(empresa.getEndereco().getNumero());
        novaEmpresa.getEndereco().setRua(empresa.getEndereco().getRua());
        novaEmpresa.getEndereco().setCidade(empresa.getEndereco().getCidade());
        novaEmpresa.getEndereco().setCodigoPostal(empresa.getEndereco().getCodigoPostal());
        novaEmpresa.getEndereco().setInformacoesAdicionais(empresa.getEndereco().getInformacoesAdicionais());
        novaEmpresa.getEndereco().setEstado(empresa.getEndereco().getEstado());
    }

    public void deletarEmpresa(Long id) {
        List<Empresa> empresas = buscarEmpresas();
        Empresa empresa = selecionador.selecionar(empresas, id);
        if (empresa != null) {
            for (Mercadoria mercadoria : empresa.getMercadorias()) {
                servicoMercadoria.deletarMercadoria(id);
            }
            for (Venda venda : empresa.getVendas()) {
                venda.setFuncionario(null);
                venda.setCliente(null);
                venda.setVeiculo(null);
                Set<Servico> servicos = venda.getServicos();
                Set<Mercadoria> mercadorias = venda.getMercadorias();
                venda.getMercadorias().removeAll(mercadorias);
                venda.getServicos().removeAll(servicos);
                servicoVenda.deletarVenda(id);
            }
            for (Servico servico : empresa.getServicos()) {
                servicoServico.deletarServico(id);
            }
            for (Usuario usuario : empresa.getUsuarios()) {
                for (Veiculo veiculo : usuario.getVeiculos()) {
                    veiculo.getVendas().clear();
                    Set<Venda> vendas = veiculo.getVendas();
                    veiculo.setProprietario(null);
                    veiculo.getVendas().removeAll(vendas);
                    usuario.setVeiculos(null);
                    veiculoServico.deletarVeiculo(id);
                }
                servicoUsuario.deletar(usuario.getId());
            }
            repositorio.deleteById(id);
        }
    }

    public void deletarMercadoria(Long idEmpresa, Long idMercadoria) {
        List<Empresa> empresas = buscarEmpresas();
        Empresa empresa = selecionador.selecionar(empresas, idMercadoria);
        Mercadoria mercadoria = servicoMercadoria.buscarMercadoria(idMercadoria);
        if (empresa.getId().equals(idEmpresa)) {
            empresa.getMercadorias().remove(mercadoria);
        }
    }

    public void deletarVenda(Long idEmpresa, Long idVenda) {
        List<Empresa> empresas = buscarEmpresas();
        Empresa empresa = selecionador.selecionar(empresas, idVenda);
        Venda venda = servicoVenda.buscarVenda(idVenda);
        if (empresa.getId().equals(idEmpresa)) {
            empresa.getVendas().remove(venda);
        }
    }

    public Telefone buscarTelefone(Long id) {
        Telefone telefone = repositorioTelefone.getById(id);
        return telefone;
    }

    public Telefone atualizarTelefone(Telefone telefone) {
        Telefone novoTelefone = buscarTelefone(telefone.getId());
        atualizarTelefoneDados(novoTelefone, telefone);
        return repositorioTelefone.save(novoTelefone);
    }

    private void atualizarTelefoneDados(Telefone novoTelefone, Telefone telefone) {
        novoTelefone.setDdd(telefone.getDdd());
        novoTelefone.setNumero(telefone.getNumero());
    }

    public Endereco buscarEndereco(Long id) {
        return repositorioEndereco.getById(id);
    }

    public Endereco atualizarEndereco(Endereco endereco) {
        Endereco novoEndereco = buscarEndereco(endereco.getId());
        atualizarEnderecoDados(novoEndereco, endereco);
        return repositorioEndereco.save(novoEndereco);
    }

    private void atualizarEnderecoDados(Endereco novoEndereco, Endereco endereco) {
        novoEndereco.setNumero(endereco.getNumero());
        novoEndereco.setBairro(endereco.getBairro());
        novoEndereco.setCidade(endereco.getCidade());
        novoEndereco.setCodigoPostal(endereco.getCodigoPostal());
        novoEndereco.setInformacoesAdicionais(endereco.getInformacoesAdicionais());
        novoEndereco.setEstado(endereco.getEstado());
        novoEndereco.setRua(endereco.getRua());
    }
}
