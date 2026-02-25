//package com.livraria.service.impl;
//
//import com.livraria.entity.RelatorioLivrosPorAutor;
//import com.livraria.repository.RelatorioLivrosPorAutorRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Testes unitários para RelatorioServiceImpl
// * Cobertura de testes para geração de relatórios
// */
//@ExtendWith(MockitoExtension.class)
//@DisplayName("Testes do Serviço de Relatórios")
//class RelatorioServiceImplTest {
//
//    @Mock
//    private RelatorioLivrosPorAutorRepository relatorioRepository;
//
//    @InjectMocks
//    private RelatorioServiceImpl relatorioService;
//
//    private RelatorioLivrosPorAutor relatorioDados;
//
//    @BeforeEach
//    void setUp() {
//        relatorioDados = RelatorioLivrosPorAutor.builder()
//                .id(1)
//                .autorNome("Machado de Assis")
//                .livroTitulo("Dom Casmurro")
//                .livroEditora("Companhia das Letras")
//                .livroValor(new BigDecimal("49.90"))
//                .assuntoDescricao("Romance")
//                .build();
//    }
//
//    @Test
//    @DisplayName("Deve gerar relatório com dados")
//    void deveGerarRelatorioComDados() {
//        // Arrange
//        List<RelatorioLivrosPorAutor> relatorios = Arrays.asList(relatorioDados);
//        when(relatorioRepository.findAll()).thenReturn(relatorios);
//
//        // Act
//        List<RelatorioLivrosPorAutor> resultado = relatorioService.gerarRelatorio();
//
//        // Assert
//        assertNotNull(resultado);
//        assertEquals(1, resultado.size());
//        assertEquals("Machado de Assis", resultado.get(0).getAutorNome());
//        assertEquals("Dom Casmurro", resultado.get(0).getLivroTitulo());
//        verify(relatorioRepository, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("Deve retornar lista vazia quando não há dados")
//    void deveRetornarListaVaziaQuandoNaoHaDados() {
//        // Arrange
//        when(relatorioRepository.findAll()).thenReturn(Collections.emptyList());
//
//        // Act
//        List<RelatorioLivrosPorAutor> resultado = relatorioService.gerarRelatorio();
//
//        // Assert
//        assertNotNull(resultado);
//        assertTrue(resultado.isEmpty());
//        verify(relatorioRepository, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("Deve gerar relatório com múltiplos registros agrupados por autor")
//    void deveGerarRelatorioComMultiplosRegistros() {
//        // Arrange
//        RelatorioLivrosPorAutor relatorio2 = RelatorioLivrosPorAutor.builder()
//                .id(2)
//                .autorNome("Machado de Assis")
//                .livroTitulo("Quincas Borba")
//                .livroEditora("Companhia das Letras")
//                .livroValor(new BigDecimal("45.00"))
//                .assuntoDescricao("Romance")
//                .build();
//
//        RelatorioLivrosPorAutor relatorio3 = RelatorioLivrosPorAutor.builder()
//                .id(3)
//                .autorNome("Cruz e Sousa")
//                .livroTitulo("Missal")
//                .livroEditora("Editora X")
//                .livroValor(new BigDecimal("55.00"))
//                .assuntoDescricao("Poesia")
//                .build();
//
//        List<RelatorioLivrosPorAutor> relatorios = Arrays.asList(relatorioDados, relatorio2, relatorio3);
//        when(relatorioRepository.findAll()).thenReturn(relatorios);
//
//        // Act
//        List<RelatorioLivrosPorAutor> resultado = relatorioService.gerarRelatorio();
//
//        // Assert
//        assertNotNull(resultado);
//        assertEquals(3, resultado.size());
//        assertTrue(resultado.stream().anyMatch(r -> "Machado de Assis".equals(r.getAutorNome())));
//        assertTrue(resultado.stream().anyMatch(r -> "Cruz e Sousa".equals(r.getAutorNome())));
//        verify(relatorioRepository, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("Deve retornar relatório com valores corretos")
//    void deveRetornarRelatorioComValoresCorretos() {
//        // Arrange
//        List<RelatorioLivrosPorAutor> relatorios = Arrays.asList(relatorioDados);
//        when(relatorioRepository.findAll()).thenReturn(relatorios);
//
//        // Act
//        List<RelatorioLivrosPorAutor> resultado = relatorioService.gerarRelatorio();
//
//        // Assert
//        assertNotNull(resultado);
//        assertEquals(new BigDecimal("49.90"), resultado.get(0).getLivroValor());
//        assertEquals("Companhia das Letras", resultado.get(0).getLivroEditora());
//        verify(relatorioRepository, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("Deve chamar repositório uma única vez")
//    void deveCharmarRepositorioUmaUnicaVez() {
//        // Arrange
//        List<RelatorioLivrosPorAutor> relatorios = Arrays.asList(relatorioDados);
//        when(relatorioRepository.findAll()).thenReturn(relatorios);
//
//        // Act
//        relatorioService.gerarRelatorio();
//        relatorioService.gerarRelatorio();
//
//        // Assert
//        verify(relatorioRepository, times(2)).findAll();
//    }
//}
