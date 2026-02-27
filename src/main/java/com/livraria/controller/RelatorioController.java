package com.livraria.controller;

import com.livraria.dto.RelatorioPdfDto;
import com.livraria.service.RelatorioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RelatorioController {

    private static final Logger logger = LoggerFactory.getLogger(RelatorioController.class);

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/relatorios/livros-por-autor")
    public String relatorioLivrosPorAutor(Model model) {
        model.addAttribute("registros", relatorioService.gerarRelatorio());
        return "livro/livros-por-autor";
    }

    @GetMapping("/relatorios/livros-por-autor/pdf")
    public ResponseEntity<byte[]> relatorioLivrosPorAutorPDF() {
        try {
            RelatorioPdfDto relatorio = relatorioService.gerarRelatorioPdfCompleto();
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + relatorio.getNomeArquivo() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .contentLength(relatorio.getTamanho())
                    .body(relatorio.getConteudo());
        } catch (Exception e) {
            logger.error("Erro ao processar download de PDF: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}