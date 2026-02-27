package com.livraria.service.impl;

import com.livraria.entity.Assunto;
import com.livraria.repository.AssuntoRepository;
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
class AssuntoServiceImplTest {

    @Mock
    private AssuntoRepository assuntoRepository;

    @InjectMocks
    private AssuntoServiceImpl assuntoService;

    private Assunto assunto;
    private Assunto assunto2;

    @BeforeEach
    void setUp() {
        assunto = Assunto.builder()
                .id(1)
                .descricao("Ficção Científica")
                .build();

        assunto2 = Assunto.builder()
                .id(2)
                .descricao("Romance")
                .build();
    }

    @Test
    @DisplayName("Listar assuntos retornando conteúdo esperado")
    void listarTodos_deveRetornarListaDeAssuntos() {
        List<Assunto> assuntos = Arrays.asList(assunto, assunto2);
        when(assuntoRepository.findAll()).thenReturn(assuntos);

        List<Assunto> resultado = assuntoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Ficção Científica", resultado.get(0).getDescricao());
        assertEquals("Romance", resultado.get(1).getDescricao());
        verify(assuntoRepository).findAll();
    }

    @Test
    @DisplayName("Quando repositório vazio, retornar lista vazia")
    void listarTodos_quandoVazio_deveRetornarListaVazia() {
        when(assuntoRepository.findAll()).thenReturn(Collections.emptyList());

        List<Assunto> resultado = assuntoService.listarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(assuntoRepository).findAll();
    }

    @Test
    @DisplayName("Retornar a mesma referência do repositório")
    void listarTodos_deveRetornarMesmaReferenciaDoRepositorio() {
        List<Assunto> assuntos = Arrays.asList(assunto);
        when(assuntoRepository.findAll()).thenReturn(assuntos);

        List<Assunto> resultado = assuntoService.listarTodos();

        assertSame(assuntos, resultado);
        verify(assuntoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Buscar por id válido e retornar assunto")
    void buscarPorId_comIdValido_deveRetornarAssunto() {
        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assunto));

        Assunto resultado = assuntoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Ficção Científica", resultado.getDescricao());
        verify(assuntoRepository).findById(1);
    }

    @Test
    @DisplayName("Buscar por id inválido e lançar IllegalArgumentException")
    void buscarPorId_comIdInvalido_deveLancarIllegalArgumentException() {
        when(assuntoRepository.findById(999)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            assuntoService.buscarPorId(999);
        });

        assertTrue(exception.getMessage().contains("Assunto inválido"));
        verify(assuntoRepository).findById(999);
    }

    @Test
    @DisplayName("Buscar por id e retornar o mesmo objeto")
    void buscarPorId_deveRetornarMesmoObjeto() {
        when(assuntoRepository.findById(1)).thenReturn(Optional.of(assunto));

        Assunto resultado = assuntoService.buscarPorId(1);

        assertSame(assunto, resultado);
        verify(assuntoRepository).findById(1);
    }

    @Test
    @DisplayName("Salvar assunto válido com sucesso")
    void salvar_comAssuntoValido_deveSalvarComSucesso() {
        Assunto assuntoParaSalvar = Assunto.builder()
                .descricao("Novo Assunto")
                .build();
        Assunto assuntoSalvo = Assunto.builder()
                .id(10)
                .descricao("Novo Assunto")
                .build();

        when(assuntoRepository.save(assuntoParaSalvar)).thenReturn(assuntoSalvo);

        Assunto resultado = assuntoService.salvar(assuntoParaSalvar);

        assertNotNull(resultado);
        assertEquals(10, resultado.getId());
        assertEquals("Novo Assunto", resultado.getDescricao());
        verify(assuntoRepository).save(assuntoParaSalvar);
    }

    @Test
    @DisplayName("Salvar retorna o assunto persistido")
    void salvar_deveRetornarAssuntoSalvo() {
        when(assuntoRepository.save(any(Assunto.class))).thenReturn(assunto);

        Assunto resultado = assuntoService.salvar(assunto);

        assertSame(assunto, resultado);
        verify(assuntoRepository).save(assunto);
    }

    @Test
    @DisplayName("Salvar múltiplos assuntos persiste cada um")
    void salvar_comMultiplosAssuntos_deveSalvarCadaUm() {
        Assunto assunto1Salvo = Assunto.builder().id(1).descricao("Ficção").build();
        Assunto assunto2Salvo = Assunto.builder().id(2).descricao("Drama").build();

        when(assuntoRepository.save(any(Assunto.class)))
                .thenReturn(assunto1Salvo)
                .thenReturn(assunto2Salvo);

        Assunto resultado1 = assuntoService.salvar(Assunto.builder().descricao("Ficção").build());
        Assunto resultado2 = assuntoService.salvar(Assunto.builder().descricao("Drama").build());

        assertEquals(1, resultado1.getId());
        assertEquals(2, resultado2.getId());
        verify(assuntoRepository, times(2)).save(any(Assunto.class));
    }

    @Test
    @DisplayName("Excluir com id válido e chamar repositório")
    void excluir_comIdValido_deveExcluirComSucesso() {
        doNothing().when(assuntoRepository).deleteById(1);

        assuntoService.excluir(1);

        verify(assuntoRepository).deleteById(1);
    }

    @Test
    @DisplayName("Excluir e invocar repositório uma vez")
    void excluir_deveInvocarRepositorioExatamenteUmaVez() {
        doNothing().when(assuntoRepository).deleteById(1);

        assuntoService.excluir(1);

        verify(assuntoRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Excluir múltiplos ids remove todos")
    void excluir_comMultiplosIds_deveExcluirTodos() {
        doNothing().when(assuntoRepository).deleteById(any(Integer.class));

        assuntoService.excluir(1);
        assuntoService.excluir(2);
        assuntoService.excluir(3);

        verify(assuntoRepository, times(3)).deleteById(any(Integer.class));
        verify(assuntoRepository).deleteById(1);
        verify(assuntoRepository).deleteById(2);
        verify(assuntoRepository).deleteById(3);
    }

    @Test
    @DisplayName("Buscar por id zero e lançar exceção")
    void buscarPorId_comValorZero_deveLancarExcecao() {
        when(assuntoRepository.findById(0)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            assuntoService.buscarPorId(0);
        });

        assertTrue(exception.getMessage().contains("Assunto inválido"));
        verify(assuntoRepository).findById(0);
    }

    @Test
    @DisplayName("Buscar por id negativo e lança exceção")
    void buscarPorId_comValorNegativo_deveLancarExcecao() {
        when(assuntoRepository.findById(-1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            assuntoService.buscarPorId(-1);
        });

        assertTrue(exception.getMessage().contains("Assunto inválido"));
        verify(assuntoRepository).findById(-1);
    }

    @Test
    @DisplayName("Salvar e passar o assunto correto ao repositório")
    void salvar_devePassarOAssuntoCorretoParaoRepositorio() {
        Assunto assuntoEsperado = Assunto.builder()
                .id(5)
                .descricao("Assunto Teste")
                .build();

        when(assuntoRepository.save(assuntoEsperado)).thenReturn(assuntoEsperado);

        assuntoService.salvar(assuntoEsperado);

        verify(assuntoRepository).save(assuntoEsperado);
    }

    @Test
    @DisplayName("Listar todos e chamar repositório duas vezes")
    void listarTodos_deveChamarRepositorioUmaUnicaVez() {
        when(assuntoRepository.findAll()).thenReturn(Collections.emptyList());

        assuntoService.listarTodos();
        assuntoService.listarTodos();

        verify(assuntoRepository, times(2)).findAll();
    }
}