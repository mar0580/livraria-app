package com.livraria.controller;

import com.livraria.entity.Autor;
import com.livraria.service.AutorService;
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
class AutorControllerTest {

    @Mock
    private AutorService autorService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @InjectMocks
    private AutorController controller;

    @BeforeEach
    void setup() {}

    @Test
    @DisplayName("Listar autores e retornar a view de lista")
    void listar_deveAdicionarTodosOsAutoresERetornarView() {
        List<Autor> listaAutores = Arrays.asList(new Autor(), new Autor());
        when(autorService.listarTodos()).thenReturn(listaAutores);

        String view = controller.listar(model);

        assertEquals("autor/lista", view);
        verify(model).addAttribute("autores", listaAutores);
    }

    @Test
    @DisplayName("Retornar o formulário de novo autor")
    void novo_deveRetornarFormulario() {
        String view = controller.novo(new Autor());
        assertEquals("autor/form", view);
    }

    @Test
    @DisplayName("Quando há erros de validação, mantém o formulário e não salva")
    void salvar_comErros_deveRetornarFormularioENaoSalvar() {
        when(bindingResult.hasErrors()).thenReturn(true);
        Autor autor = new Autor();

        String view = controller.salvar(autor, bindingResult, model);

        assertEquals("autor/form", view);
        verifyNoInteractions(autorService);
    }

    @Test
    @DisplayName("Quando não há erros de validação, salva e redireciona")
    void salvar_semErros_deveSalvarERedirecionar() {
        when(bindingResult.hasErrors()).thenReturn(false);
        Autor autor = new Autor();

        String view = controller.salvar(autor, bindingResult, model);

        assertEquals("redirect:/autores", view);
        verify(autorService).salvar(autor);
    }

    @Test
    @DisplayName("Editar autor existente e retorna o formulário")
    void editar_comIdExistente_deveAdicionarAutorERetornarFormulario() {
        Autor autor = new Autor();
        autor.setId(42);
        when(autorService.buscarPorId(42)).thenReturn(autor);

        String view = controller.editar(42, model);

        assertEquals("autor/form", view);
        verify(model).addAttribute("autor", autor);
    }

    @Test
    @DisplayName("Ao editar autor inexistente, lançar exceção")
    void editar_comIdInexistente_deveLancarExcecao() {
        when(autorService.buscarPorId(99)).thenThrow(new IllegalArgumentException("Autor inválido: 99"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> controller.editar(99, model));
        assertTrue(ex.getMessage().contains("Autor inválido"));
    }

    @Test
    @DisplayName("Excluir autor e redirecionar para listagem")
    void excluir_deveDeletarERedirecionar() {
        String view = controller.excluir(123);
        assertEquals("redirect:/autores", view);
        verify(autorService).excluir(123);
    }
}