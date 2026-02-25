package com.livraria.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
// mapeia diretamente a view
@Table(name = "vw_relatorio_livros_por_autor")
@Getter @Setter
public class RelatorioLivrosPorAutor {

    @Id
    @Column(name = "livro_id") // ou outra coluna única da view
    private Integer livroId;

    @Column(name = "autor_id")
    private Integer autorId;

    @Column(name = "autor_nome")
    private String autorNome;

    @Column(name = "livro_titulo")
    private String livroTitulo;

    @Column(name = "livro_editora")
    private String livroEditora;

    @Column(name = "livro_edicao")
    private Integer livroEdicao;

    @Column(name = "livro_ano_publicacao")
    private String livroAnoPublicacao;

    @Column(name = "livro_valor")
    private BigDecimal livroValor;

    @Column(name = "assuntos")
    private String assuntos;
}
