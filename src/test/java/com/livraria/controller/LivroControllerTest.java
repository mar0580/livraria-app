package com.livraria.controller;

import com.livraria.entity.Assunto;
import com.livraria.entity.Autor;
import com.livraria.entity.Livro;
import com.livraria.service.AssuntoService;
import com.livraria.service.AutorService;
import com.livraria.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroControllerTest {

    @Mock
    private LivroService livroService;

    @Mock
    private AutorService autorService;

    @Mock
    private AssuntoService assuntoService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private LivroController livroController;

    private Livro livro;
    private Autor autor;
    private Assunto assunto;
    private List<Autor> autores;
    private List<Assunto> assuntos;

    @BeforeEach
    void setUp() {
        autor = Autor.builder()
                .id(1)
                .nome("Autor Teste")
                .build();

        assunto = Assunto.builder()
                .id(1)
                .descricao("Assunto Teste")
                .build();

        livro = Livro.builder()
                .id(1)
                .titulo("Livro Teste")
                .editora("Editora Teste")
                .edicao(1)
                .anoPublicacao("2024")
                .valor(new BigDecimal("50.00"))
                .autores(new HashSet<>(Arrays.asList(autor)))
                .assuntos(new HashSet<>(Arrays.asList(assunto)))
                .build();

        autores = Arrays.asList(autor);
        assuntos = Arrays.asList(assunto);
    }

    @Test
    @DisplayName("Deve listar livros e retornar a view de lista")
    void listar_deveRetornarListaDeLivros() {
        List<Livro> livrosEsperados = Arrays.asList(livro);
        when(livroService.listarTodos()).thenReturn(livrosEsperados);

        String viewName = livroController.listar(model);

        assertEquals("livro/lista", viewName);
        verify(livroService).listarTodos();
        verify(model).addAttribute("livros", livrosEsperados);
    }

    @Test
    @DisplayName("Deve carregar formulário com autores e assuntos")
    void novo_deveCarregarFormularioComDados() {
        when(autorService.listarTodos()).thenReturn(autores);
        when(assuntoService.listarTodos()).thenReturn(assuntos);

        String viewName = livroController.novo(model);

        assertEquals("livro/form", viewName);
        verify(model).addAttribute(eq("livro"), any(Livro.class));
        verify(model).addAttribute("autores", autores);
        verify(model).addAttribute("assuntos", assuntos);
        verify(autorService).listarTodos();
        verify(assuntoService).listarTodos();
    }

    @Test
    @DisplayName("Deve salvar com dados válidos e redireciona")
    void salvar_comDadosValidos_deveRedirecionarParaLista() {
        Set<Integer> autoresIds = new HashSet<>(Arrays.asList(1));
        Set<Integer> assuntosIds = new HashSet<>(Arrays.asList(1));
        when(bindingResult.hasErrors()).thenReturn(false);
        when(livroService.salvar(any(Livro.class), anySet(), anySet())).thenReturn(livro);

        String viewName = livroController.salvar(livro, bindingResult, autoresIds, assuntosIds, model);

        assertEquals("redirect:/livros", viewName);
        verify(bindingResult).hasErrors();
        verify(livroService).salvar(livro, autoresIds, assuntosIds);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    @DisplayName("Com erros de validação, retornar formulário")
    void salvar_comErrosDeValidacao_deveRetornarFormulario() {
        Set<Integer> autoresIds = new HashSet<>(Arrays.asList(1));
        Set<Integer> assuntosIds = new HashSet<>(Arrays.asList(1));
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getErrorCount()).thenReturn(2);
        when(autorService.listarTodos()).thenReturn(autores);
        when(assuntoService.listarTodos()).thenReturn(assuntos);

        String viewName = livroController.salvar(livro, bindingResult, autoresIds, assuntosIds, model);

        assertEquals("livro/form", viewName);
        verify(bindingResult).hasErrors();
        verify(livroService, never()).salvar(any(), anySet(), anySet());
        verify(model).addAttribute("autores", autores);
        verify(model).addAttribute("assuntos", assuntos);
    }

    @Test
    @DisplayName("Quando autores e assuntos são nulos, retorna formulário com mensagem")
    void salvar_comAutoresEAssuntosNull_deveRetornarFormularioComMensagem() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(livroService.salvar(any(Livro.class), isNull(), isNull()))
                .thenThrow(new IllegalArgumentException("Selecione ao menos um autor e um assunto."));
        when(autorService.listarTodos()).thenReturn(autores);
        when(assuntoService.listarTodos()).thenReturn(assuntos);

        String viewName = livroController.salvar(livro, bindingResult, null, null, model);

        assertEquals("livro/form", viewName);
        verify(model).addAttribute("errorMessage", "Selecione ao menos um autor e um assunto.");
        verify(model).addAttribute("autores", autores);
        verify(model).addAttribute("assuntos", assuntos);
        verify(livroService).salvar(livro, null, null);
    }

    @Test
    @DisplayName("Quando assuntos são nulos, retorna formulário com mensagem")
    void salvar_comAssuntosNull_deveRetornarFormularioComMensagem() {
        Set<Integer> autoresIds = new HashSet<>(Arrays.asList(1));
        when(bindingResult.hasErrors()).thenReturn(false);
        when(livroService.salvar(any(Livro.class), anySet(), isNull()))
                .thenThrow(new IllegalArgumentException("Selecione ao menos um assunto."));
        when(autorService.listarTodos()).thenReturn(autores);
        when(assuntoService.listarTodos()).thenReturn(assuntos);

        String viewName = livroController.salvar(livro, bindingResult, autoresIds, null, model);

        assertEquals("livro/form", viewName);
        verify(model).addAttribute("errorMessage", "Selecione ao menos um assunto.");
        verify(model).addAttribute("autores", autores);
        verify(model).addAttribute("assuntos", assuntos);
        verify(livroService).salvar(livro, autoresIds, null);
    }

    @Test
    @DisplayName("Editar livro existente e carregar formulário")
    void editar_comIdValido_deveCarregarFormularioComLivro() {
        when(livroService.buscarPorId(1)).thenReturn(livro);
        when(autorService.listarTodos()).thenReturn(autores);
        when(assuntoService.listarTodos()).thenReturn(assuntos);

        String viewName = livroController.editar(1, model);

        assertEquals("livro/form", viewName);
        verify(livroService).buscarPorId(1);
        verify(model).addAttribute("livro", livro);
        verify(model).addAttribute("autores", autores);
        verify(model).addAttribute("assuntos", assuntos);
    }

    @Test
    @DisplayName("Ao editar livro inexistente, lançar exceção")
    void editar_comIdInvalido_deveLancarExcecao() {
        when(livroService.buscarPorId(999)).thenThrow(new IllegalArgumentException("Livro não encontrado com ID: 999"));

        assertThrows(IllegalArgumentException.class, () -> {
            livroController.editar(999, model);
        });

        verify(livroService).buscarPorId(999);
    }

    @Test
    @DisplayName("Excluir livro e redirecionar para lista")
    void excluir_comIdValido_deveExcluirERedirecionarParaLista() {
        doNothing().when(livroService).excluir(1);

        String viewName = livroController.excluir(1);

        assertEquals("redirect:/livros", viewName);
        verify(livroService).excluir(1);
    }

    @Test
    @DisplayName("Ao excluir livro inexistente, lança exceção")
    void excluir_comIdInvalido_deveLancarExcecao() {
        doThrow(new IllegalArgumentException("Livro não encontrado com ID: 999"))
                .when(livroService).excluir(999);

        assertThrows(IllegalArgumentException.class, () -> {
            livroController.excluir(999);
        });

        verify(livroService).excluir(999);
    }

    @Test
    @DisplayName("Quando autores e assuntos estão vazios, retorna formulário com mensagem")
    void salvar_comConjuntosVazios_deveRetornarFormularioComMensagem() {
        Set<Integer> autoresIds = new HashSet<>();
        Set<Integer> assuntosIds = new HashSet<>();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(livroService.salvar(any(Livro.class), anySet(), anySet()))
                .thenThrow(new IllegalArgumentException("Selecione ao menos um autor e um assunto."));
        when(autorService.listarTodos()).thenReturn(autores);
        when(assuntoService.listarTodos()).thenReturn(assuntos);

        String viewName = livroController.salvar(livro, bindingResult, autoresIds, assuntosIds, model);

        assertEquals("livro/form", viewName);
        verify(model).addAttribute("errorMessage", "Selecione ao menos um autor e um assunto.");
        verify(model).addAttribute("autores", autores);
        verify(model).addAttribute("assuntos", assuntos);
        verify(livroService).salvar(livro, autoresIds, assuntosIds);
    }
}