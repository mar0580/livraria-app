package com.livraria.service;

import com.livraria.entity.RelatorioLivrosPorAutor;

import java.io.InputStream;
import java.util.List;

public interface RelatorioService {
    List<RelatorioLivrosPorAutor> gerarRelatorio();
    
    byte[] gerarRelatorioPDF() throws Exception;
    
    InputStream gerarRelatorioInputStream() throws Exception;
}
