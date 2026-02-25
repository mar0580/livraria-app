package com.livraria.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para exceções customizadas da aplicação
 */
@DisplayName("Testes de Tratamento de Exceções")
class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Deve criar exceção com mensagem")
    void deveCriarExcecaoComMensagem() {
        // Arrange
        String mensagem = "Livro com ID 999 não encontrado";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(mensagem);

        // Assert
        assertNotNull(exception);
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção com mensagem customizada")
    void deveLancarExcecaoComMensagemCustomizada() {
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    throw new ResourceNotFoundException("Recurso não encontrado");
                }
        );

        assertEquals("Recurso não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve herdar de RuntimeException")
    void deveHerdarDeRuntimeException() {
        // Arrange & Act
        ResourceNotFoundException exception = new ResourceNotFoundException("Teste");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}
