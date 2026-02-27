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
import static org.mockito.Mockito.*;

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
        List<Livro> livrosEsperados = Arrays.asList(livroTeste);
        when(livroRepository.findAll()).thenReturn(livrosEsperados);

        List<Livro> resultado = livroService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Dom Casmurro", resultado.get(0).getTitulo());
        verify(livroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não existem livros")
    void deveRetornarListaVaziaQuandoNaoExistemLivros() {

        when(livroRepository.findAll()).thenReturn(Collections.emptyList());

        List<Livro> resultado = livroService.listarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(livroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar livro por ID com sucesso")
    void deveBuscarLivroPorIdComSucesso() {
        when(livroRepository.findById(1)).thenReturn(Optional.of(livroTeste));

        Livro resultado = livroService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Dom Casmurro", resultado.getTitulo());
        verify(livroRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar livro inexistente")
    void deveLancarExcecaoAoBuscarLivroInexistente() {
        when(livroRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> livroService.buscarPorId(999));
        verify(livroRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Deve salvar novo livro com autores e assuntos")
    void deveSalvarNovoLivroComAutoresEAssuntos() {
        Livro novoLivro = Livro.builder()
                .titulo("Quincas Borba")
                .editora("Editora Teste")
                .edicao(1)
                .anoPublicacao("1891")
                .valor(new BigDecimal("45.00"))
                .build();

        Set<Integer> idsAutores = Set.of(1);
        Set<Integer> idsAssuntos = Set.of(1);

        when(autorRepository.findById(1)).thenReturn(Optional.of(autorTeste));
        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assuntoTeste));
        when(livroRepository.save(any(Livro.class)))
                .thenReturn(livroTeste);

        Livro resultado = livroService.salvar(novoLivro, idsAutores, idsAssuntos);

        assertNotNull(resultado);
        assertEquals("Dom Casmurro", resultado.getTitulo());
        assertFalse(resultado.getAutores().isEmpty());
        assertFalse(resultado.getAssuntos().isEmpty());
        verify(livroRepository, times(1)).save(any(Livro.class));
        verify(autorRepository, times(1)).findById(1);
        verify(assuntoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar com autores e assuntos vazios")
    void deveLancarExcecaoAoSalvarComAutoresEAssuntosVazios() {
        Livro novoLivro = Livro.builder()
                .titulo("Memórias Póstumas de Brás Cubas")
                .editora("Editora X")
                .edicao(1)
                .anoPublicacao("1881")
                .valor(new BigDecimal("55.00"))
                .autores(new HashSet<>())
                .assuntos(new HashSet<>())
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> livroService.salvar(novoLivro, Collections.emptySet(), Collections.emptySet()));
        assertTrue(ex.getMessage().contains("autor"));
    }

    @Test
    @DisplayName("Deve atualizar livro existente com sucesso")
    void deveAtualizarLivroExistenteComSucesso() {
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

        when(autorRepository.findById(1)).thenReturn(Optional.of(autorTeste));
        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assuntoTeste));
        when(livroRepository.save(any(Livro.class)))
                .thenReturn(livroAtualizado);

        Livro resultado = livroService.salvar(livroAtualizado, idsAutores, idsAssuntos);

        assertNotNull(resultado);
        assertEquals(2, resultado.getEdicao());
        assertEquals(new BigDecimal("59.90"), resultado.getValor());
        verify(livroRepository, times(1)).save(any(Livro.class));
        verify(autorRepository, times(1)).findById(1);
        verify(assuntoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Deve excluir livro com sucesso")
    void deveExcluirLivroComSucesso() {
        when(livroRepository.existsById(1)).thenReturn(true);
        doNothing().when(livroRepository).deleteById(1);
        livroService.excluir(1);
        verify(livroRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios do livro")
    void deveValidarCamposObrigatoriosDoLivro() {
        Livro livroInvalido = Livro.builder()
                .titulo(null)
                .editora("Editora")
                .edicao(1)
                .anoPublicacao("2024")
                .valor(new BigDecimal("50.00"))
                .build();
        assertNull(livroInvalido.getTitulo());
    }

    @Test
        @DisplayName("Deve salvar livro com múltiplos autores")
        void deveSalvarLivroComMultiplosAutores() {
        Autor autor2 = Autor.builder().id(2).nome("Cruz e Sousa").build();
        Set<Integer> idsAutores = Set.of(1, 2);
        Set<Integer> idsAssuntos = Set.of(1);

        Livro livroMultiplosAutores = Livro.builder()
                .titulo("Obra Colaborativa")
                .editora("Editora Y")
                .edicao(1)
                .anoPublicacao("1890")
                .valor(new BigDecimal("60.00"))
            .autores(new HashSet<>(Arrays.asList(autorTeste, autor2)))
            .assuntos(new HashSet<>(Set.of(assuntoTeste)))
                .build();

        when(autorRepository.findById(1)).thenReturn(Optional.of(autorTeste));
        when(autorRepository.findById(2)).thenReturn(Optional.of(autor2));
        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assuntoTeste));
        when(livroRepository.save(any(Livro.class)))
                .thenReturn(livroMultiplosAutores);

        Livro resultado = livroService.salvar(livroMultiplosAutores, idsAutores, idsAssuntos);

        assertNotNull(resultado);
        assertEquals(2, resultado.getAutores().size());
        verify(livroRepository, times(1)).save(any(Livro.class));
        verify(autorRepository, times(1)).findById(1);
        verify(autorRepository, times(1)).findById(2);
        verify(assuntoRepository, times(1)).findById(1);
    }

    @Test
        @DisplayName("Deve salvar livro com múltiplos assuntos")
        void deveSalvarLivroComMultiplosAssuntos() {

        Assunto assunto2 = Assunto.builder().id(2).descricao("Ficção").build();
        Set<Integer> idsAssuntos = Set.of(1, 2);
        Set<Integer> idsAutores = Set.of(1);

        Livro livroMultiplosAssuntos = Livro.builder()
                .titulo("Obra Diversa")
                .editora("Editora Z")
                .edicao(1)
                .anoPublicacao("1895")
                .valor(new BigDecimal("50.00"))
            .autores(new HashSet<>(Set.of(autorTeste)))
                .assuntos(new HashSet<>(Arrays.asList(assuntoTeste, assunto2)))
                .build();

        when(autorRepository.findById(1)).thenReturn(Optional.of(autorTeste));
        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assuntoTeste));
        when(assuntoRepository.findById(2)).thenReturn(Optional.of(assunto2));
        when(livroRepository.save(any(Livro.class)))
                .thenReturn(livroMultiplosAssuntos);

        Livro resultado = livroService.salvar(livroMultiplosAssuntos, idsAutores, idsAssuntos);

        assertNotNull(resultado);
        assertEquals(2, resultado.getAssuntos().size());
        verify(livroRepository, times(1)).save(any(Livro.class));
        verify(autorRepository, times(1)).findById(1);
        verify(assuntoRepository, times(1)).findById(1);
        verify(assuntoRepository, times(1)).findById(2);
    }

    @Test
    @DisplayName("Deve validar valor mínimo do livro")
    void deveValidarValorMinimoDoLivro() {
        Livro livroComValorInvalido = Livro.builder()
                .titulo("Livro Teste")
                .editora("Editora")
                .edicao(1)
                .anoPublicacao("2024")
                .valor(new BigDecimal("0.00"))
                .build();
        assertTrue(livroComValorInvalido.getValor().compareTo(BigDecimal.ZERO) <= 0);
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor não for encontrado")
    void deveLancarExcecaoQuandoAutorNaoEncontrado() {
        Livro novoLivro = Livro.builder().titulo("Teste").build();
        Set<Integer> idsAutores = Set.of(999);
        Set<Integer> idsAssuntos = Set.of(1);
        when(autorRepository.findById(999)).thenReturn(Optional.empty());
        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assuntoTeste));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> livroService.salvar(novoLivro, idsAutores, idsAssuntos));
        assertTrue(ex.getMessage().contains("Autor não encontrado com ID: 999"));
        verify(autorRepository, times(1)).findById(999);
        verify(assuntoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando assunto não for encontrado")
    void deveLancarExcecaoQuandoAssuntoNaoEncontrado() {
        Livro novoLivro = Livro.builder().titulo("Teste").build();
        Set<Integer> idsAutores = Set.of(1);
        Set<Integer> idsAssuntos = Set.of(888);
        when(autorRepository.findById(1)).thenReturn(Optional.of(autorTeste));
        when(assuntoRepository.findById(888)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> livroService.salvar(novoLivro, idsAutores, idsAssuntos));
        assertTrue(ex.getMessage().contains("Assunto não encontrado com ID: 888"));
        verify(autorRepository, times(1)).findById(1);
        verify(assuntoRepository, times(1)).findById(888);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir livro inexistente")
    void deveLancarExcecaoAoExcluirLivroInexistente() {
        when(livroRepository.existsById(999)).thenReturn(false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> livroService.excluir(999));
        assertTrue(ex.getMessage().contains("Livro não encontrado com ID: 999"));
        verify(livroRepository, never()).deleteById(999);
    }

    @Test
    @DisplayName("Deve validar que existsById é verificado antes de deletar")
    void deveVerificarExistenciaAntesDeDeletar() {
        when(livroRepository.existsById(1)).thenReturn(true);
        doNothing().when(livroRepository).deleteById(1);
        livroService.excluir(1);
        verify(livroRepository, times(1)).existsById(1);
        verify(livroRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar com IDs null para autores e assuntos")
    void deveLancarExcecaoAoSalvarComIdsNullAutoresEAssuntos() {
        Livro novoLivro = Livro.builder().titulo("Teste").build();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> livroService.salvar(novoLivro, null, null));
        assertTrue(ex.getMessage().contains("autor"));
    }

    @Test
    @DisplayName("Deve salvar livro com um autor e múltiplos assuntos")
    void deveSalvarLivroComUmAutorEMultiplosAssuntos() {
        Assunto assunto2 = Assunto.builder().id(3).descricao("Poesática").build();
        Set<Integer> idsAutores = Set.of(1);
        Set<Integer> idsAssuntos = Set.of(1, 3);
        Livro novoLivro = Livro.builder()
                .titulo("Música Brasileira")
                .editora("Editora X")
                .edicao(1)
                .anoPublicacao("1900")
                .valor(new BigDecimal("75.00"))
                .build();

        Livro esperado = Livro.builder()
                .id(5)
                .titulo("Música Brasileira")
                .editora("Editora X")
                .edicao(1)
                .anoPublicacao("1900")
                .valor(new BigDecimal("75.00"))
                .autores(new HashSet<>(Set.of(autorTeste)))
                .assuntos(new HashSet<>(Arrays.asList(assuntoTeste, assunto2)))
                .build();

        when(autorRepository.findById(1)).thenReturn(Optional.of(autorTeste));
        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assuntoTeste));
        when(assuntoRepository.findById(3)).thenReturn(Optional.of(assunto2));
        when(livroRepository.save(any(Livro.class))).thenReturn(esperado);

        Livro resultado = livroService.salvar(novoLivro, idsAutores, idsAssuntos);

        assertNotNull(resultado);
        assertEquals(1, resultado.getAutores().size());
        assertEquals(2, resultado.getAssuntos().size());
        verify(livroRepository, times(1)).save(any(Livro.class));
        verify(autorRepository, times(1)).findById(1);
        verify(assuntoRepository, times(1)).findById(1);
        verify(assuntoRepository, times(1)).findById(3);
    }
}
