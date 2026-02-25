package com.livraria.controller;

import com.livraria.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RelatorioController {

    @Autowired
    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/relatorios/livros-por-autor")
    public String relatorioLivrosPorAutor(Model model) {
        model.addAttribute("registros", relatorioService.gerarRelatorio());
        return "livro/livros-por-autor";
    }
}

/*
Você pode falar algo assim (em termos simples):

    “Para o relatório, criei uma view no PostgreSQL que já retorna os dados agregados de livro, autor e assunto.”
    “No backend, usei uma projection (RelatorioLivrosPorAutorProjection) para mapear as colunas da view sem criar uma entidade de verdade, porque relatório não é um agregado de domínio, é só leitura.”
    “Criei um repositório específico (RelatorioLivrosPorAutorRepository) com uma query nativa em cima da view. Assim, não precisei de nenhuma entidade fake, o código fica mais limpo e alinhado com o uso read-only do relatório.”

 */