package com.livraria.service.impl;

import com.livraria.entity.Autor;
import com.livraria.repository.AutorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutorServiceImplTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorServiceImpl autorService;

    private Autor autor;
    private Autor autor2;

    @BeforeEach
    void setUp() {
        autor = Autor.builder()
                .id(1)
                .nome("Machado de Assis")
                .build();

        autor2 = Autor.builder()
                .id(2)
                .nome("Clarice Lispector")
                .build();
    }

    @Test
    @DisplayName("Listar todos os autores retornando conteúdo esperado")
    void listarTodos_deveRetornarListaDeAutores() {
        List<Autor> autores = Arrays.asList(autor, autor2);
        when(autorRepository.findAll()).thenReturn(autores);

        List<Autor> resultado = autorService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Machado de Assis", resultado.get(0).getNome());
        assertEquals("Clarice Lispector", resultado.get(1).getNome());
        verify(autorRepository).findAll();
    }

    @Test
    @DisplayName("Quando repositório estiver vazio, retornar lista vazia")
    void listarTodos_quandoVazio_deveRetornarListaVazia() {
        when(autorRepository.findAll()).thenReturn(Collections.emptyList());

        List<Autor> resultado = autorService.listarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(autorRepository).findAll();
    }

    @Test
    @DisplayName("Retornar a mesma referência fornecida pelo repositório")
    void listarTodos_deveRetornarMesmaReferenciaDoRepositorio() {
        List<Autor> autores = Arrays.asList(autor);
        when(autorRepository.findAll()).thenReturn(autores);

        List<Autor> resultado = autorService.listarTodos();

        assertSame(autores, resultado);
        verify(autorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Buscar por id válido e retornar autor")
    void buscarPorId_comIdValido_deveRetornarAutor() {
        when(autorRepository.findById(1)).thenReturn(Optional.of(autor));

        Autor resultado = autorService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Machado de Assis", resultado.getNome());
        verify(autorRepository).findById(1);
    }

    @Test
    @DisplayName("Buscar por id inválido e lançar IllegalArgumentException")
    void buscarPorId_comIdInvalido_deveLancarIllegalArgumentException() {
        when(autorRepository.findById(999)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            autorService.buscarPorId(999);
        });

        assertTrue(exception.getMessage().contains("Autor inválido"));
        verify(autorRepository).findById(999);
    }

    @Test
    @DisplayName("Buscar por id e retornar o mesmo objeto encontrado")
    void buscarPorId_deveRetornarMesmoObjeto() {
        when(autorRepository.findById(1)).thenReturn(Optional.of(autor));

        Autor resultado = autorService.buscarPorId(1);

        assertSame(autor, resultado);
        verify(autorRepository).findById(1);
    }

    @Test
    @DisplayName("Salva autor válido com sucesso")
    void salvar_comAutorValido_deveSalvarComSucesso() {
        Autor autorParaSalvar = Autor.builder()
                .nome("Novo Autor")
                .build();
        Autor autorSalvo = Autor.builder()
                .id(10)
                .nome("Novo Autor")
                .build();

        when(autorRepository.save(autorParaSalvar)).thenReturn(autorSalvo);

        Autor resultado = autorService.salvar(autorParaSalvar);

        assertNotNull(resultado);
        assertEquals(10, resultado.getId());
        assertEquals("Novo Autor", resultado.getNome());
        verify(autorRepository).save(autorParaSalvar);
    }

    @Test
    @DisplayName("Salvar e retornar o autor persistido")
    void salvar_deveRetornarAutorSalvo() {
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);

        Autor resultado = autorService.salvar(autor);

        assertSame(autor, resultado);
        verify(autorRepository).save(autor);
    }

    @Test
    @DisplayName("Salvar múltiplos autores e persistir cada um")
    void salvar_comMultiplosAutores_deveSalvarCadaUm() {
        Autor autor1Salvo = Autor.builder().id(1).nome("Autor 1").build();
        Autor autor2Salvo = Autor.builder().id(2).nome("Autor 2").build();

        when(autorRepository.save(any(Autor.class)))
                .thenReturn(autor1Salvo)
                .thenReturn(autor2Salvo);

        Autor resultado1 = autorService.salvar(Autor.builder().nome("Autor 1").build());
        Autor resultado2 = autorService.salvar(Autor.builder().nome("Autor 2").build());

        assertEquals(1, resultado1.getId());
        assertEquals(2, resultado2.getId());
        verify(autorRepository, times(2)).save(any(Autor.class));
    }

    @Test
    @DisplayName("Excluir com id válido e chamar repositório")
    void excluir_comIdValido_deveExcluirComSucesso() {
        doNothing().when(autorRepository).deleteById(1);

        autorService.excluir(1);

        verify(autorRepository).deleteById(1);
    }

    @Test
    @DisplayName("Excluir e invocar repositório exatamente uma vez")
    void excluir_deveInvocarRepositorioExatamenteUmaVez() {
        doNothing().when(autorRepository).deleteById(1);

        autorService.excluir(1);

        verify(autorRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Excluir múltiplos ids remove todos")
    void excluir_comMultiplosIds_deveExcluirTodos() {
        doNothing().when(autorRepository).deleteById(any(Integer.class));

        autorService.excluir(1);
        autorService.excluir(2);
        autorService.excluir(3);

        verify(autorRepository, times(3)).deleteById(any(Integer.class));
        verify(autorRepository).deleteById(1);
        verify(autorRepository).deleteById(2);
        verify(autorRepository).deleteById(3);
    }

    @Test
    @DisplayName("Buscar por id zero e lançar exceção")
    void buscarPorId_comValorZero_deveLancarExcecao() {
        when(autorRepository.findById(0)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            autorService.buscarPorId(0);
        });

        assertTrue(exception.getMessage().contains("Autor inválido"));
        verify(autorRepository).findById(0);
    }

    @Test
    @DisplayName("Buscar por id negativo e lançar exceção")
    void buscarPorId_comValorNegativo_deveLancarExcecao() {
        when(autorRepository.findById(-1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            autorService.buscarPorId(-1);
        });

        assertTrue(exception.getMessage().contains("Autor inválido"));
        verify(autorRepository).findById(-1);
    }

    @Test
    @DisplayName("Salvar e passar o autor correto ao repositório")
    void salvar_devePassarOAutorCorretoParaoRepositorio() {
        Autor autorEsperado = Autor.builder()
                .id(5)
                .nome("Autor Teste")
                .build();

        when(autorRepository.save(autorEsperado)).thenReturn(autorEsperado);

        autorService.salvar(autorEsperado);

        verify(autorRepository).save(autorEsperado);
    }

    @Test
    @DisplayName("Listar todos e chamar repositório duas vezes")
    void listarTodos_deveChamarRepositorioUmaUnicaVez() {
        when(autorRepository.findAll()).thenReturn(Collections.emptyList());

        autorService.listarTodos();
        autorService.listarTodos();

        verify(autorRepository, times(2)).findAll();
    }

    @Test
    @DisplayName("Mensagem de erro contém o id informado")
    void buscarPorId_mensagemDeErroDeveConterIdInformado() {
        when(autorRepository.findById(123)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            autorService.buscarPorId(123);
        });

        assertTrue(exception.getMessage().contains("123"));
        verify(autorRepository).findById(123);
    }
}