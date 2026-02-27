package com.livraria.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        logger.error("Recurso não encontrado: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("status", 404);
        return "error/404";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataIntegrity(DataIntegrityViolationException ex, Model model) {
        logger.error("Erro de integridade de dados", ex);
        String mensagem = "Operação não permitida. Verifique se há vínculos com outros registros.";
        // detecta FK para tabela de junção livro_autor e oferece explicação mais amigável
        Throwable root = ex.getRootCause();
        if (root != null && root.getMessage() != null) {
            String rootMessage = root.getMessage();
            if (rootMessage.contains("uq_autor_nome_ci") || rootMessage.contains("autor_nome") || rootMessage.contains("autor")) {
                mensagem = "Autor já cadastrado. Informe um nome diferente.";
            } else if (rootMessage.contains("uq_assunto_descricao_ci") || rootMessage.contains("assunto_descricao") || rootMessage.contains("assunto")) {
                mensagem = "Assunto já cadastrado. Informe uma descrição diferente.";
            } else if (rootMessage.contains("livro_autor")) {
                mensagem = "Não é possível excluir o autor porque existem livros vinculados a ele. " +
                           "Desvincule ou exclua os registros relacionados antes.";
            } else if (rootMessage.contains("livro_assunto")) {
                mensagem = "Não é possível excluir o assunto porque existem livros vinculados a ele. " +
                           "Desvincule ou exclua os registros relacionados antes.";
            }
        }
        model.addAttribute("errorMessage", mensagem);
        model.addAttribute("status", 400);
        return "error/generic";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        logger.error("Argumento inválido: {}", ex.getMessage());
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("status", 400);
        return "error/generic";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex, Model model) {
        logger.error("Erro não tratado", ex);
        model.addAttribute("errorMessage", "Erro inesperado ao processar sua solicitação. Tente novamente mais tarde.");
        model.addAttribute("status", 500);
        return "error/generic";
    }
}
