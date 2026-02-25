//package com.livraria.controller;
//
//import com.livraria.entity.Assunto;
//import com.livraria.entity.Autor;
//import com.livraria.entity.Livro;
//import com.livraria.repository.AssuntoRepository;
//import com.livraria.repository.AutorRepository;
//import com.livraria.service.LivroService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//
//import java.math.BigDecimal;
//import java.util.*;
//
//import static org.hamcrest.Matchers.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Testes para LivroController
// * Testa endpoints HTTP para operações de livros
// */
//@WebMvcTest(LivroController.class)
//@DisplayName("Testes do Controller de Livros")
//class LivroControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private LivroService livroService;
//
//    @MockBean
//    private AutorRepository autorRepository;
//
//    @MockBean
//    private AssuntoRepository assuntoRepository;
//
//    private Livro livroTeste;
//    private Autor autorTeste;
//    private Assunto assuntoTeste;
//
//    @BeforeEach
//    void setUp() {
//        autorTeste = Autor.builder()
//                .id(1)
//                .nome("Machado de Assis")
//                .build();
//
//        assuntoTeste = Assunto.builder()
//                .id(1)
//                .descricao("Romance")
//                .build();
//
//        livroTeste = Livro.builder()
//                .id(1)
//                .titulo("Dom Casmurro")
//                .editora("Companhia das Letras")
//                .edicao(1)
//                .anoPublicacao("1899")
//                .valor(new BigDecimal("49.90"))
//                .autores(new HashSet<>(Set.of(autorTeste)))
//                .assuntos(new HashSet<>(Set.of(assuntoTeste)))
//                .build();
//    }
//
//    @Test
//    @DisplayName("GET /livros - Deve retornar lista de livros")
//    void deveRetornarListaDeLivros() throws Exception {
//        // Arrange
//        List<Livro> livros = Arrays.asList(livroTeste);
//        when(livroService.listarTodos()).thenReturn(livros);
//
//        // Act & Assert
//        mockMvc.perform(get("/livros"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("livro/lista"))
//                .andExpect(model().attributeExists("livros"))
//                .andExpect(model().attribute("livros", hasSize(1)));
//
//        verify(livroService, times(1)).listarTodos();
//    }
//
//    @Test
//    @DisplayName("GET /livros - Deve retornar lista vazia quando não há livros")
//    void deveRetornarListaVaziaQuandoNaoHaLivros() throws Exception {
//        // Arrange
//        when(livroService.listarTodos()).thenReturn(Collections.emptyList());
//
//        // Act & Assert
//        mockMvc.perform(get("/livros"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("livro/lista"))
//                .andExpect(model().attribute("livros", hasSize(0)));
//
//        verify(livroService, times(1)).listarTodos();
//    }
//
//    @Test
//    @DisplayName("GET /livros/novo - Deve exibir formulário de novo livro")
//    void deveExibirFormularioDeNovoLivro() throws Exception {
//        // Arrange
//        when(autorRepository.findAll()).thenReturn(Arrays.asList(autorTeste));
//        when(assuntoRepository.findAll()).thenReturn(Arrays.asList(assuntoTeste));
//
//        // Act & Assert
//        mockMvc.perform(get("/livros/novo"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("livro/form"))
//                .andExpect(model().attributeExists("livro"))
//                .andExpect(model().attributeExists("autores"))
//                .andExpect(model().attributeExists("assuntos"));
//
//        verify(autorRepository, times(1)).findAll();
//        verify(assuntoRepository, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("POST /livros - Deve salvar novo livro com sucesso")
//    void deveSalvarNovoLivroComSucesso() throws Exception {
//        // Arrange
//        when(livroService.salvar(any(Livro.class), anySet(), anySet()))
//                .thenReturn(livroTeste);
//
//        // Act & Assert
//        mockMvc.perform(post("/livros")
//                .param("titulo", "Dom Casmurro")
//                .param("editora", "Companhia das Letras")
//                .param("edicao", "1")
//                .param("anoPublicacao", "1899")
//                .param("valor", "49.90")
//                .param("autoresSelecionados", "1")
//                .param("assuntosSelecionados", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/livros"));
//
//        verify(livroService, times(1)).salvar(any(Livro.class), anySet(), anySet());
//    }
//
//    @Test
//    @DisplayName("POST /livros - Deve rejeitar livro com título vazio")
//    void deveRejeitarLivroComTituloVazio() throws Exception {
//        // Act & Assert
//        mockMvc.perform(post("/livros")
//                .param("titulo", "")
//                .param("editora", "Companhia das Letras")
//                .param("edicao", "1")
//                .param("anoPublicacao", "1899")
//                .param("valor", "49.90"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("livro/form"));
//
//        verify(livroService, never()).salvar(any(Livro.class), anySet(), anySet());
//    }
//
//    @Test
//    @DisplayName("POST /livros - Deve rejeitar livro com valor inválido")
//    void deveRejeitarLivroComValorInvalido() throws Exception {
//        // Act & Assert
//        mockMvc.perform(post("/livros")
//                .param("titulo", "Dom Casmurro")
//                .param("editora", "Companhia das Letras")
//                .param("edicao", "1")
//                .param("anoPublicacao", "1899")
//                .param("valor", "0.00"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("livro/form"));
//
//        verify(livroService, never()).salvar(any(Livro.class), anySet(), anySet());
//    }
//
//    @Test
//    @DisplayName("GET /livros/{id}/editar - Deve exibir formulário de edição")
//    void deveExibirFormularioDeEdicao() throws Exception {
//        // Arrange
//        when(livroService.buscarPorId(1)).thenReturn(livroTeste);
//        when(autorRepository.findAll()).thenReturn(Arrays.asList(autorTeste));
//        when(assuntoRepository.findAll()).thenReturn(Arrays.asList(assuntoTeste));
//
//        // Act & Assert
//        mockMvc.perform(get("/livros/1/editar"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("livro/form"))
//                .andExpect(model().attributeExists("livro"))
//                .andExpect(model().attributeExists("autores"))
//                .andExpect(model().attributeExists("assuntos"));
//
//        verify(livroService, times(1)).buscarPorId(1);
//    }
//
//    @Test
//    @DisplayName("POST /livros/{id} - Deve atualizar livro existente")
//    void deveAtualizarLivroExistente() throws Exception {
//        // Arrange
//        when(livroService.salvar(any(Livro.class), anySet(), anySet()))
//                .thenReturn(livroTeste);
//
//        // Act & Assert
//        mockMvc.perform(post("/livros/1")
//                .param("id", "1")
//                .param("titulo", "Dom Casmurro - Edição Revisada")
//                .param("editora", "Companhia das Letras")
//                .param("edicao", "2")
//                .param("anoPublicacao", "1899")
//                .param("valor", "59.90")
//                .param("autoresSelecionados", "1")
//                .param("assuntosSelecionados", "1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/livros"));
//
//        verify(livroService, times(1)).salvar(any(Livro.class), anySet(), anySet());
//    }
//
//    @Test
//    @DisplayName("GET /livros/{id}/deletar - Deve deletar livro")
//    void deveDeletarLivro() throws Exception {
//        // Arrange
//        doNothing().when(livroService).excluir(1);
//
//        // Act & Assert
//        mockMvc.perform(get("/livros/1/deletar"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/livros"));
//
//        verify(livroService, times(1)).excluir(1);
//    }
//
//    @Test
//    @DisplayName("POST /livros - Deve salvar livro com múltiplos autores")
//    void deveSalvarLivroComMultiplosAutores() throws Exception {
//        // Arrange
//        when(livroService.salvar(any(Livro.class), anySet(), anySet()))
//                .thenReturn(livroTeste);
//
//        // Act & Assert
//        mockMvc.perform(post("/livros")
//                .param("titulo", "Obra Colaborativa")
//                .param("editora", "Editora Teste")
//                .param("edicao", "1")
//                .param("anoPublicacao", "1890")
//                .param("valor", "60.00")
//                .param("autoresSelecionados", "1", "2")
//                .param("assuntosSelecionados", "1"))
//                .andExpect(status().is3xxRedirection());
//
//        verify(livroService, times(1)).salvar(any(Livro.class), anySet(), anySet());
//    }
//
//    @Test
//    @DisplayName("POST /livros - Deve rejeitar ano de publicação inválido")
//    void deveRejeitarAnoDePublicacaoInvalido() throws Exception {
//        // Act & Assert
//        mockMvc.perform(post("/livros")
//                .param("titulo", "Dom Casmurro")
//                .param("editora", "Companhia das Letras")
//                .param("edicao", "1")
//                .param("anoPublicacao", "18999")  // Inválido: 5 dígitos
//                .param("valor", "49.90"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("livro/form"));
//
//        verify(livroService, never()).salvar(any(Livro.class), anySet(), anySet());
//    }
//}
