//package com.livraria.repository;
//
//import com.livraria.entity.Livro;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Testes de integração para LivroRepository
// * Testa operações de persistência no banco de dados
// */
//@DataJpaTest
//@ActiveProfiles("test")
//@DisplayName("Testes do Repositório de Livros")
//class LivroRepositoryTest {
//
//    @Autowired
//    private LivroRepository livroRepository;
//
//    private Livro livroTeste;
//
//    @BeforeEach
//    void setUp() {
//        livroTeste = Livro.builder()
//                .titulo("Dom Casmurro")
//                .editora("Companhia das Letras")
//                .edicao(1)
//                .anoPublicacao("1899")
//                .valor(new BigDecimal("49.90"))
//                .build();
//    }
//
//    @Test
//    @DisplayName("Deve salvar um livro no banco de dados")
//    void deveSalvarUmLivroNoBancoDeDados() {
//        // Act
//        Livro livroSalvo = livroRepository.save(livroTeste);
//
//        // Assert
//        assertNotNull(livroSalvo.getId());
//        assertTrue(livroRepository.existsById(livroSalvo.getId()));
//    }
//
//    @Test
//    @DisplayName("Deve buscar livro por ID")
//    void deveBuscarLivroPorId() {
//        // Arrange
//        Livro livroSalvo = livroRepository.save(livroTeste);
//
//        // Act
//        Optional<Livro> resultado = livroRepository.findById(livroSalvo.getId());
//
//        // Assert
//        assertTrue(resultado.isPresent());
//        assertEquals("Dom Casmurro", resultado.get().getTitulo());
//    }
//
//    @Test
//    @DisplayName("Deve atualizar um livro existente")
//    void deveAtualizarUmLivroExistente() {
//        // Arrange
//        Livro livroSalvo = livroRepository.save(livroTeste);
//        livroSalvo.setTitulo("Dom Casmurro - Edição Revisada");
//        livroSalvo.setEdicao(2);
//
//        // Act
//        Livro livroAtualizado = livroRepository.save(livroSalvo);
//
//        // Assert
//        assertEquals("Dom Casmurro - Edição Revisada", livroAtualizado.getTitulo());
//        assertEquals(2, livroAtualizado.getEdicao());
//    }
//
//    @Test
//    @DisplayName("Deve excluir um livro do banco de dados")
//    void deveExcluirUmLivroDoBancoDeDados() {
//        // Arrange
//        Livro livroSalvo = livroRepository.save(livroTeste);
//        Integer id = livroSalvo.getId();
//
//        // Act
//        livroRepository.deleteById(id);
//
//        // Assert
//        assertFalse(livroRepository.existsById(id));
//    }
//
//    @Test
//    @DisplayName("Deve retornar false ao buscar livro inexistente")
//    void deveRetornarFalseAoBuscarLivroInexistente() {
//        // Act & Assert
//        assertFalse(livroRepository.existsById(999));
//    }
//
//    @Test
//    @DisplayName("Deve contar todos os livros no banco de dados")
//    void deveContarTodosOsLivrosNoBancoDeDados() {
//        // Arrange
//        livroRepository.save(livroTeste);
//        Livro livro2 = Livro.builder()
//                .titulo("Quincas Borba")
//                .editora("Editora X")
//                .edicao(1)
//                .anoPublicacao("1891")
//                .valor(new BigDecimal("45.00"))
//                .build();
//        livroRepository.save(livro2);
//
//        // Act
//        long total = livroRepository.count();
//
//        // Assert
//        assertEquals(2, total);
//    }
//
//    @Test
//    @DisplayName("Deve salvar livro com valor em BigDecimal corretamente")
//    void deveSalvarLivroComValorEmBigDecimalCorretamente() {
//        // Arrange
//        livroTeste.setValor(new BigDecimal("199.99"));
//
//        // Act
//        Livro livroSalvo = livroRepository.save(livroTeste);
//
//        // Assert
//        assertNotNull(livroSalvo.getId());
//        assertEquals(new BigDecimal("199.99"), livroSalvo.getValor());
//    }
//
//    @Test
//    @DisplayName("Deve persistir todas as propriedades do livro")
//    void devePeristirTodasAsPropriedadesDoLivro() {
//        // Act
//        Livro livroSalvo = livroRepository.save(livroTeste);
//        Livro livroRecuperado = livroRepository.findById(livroSalvo.getId()).orElse(null);
//
//        // Assert
//        assertNotNull(livroRecuperado);
//        assertEquals("Dom Casmurro", livroRecuperado.getTitulo());
//        assertEquals("Companhia das Letras", livroRecuperado.getEditora());
//        assertEquals(1, livroRecuperado.getEdicao());
//        assertEquals("1899", livroRecuperado.getAnoPublicacao());
//        assertEquals(new BigDecimal("49.90"), livroRecuperado.getValor());
//    }
//}
