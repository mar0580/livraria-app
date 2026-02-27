package com.livraria.controller;

import com.livraria.dto.RelatorioPdfDto;
import com.livraria.service.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioControllerTest {

    @Mock
    private RelatorioService relatorioService;

    @Mock
    private Model model;

    @InjectMocks
    private RelatorioController controller;

    private RelatorioPdfDto relatorioPdfDto;

    @BeforeEach
    void setup() {
        byte[] pdfContent = new byte[] {1, 2, 3, 4, 5};
        relatorioPdfDto = RelatorioPdfDto.builder()
                .conteudo(pdfContent)
                .nomeArquivo("relatorio_livros_por_autor_20240101_120000.pdf")
                .tamanho(pdfContent.length)
                .build();
    }

    @Test
    @DisplayName("Listar registros do relatório e retornar a view")
    void relatorioLivrosPorAutor_deveAdicionarRegistrosERetornarView() {
        when(relatorioService.gerarRelatorio()).thenReturn(Collections.emptyList());

        String view = controller.relatorioLivrosPorAutor(model);

        assertEquals("livro/livros-por-autor", view);
        verify(model).addAttribute("registros", Collections.emptyList());
        verify(relatorioService).gerarRelatorio();
    }

    @Test
    @DisplayName("Propagar exceção quando serviço falhar ao listar relatório")
    void relatorioLivrosPorAutor_quandoServiceLancaExcecao_devePropagar() {
        when(relatorioService.gerarRelatorio()).thenThrow(new IllegalStateException("erro"));

        assertThrows(IllegalStateException.class, () -> controller.relatorioLivrosPorAutor(model));
        verify(relatorioService).gerarRelatorio();
        verifyNoInteractions(model);
    }

    @Test
    @DisplayName("Gerar PDF com headers corretos quando sucesso")
    void relatorioLivrosPorAutorPDF_quandoSucesso_deveRetornarArquivoPdfEHeaders() {
        when(relatorioService.gerarRelatorioPdfCompleto()).thenReturn(relatorioPdfDto);

        ResponseEntity<byte[]> response = controller.relatorioLivrosPorAutorPDF();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(relatorioPdfDto.getConteudo(), response.getBody());
        assertEquals(MediaType.APPLICATION_PDF_VALUE, response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION).contains(relatorioPdfDto.getNomeArquivo()));
        assertEquals(String.valueOf(relatorioPdfDto.getTamanho()), response.getHeaders().getFirst(HttpHeaders.CONTENT_LENGTH));
        assertEquals("no-cache, no-store, must-revalidate", response.getHeaders().getFirst(HttpHeaders.CACHE_CONTROL));
        assertEquals("no-cache", response.getHeaders().getFirst(HttpHeaders.PRAGMA));
        assertEquals("0", response.getHeaders().getFirst(HttpHeaders.EXPIRES));
        verify(relatorioService).gerarRelatorioPdfCompleto();
    }

    @Test
    @DisplayName("Retornar PDF com content-length zero quando conteúdo vazio")
    void relatorioLivrosPorAutorPDF_quandoConteudoVazio_deveRetornarArquivoComContentLengthZero() {
        RelatorioPdfDto vazio = RelatorioPdfDto.builder()
                .conteudo(new byte[0])
                .nomeArquivo("vazio.pdf")
                .tamanho(0)
                .build();

        when(relatorioService.gerarRelatorioPdfCompleto()).thenReturn(vazio);

        ResponseEntity<byte[]> response = controller.relatorioLivrosPorAutorPDF();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length);
        assertEquals("0", response.getHeaders().getFirst(HttpHeaders.CONTENT_LENGTH));
        verify(relatorioService).gerarRelatorioPdfCompleto();
    }

    @Test
    @DisplayName("Retorna erro 500 quando serviço lançar IllegalStateException")
    void relatorioLivrosPorAutorPDF_quandoServiceLancaIllegalStateException_deveRetornarErro500() {
        when(relatorioService.gerarRelatorioPdfCompleto())
                .thenThrow(new IllegalStateException("Erro ao gerar PDF: conteúdo vazio ou nulo"));

        ResponseEntity<byte[]> response = controller.relatorioLivrosPorAutorPDF();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(relatorioService).gerarRelatorioPdfCompleto();
    }

    @Test
    @DisplayName("Retornar erro 500 quando serviço lançar RuntimeException")
    void relatorioLivrosPorAutorPDF_quandoServiceLancaRuntimeException_deveRetornarErro500() {
        when(relatorioService.gerarRelatorioPdfCompleto())
                .thenThrow(new RuntimeException("Erro ao gerar relatório PDF"));

        ResponseEntity<byte[]> response = controller.relatorioLivrosPorAutorPDF();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(relatorioService).gerarRelatorioPdfCompleto();
    }
}