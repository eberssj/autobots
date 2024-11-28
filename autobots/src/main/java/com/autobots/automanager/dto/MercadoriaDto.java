package com.autobots.automanager.dto;

import java.io.Serializable;
import java.util.Date;

import com.autobots.automanager.entitades.Mercadoria;

import lombok.Data;

@Data
public class MercadoriaDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Date validade;
    private Date fabricacao;
    private Date cadastro;
    private String nome;
    private Long quantidade;
    private double valor;
    private String descricao;
    
    public MercadoriaDto() {
        
    }
    
    public MercadoriaDto(Mercadoria mercadoria) {
        this.id = mercadoria.getId();
        this.validade = mercadoria.getValidade();
        this.fabricacao = mercadoria.getFabricao();
        this.cadastro = mercadoria.getCadastro();
        this.nome = mercadoria.getNome();
        this.quantidade = mercadoria.getQuantidade();
        this.valor = mercadoria.getValor();
        this.descricao = mercadoria.getDescricao();
    }
}
