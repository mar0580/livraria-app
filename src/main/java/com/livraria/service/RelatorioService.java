package com.livraria.service;

import com.livraria.dto.RelatorioPdfDto;
import com.livraria.repository.projection.RelatorioLivrosPorAutorProjection;

import java.io.InputStream;
import java.util.List;

public interface RelatorioService {
    List<RelatorioLivrosPorAutorProjection> gerarRelatorio();
    
    byte[] gerarRelatorioPDF() throws Exception;
    
    InputStream gerarRelatorioInputStream() throws Exception;
    
    RelatorioPdfDto gerarRelatorioPdfCompleto();
}
