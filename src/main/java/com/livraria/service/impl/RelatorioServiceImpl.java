package com.livraria.service.impl;

import com.livraria.entity.RelatorioLivrosPorAutor;
import com.livraria.repository.RelatorioLivrosPorAutorRepository;
import com.livraria.service.RelatorioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioServiceImpl implements RelatorioService {

    private final RelatorioLivrosPorAutorRepository relatorioRepo;

    public RelatorioServiceImpl(RelatorioLivrosPorAutorRepository relatorioRepo) {
        this.relatorioRepo = relatorioRepo;
    }

    @Override
    public List<RelatorioLivrosPorAutor> gerarRelatorio() {
        return relatorioRepo.findAll();
    }
}
