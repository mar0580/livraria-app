package com.livraria.repository.projection;

import java.math.BigDecimal;

public interface RelatorioLivrosPorAutorProjection {
    Integer getAutorId();
    String getAutorNome();
    Integer getLivroId();
    String getLivroTitulo();
    String getLivroEditora();
    Integer getLivroEdicao();
    String getLivroAnoPublicacao();
    BigDecimal getLivroValor();
    String getAssuntos();
    String getTodosAutores(); // Mapeia a nova coluna da View
}