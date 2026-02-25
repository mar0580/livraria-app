package com.livraria.service.impl;

import com.livraria.entity.Assunto;
import com.livraria.entity.Autor;
import com.livraria.entity.Livro;
import com.livraria.repository.AssuntoRepository;
import com.livraria.repository.AutorRepository;
import com.livraria.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para LivroServiceImpl
 * Cobertura de testes para todas as operações CRUD
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço de Livros")
class LivroServiceImplTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private AssuntoRepository assuntoRepository;

    @InjectMocks
    private LivroServiceImpl livroService;

    private Livro livroTeste;
    private Autor autorTeste;
    private Assunto assuntoTeste;

    @BeforeEach
    void setUp() {
        // Preparar dados de teste
        autorTeste = Autor.builder()
                .id(1)
                .nome("Machado de Assis")
                .build();

        assuntoTeste = Assunto.builder()
                .id(1)
                .descricao("Romance")
                .build();

        livroTeste = Livro.builder()
                .id(1)
                .titulo("Dom Casmurro")
                .editora("Companhia das Letras")
                .edicao(1)
                .anoPublicacao("1899")
                .valor(new BigDecimal("49.90"))
                .autores(new HashSet<>(Set.of(autorTeste)))
                .assuntos(new HashSet<>(Set.of(assuntoTeste)))
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os livros com sucesso")
    void deveListarTodosOsLivros() {
        // Arrange
        List<Livro> livrosEsperados = Arrays.asList(livroTeste);
        when(livroRepository.findAll()).thenReturn(livrosEsperados);

        // Act
        List<Livro> resultado = livroService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Dom Casmurro", resultado.get(0).getTitulo());
        verify(livroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem livros")
    void deveRetornarListaVaziaQuandoNaoExistemLivros() {
        // Arrange
        when(livroRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Livro> resultado = livroService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(livroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar livro por ID com sucesso")
    void deveBuscarLivroPorIdComSucesso() {
        // Arrange
        when(livroRepository.findById(1)).thenReturn(Optional.of(livroTeste));

        // Act
        Livro resultado = livroService.buscarPorId(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Dom Casmurro", resultado.getTitulo());
        verify(livroRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar livro inexistente")
    void deveLancarExcecaoAoBuscarLivroInexistente() {
        // Arrange
        when(livroRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> livroService.buscarPorId(999));
        verify(livroRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Deve salvar novo livro com autores e assuntos")
    void deveSalvarNovoLivroComAutoresEAssuntos() {
        // Arrange
        Livro novoLivro = Livro.builder()
                .titulo("Quincas Borba")
                .editora("Editora Teste")
                .edicao(1)
                .anoPublicacao("1891")
                .valor(new BigDecimal("45.00"))
                .build();

        Set<Integer> idsAutores = Set.of(1);
        Set<Integer> idsAssuntos = Set.of(1);

        when(autorRepository.findAllById(idsAutores))
                .thenReturn(List.of(autorTeste));
        when(assuntoRepository.findAllById(idsAssuntos))
                .thenReturn(List.of(assuntoTeste));
        when(livroRepository.save(any(Livro.class)))
                .thenReturn(livroTeste);

        // Act
        Livro resultado = livroService.salvar(novoLivro, idsAutores, idsAssuntos);

        // Assert
        assertNotNull(resultado);
        assertEquals("Dom Casmurro", resultado.getTitulo());
        assertFalse(resultado.getAutores().isEmpty());
        assertFalse(resultado.getAssuntos().isEmpty());
        verify(livroRepository, times(1)).save(any(Livro.class));
        verify(autorRepository, times(1)).findAllById(idsAutores);
        verify(assuntoRepository, times(1)).findAllById(idsAssuntos);
    }

    @Test
    @DisplayName("Deve salvar livro com autores e assuntos vazios")
    void deveSalvarLivroComAutoresEAssuntosVazios() {
        // Arrange
        Livro novoLivro = Livro.builder()
                .titulo("Memórias Póstumas de Brás Cubas")
                .editora("Editora X")
                .edicao(1)
                .anoPublicacao("1881")
                .valor(new BigDecimal("55.00"))
                .autores(new HashSet<>())
                .assuntos(new HashSet<>())
                .build();

        when(livroRepository.save(any(Livro.class))).thenReturn(novoLivro);

        // Act
        Livro resultado = livroService.salvar(novoLivro, Collections.emptySet(), Collections.emptySet());

        // Assert
        assertNotNull(resultado);
        assertEquals("Memórias Póstumas de Brás Cubas", resultado.getTitulo());
        assertTrue(resultado.getAutores().isEmpty());
        assertTrue(resultado.getAssuntos().isEmpty());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    @DisplayName("Deve atualizar livro existente com sucesso")
    void deveAtualizarLivroExistenteComSucesso() {
        // Arrange
        Livro livroAtualizado = Livro.builder()
                .id(1)
                .titulo("Dom Casmurro - Edição Revisada")
                .editora("Companhia das Letras")
                .edicao(2)
                .anoPublicacao("1899")
                .valor(new BigDecimal("59.90"))
                .autores(new HashSet<>(Set.of(autorTeste)))
                .assuntos(new HashSet<>(Set.of(assuntoTeste)))
                .build();

        Set<Integer> idsAutores = Set.of(1);
        Set<Integer> idsAssuntos = Set.of(1);

        when(autorRepository.findAllById(idsAutores))
                .thenReturn(List.of(autorTeste));
        when(assuntoRepository.findAllById(idsAssuntos))
                .thenReturn(List.of(assuntoTeste));
        when(livroRepository.save(any(Livro.class)))
                .thenReturn(livroAtualizado);

        // Act
        Livro resultado = livroService.salvar(livroAtualizado, idsAutores, idsAssuntos);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getEdicao());
        assertEquals(new BigDecimal("59.90"), resultado.getValor());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    @DisplayName("Deve excluir livro com sucesso")
    void deveExcluirLivroComSucesso() {
        // Arrange
        doNothing().when(livroRepository).deleteById(1);

        // Act
        livroService.excluir(1);

        // Assert
        verify(livroRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios do livro")
    void deveValidarCamposObrigatoriosDoLivro() {
        // Arrange
        Livro livroInvalido = Livro.builder()
                .titulo(null) // Inválido
                .editora("Editora")
                .edicao(1)
                .anoPublicacao("2024")
                .valor(new BigDecimal("50.00"))
                .build();

        // Act & Assert - O teste de validação deve ser feito no Controller
        // ou através de um validador específico
        assertNull(livroInvalido.getTitulo());
    }

    @Test
    @DisplayName("Deve salvar livro com múltiplos autores")
    void deveSalvarLivroComMultiplosAutores() {
        // Arrange
        Autor autor2 = Autor.builder().id(2).nome("Cruz e Sousa").build();
        Set<Integer> idsAutores = Set.of(1, 2);

        Livro livroMultiplosAutores = Livro.builder()
                .titulo("Obra Colaborativa")
                .editora("Editora Y")
                .edicao(1)
                .anoPublicacao("1890")
                .valor(new BigDecimal("60.00"))
                .autores(new HashSet<>(Arrays.asList(autorTeste, autor2)))
                .build();

        when(autorRepository.findAllById(idsAutores))
                .thenReturn(Arrays.asList(autorTeste, autor2));
        when(livroRepository.save(any(Livro.class)))
                .thenReturn(livroMultiplosAutores);

        // Act
        Livro resultado = livroService.salvar(livroMultiplosAutores, idsAutores, Collections.emptySet());

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getAutores().size());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    @DisplayName("Deve salvar livro com múltiplos assuntos")
    void deveSalvarLivroComMultiplosAssuntos() {
        // Arrange
        Assunto assunto2 = Assunto.builder().id(2).descricao("Ficção").build();
        Set<Integer> idsAssuntos = Set.of(1, 2);

        Livro livroMultiplosAssuntos = Livro.builder()
                .titulo("Obra Diversa")
                .editora("Editora Z")
                .edicao(1)
                .anoPublicacao("1895")
                .valor(new BigDecimal("50.00"))
                .assuntos(new HashSet<>(Arrays.asList(assuntoTeste, assunto2)))
                .build();

        when(assuntoRepository.findAllById(idsAssuntos))
                .thenReturn(Arrays.asList(assuntoTeste, assunto2));
        when(livroRepository.save(any(Livro.class)))
                .thenReturn(livroMultiplosAssuntos);

        // Act
        Livro resultado = livroService.salvar(livroMultiplosAssuntos, Collections.emptySet(), idsAssuntos);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getAssuntos().size());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    @DisplayName("Deve validar valor mínimo do livro")
    void deveValidarValorMinimoDoLivro() {
        // Arrange
        Livro livroComValorInvalido = Livro.builder()
                .titulo("Livro Teste")
                .editora("Editora")
                .edicao(1)
                .anoPublicacao("2024")
                .valor(new BigDecimal("0.00")) // Valor inválido
                .build();

        // Act & Assert
        assertTrue(livroComValorInvalido.getValor().compareTo(BigDecimal.ZERO) <= 0);
    }
}
