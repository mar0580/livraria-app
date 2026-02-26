package com.livraria.controller;

import com.livraria.service.RelatorioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class RelatorioController {

    private static final Logger logger = LoggerFactory.getLogger(RelatorioController.class);

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

    @GetMapping("/relatorios/livros-por-autor/pdf")
    public ResponseEntity<byte[]> relatorioLivrosPorAutorPDF() {
        try {
            logger.info("Iniciando requisição de download de PDF");
            
            byte[] pdfBytes = relatorioService.gerarRelatorioPDF();
            
            if (pdfBytes == null || pdfBytes.length == 0) {
                logger.error("PDF gerado com tamanho zero ou nulo");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "relatorio_livros_por_autor_" + timestamp + ".pdf";
            
            logger.info("PDF gerado com sucesso. Tamanho: {} bytes. Filename: {}", pdfBytes.length, filename);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
        } catch (Exception e) {
            logger.error("Erro ao gerar PDF: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

/*
Você pode falar algo assim (em termos simples):

    “Para o relatório, criei uma view no PostgreSQL que já retorna os dados agregados de livro, autor e assunto.”
    “No backend, usei uma projection (RelatorioLivrosPorAutorProjection) para mapear as colunas da view sem criar uma entidade de verdade, porque relatório não é um agregado de domínio, é só leitura.”
    “Criei um repositório específico (RelatorioLivrosPorAutorRepository) com uma query nativa em cima da view. Assim, não precisei de nenhuma entidade fake, o código fica mais limpo e alinhado com o uso read-only do relatório.”

 */