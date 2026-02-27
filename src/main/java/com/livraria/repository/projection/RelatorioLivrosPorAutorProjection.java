package com.livraria.repository.projection;

public interface RelatorioLivrosPorAutorProjection {
    Integer getAutorId();
    String getAutorNome();
    String getLivros();
    String getEditoras();
    String getAnosPublicacao();
    String getValores();
    String getAssuntos();
    String getTodosAutores();
}