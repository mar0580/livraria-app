package com.livraria.controller;

import com.livraria.entity.Assunto;
import com.livraria.service.AssuntoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssuntoControllerTest {

    @Mock
    private AssuntoService assuntoService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @InjectMocks
    private AssuntoController controller;

    @BeforeEach
    void setup() {}

    @Test
    @DisplayName("Deve listar assuntos e retornar a view de lista")
    void listar_deveAdicionarTodosOsAssuntosERetornarView() {
        List<Assunto> fake = Arrays.asList(new Assunto(), new Assunto());
        when(assuntoService.listarTodos()).thenReturn(fake);

        String view = controller.listar(model);

        assertEquals("assunto/lista", view);
        verify(model).addAttribute("assuntos", fake);
    }

    @Test
    @DisplayName("Deve retornar o formulário de novo assunto")
    void novo_deveRetornarFormulario() {
        String view = controller.novo(new Assunto());
        assertEquals("assunto/form", view);
    }

    @Test
    @DisplayName("Quando houver erros de validação, manter o formulário e não salvar")
    void salvar_comErros_deveRetornarFormularioENaoSalvar() {
        when(bindingResult.hasErrors()).thenReturn(true);
        Assunto assunto = new Assunto();

        String view = controller.salvar(assunto, bindingResult, model);

        assertEquals("assunto/form", view);
        verifyNoInteractions(assuntoService);
    }

    @Test
    @DisplayName("Quando não houver erros de validação, salvar e redirecionar para listagem")
    void salvar_semErros_deveSalvarERedirecionar() {
        when(bindingResult.hasErrors()).thenReturn(false);
        Assunto assunto = new Assunto();

        String view = controller.salvar(assunto, bindingResult, model);

        assertEquals("redirect:/assuntos", view);
        verify(assuntoService).salvar(assunto);
    }

    @Test
    @DisplayName("Deve editar assunto existente e retornar o formulário preenchido")
    void editar_comIdExistente_deveAdicionarAssuntoERetornarFormulario() {
        Assunto stored = new Assunto();
        stored.setId(42);
        when(assuntoService.buscarPorId(42)).thenReturn(stored);

        String view = controller.editar(42, model);

        assertEquals("assunto/form", view);
        verify(model).addAttribute("assunto", stored);
    }

    @Test
    @DisplayName("Na tentativa de editar assunto inexistente, lançar exceção")
    void editar_comIdInexistente_deveLancarExcecao() {
        when(assuntoService.buscarPorId(99)).thenThrow(new IllegalArgumentException("Assunto inválido: 99"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> controller.editar(99, model));
        assertTrue(ex.getMessage().contains("Assunto inválido"));
    }

    @Test
    @DisplayName("Excluir assunto e redirecionar para listagem")
    void excluir_deveDeletarERedirecionar() {
        String view = controller.excluir(123);
        assertEquals("redirect:/assuntos", view);
        verify(assuntoService).excluir(123);
    }
}
