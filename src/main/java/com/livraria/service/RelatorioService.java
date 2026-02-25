package com.livraria.service;

import com.livraria.entity.RelatorioLivrosPorAutor;

import java.util.List;

public interface RelatorioService {
    List<RelatorioLivrosPorAutor> gerarRelatorio();
}
