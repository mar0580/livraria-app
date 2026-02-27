package com.livraria.service.impl;

import com.livraria.repository.RelatorioLivrosPorAutorRepository;
import com.livraria.repository.projection.RelatorioLivrosPorAutorProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceImplTest {

    @Mock
    private RelatorioLivrosPorAutorRepository relatorioRepo;

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    @Mock
    private RelatorioLivrosPorAutorProjection projection;

    @InjectMocks
    private RelatorioServiceImpl relatorioService;

    private byte[] pdfMockBytes;

    @BeforeEach
    void setUp() {
        pdfMockBytes = new byte[]{37, 80, 68, 70, 45, 49, 46, 52, 10, 11, 12, 13}; // %PDF-1.4 header + bytes
    }

    @Test
    @DisplayName("Gerar relatório e retorna lista de projections")
    void gerarRelatorio_deveRetornarListaDeProjections() {
        List<RelatorioLivrosPorAutorProjection> projections = Arrays.asList(projection);
        when(relatorioRepo.findAllProjections()).thenReturn(projections);

        List<RelatorioLivrosPorAutorProjection> resultado = relatorioService.gerarRelatorio();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(relatorioRepo).findAllProjections();
    }

    @Test
    @DisplayName("Quando repositório retorna vazio, relatório deve ser lista vazia")
    void gerarRelatorio_quandoListaVazia_deveRetornarListaVazia() {
        when(relatorioRepo.findAllProjections()).thenReturn(Collections.emptyList());

        List<RelatorioLivrosPorAutorProjection> resultado = relatorioService.gerarRelatorio();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(relatorioRepo).findAllProjections();
    }

    @Test
    @DisplayName("Retornar a mesma lista fornecida pelo repositório")
    void gerarRelatorio_deveRetornarMesmaListaDoRepositorio() {
        List<RelatorioLivrosPorAutorProjection> esperado = Arrays.asList(projection, projection);
        when(relatorioRepo.findAllProjections()).thenReturn(esperado);

        List<RelatorioLivrosPorAutorProjection> resultado = relatorioService.gerarRelatorio();

        assertSame(esperado, resultado);
        verify(relatorioRepo, times(1)).findAllProjections();
    }

    @Test
    @DisplayName("Gerar relatório com múltiplas projections")
    void gerarRelatorio_comMultiplasProjections_deveRetornarTodas() {
        RelatorioLivrosPorAutorProjection projection1 = mock(RelatorioLivrosPorAutorProjection.class);
        RelatorioLivrosPorAutorProjection projection2 = mock(RelatorioLivrosPorAutorProjection.class);
        RelatorioLivrosPorAutorProjection projection3 = mock(RelatorioLivrosPorAutorProjection.class);
        List<RelatorioLivrosPorAutorProjection> projections = Arrays.asList(projection1, projection2, projection3);
        when(relatorioRepo.findAllProjections()).thenReturn(projections);

        List<RelatorioLivrosPorAutorProjection> resultado = relatorioService.gerarRelatorio();

        assertEquals(3, resultado.size());
        assertSame(projections, resultado);
    }

    @Test
    @DisplayName("Quando falha ao carregar template, gera exceção no PDF")
    void gerarRelatorioPDF_quandoErroAoCarregarTemplate_deveLancarException() {
        when(resourceLoader.getResource(anyString())).thenThrow(new RuntimeException("Template não encontrado"));

        Exception exception = assertThrows(Exception.class, () -> {
            relatorioService.gerarRelatorioPDF();
        });

        assertTrue(exception.getMessage().contains("Erro ao gerar relatório PDF"));
        assertTrue(exception.getMessage().contains("Template não encontrado"));
        verify(resourceLoader).getResource("classpath:reports/livros_por_autor.jrxml");
    }

    @Test
    @DisplayName("Quando falha ao abrir input stream, gera exceção no PDF")
    void gerarRelatorioPDF_quandoErroAoAbrirInputStream_deveLancarException() throws Exception {
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.getInputStream()).thenThrow(new RuntimeException("Erro ao abrir stream"));

        Exception exception = assertThrows(Exception.class, () -> {
            relatorioService.gerarRelatorioPDF();
        });

        assertTrue(exception.getMessage().contains("Erro ao gerar relatório PDF"));
        verify(resource).getInputStream();
    }

    @Test
    @DisplayName("Relatório InputStream delega para geração de PDF")
    void gerarRelatorioInputStream_deveDelegarParaGerarRelatorioPDF() {
        when(resourceLoader.getResource(anyString())).thenThrow(new RuntimeException("Erro"));

        assertThrows(Exception.class, () -> {
            relatorioService.gerarRelatorioInputStream();
        });
        
        verify(resourceLoader).getResource("classpath:reports/livros_por_autor.jrxml");
    }

    @Test
    @DisplayName("Quando erro na geração completa do PDF, lança RuntimeException")
    void gerarRelatorioPdfCompleto_quandoErroNaGeracao_deveLancarRuntimeException() {
        when(resourceLoader.getResource(anyString())).thenThrow(new RuntimeException("Erro ao carregar template"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            relatorioService.gerarRelatorioPdfCompleto();
        });

        assertTrue(exception.getMessage().contains("Erro ao gerar relatório PDF"));
        assertTrue(exception.getMessage().contains("Erro ao carregar template"));
        verify(resourceLoader).getResource("classpath:reports/livros_por_autor.jrxml");
    }

    @Test
    @DisplayName("Quando exceção genérica ocorre, RuntimeException é lançada com causa")
    void gerarRelatorioPdfCompleto_quandoExceptionGenerica_deveLancarRuntimeException() {
        when(resourceLoader.getResource(anyString())).thenThrow(new RuntimeException("Erro genérico"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            relatorioService.gerarRelatorioPdfCompleto();
        });

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("Erro ao gerar relatório PDF"));
        assertNotNull(exception.getCause());
    }

    @Test
    @DisplayName("Mensagem de erro do PDF completo deve ser detalhada")
    void gerarRelatorioPdfCompleto_mensagemDeErroDeveSerDetalhada() {
        when(resourceLoader.getResource(anyString())).thenThrow(new RuntimeException("Causa raiz do erro"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            relatorioService.gerarRelatorioPdfCompleto();
        });

        assertTrue(exception.getMessage().contains("Erro ao gerar relatório PDF"));
        assertTrue(exception.getMessage().contains("Causa raiz do erro"));
    }

    @Test
    @DisplayName("Geração de relatório deve chamar repositório corretamente")
    void gerarRelatorio_deveChamarRepositorioCorretamente() {
        List<RelatorioLivrosPorAutorProjection> projections = Collections.emptyList();
        when(relatorioRepo.findAllProjections()).thenReturn(projections);

        relatorioService.gerarRelatorio();
        relatorioService.gerarRelatorio();

        verify(relatorioRepo, times(2)).findAllProjections();
    }

    @Test
    @DisplayName("Geração de PDF deve usar ResourceLoader com path correto")
    void gerarRelatorioPDF_deveUsarResourceLoaderComPathCorreto() {
        when(resourceLoader.getResource(anyString())).thenThrow(new RuntimeException("Mock"));

        assertThrows(Exception.class, () -> {
            relatorioService.gerarRelatorioPDF();
        });

        verify(resourceLoader).getResource("classpath:reports/livros_por_autor.jrxml");
    }

    @Test
    @DisplayName("Quando ocorrer erro ao gerar PDF, InputStream deve propagar exceção")
    void gerarRelatorioInputStream_quandoErroAoGerarPDF_devePropagar() throws Exception {
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.getInputStream()).thenThrow(new RuntimeException("Erro no input stream"));

        Exception exception = assertThrows(Exception.class, () -> {
            relatorioService.gerarRelatorioInputStream();
        });

        assertNotNull(exception.getMessage());
        verify(resource).getInputStream();
    }

    @Test
    @DisplayName("Quando causa é Exception, encapsula corretamente no PDF completo")
    void gerarRelatorioPdfCompleto_quandoCausaEhException_deveEncapsularCorretamente() {
        Exception causaOriginal = new Exception("Causa original");
        when(resourceLoader.getResource(anyString())).thenThrow(new RuntimeException(causaOriginal));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            relatorioService.gerarRelatorioPdfCompleto();
        });

        assertNotNull(exception.getCause());
        assertTrue(exception.getMessage().contains("Erro ao gerar relatório PDF"));
    }
}
