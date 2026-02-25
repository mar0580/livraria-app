package com.livraria;

import com.livraria.controller.HomeController;
import com.livraria.controller.LivroController;
import com.livraria.controller.RelatorioController;
import com.livraria.service.LivroService;
import com.livraria.service.RelatorioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Testes de integração da aplicação
 * Valida que todos os componentes estão corretamente configurados
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes de Integração da Aplicação")
class ProjetoParaCadastroDeLivrosApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve carregar o contexto da aplicação com sucesso")
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    @DisplayName("HomeController deve estar disponível no contexto")
    void homeControllerDeveSairDisponivel() {
        HomeController homeController = applicationContext.getBean(HomeController.class);
        assertNotNull(homeController);
    }

    @Test
    @DisplayName("LivroController deve estar disponível no contexto")
    void livroControllerDeveSairDisponivel() {
        LivroController livroController = applicationContext.getBean(LivroController.class);
        assertNotNull(livroController);
    }

    @Test
    @DisplayName("RelatorioController deve estar disponível no contexto")
    void relatorioControllerDeveSairDisponivel() {
        RelatorioController relatorioController = applicationContext.getBean(RelatorioController.class);
        assertNotNull(relatorioController);
    }

    @Test
    @DisplayName("LivroService deve estar disponível no contexto")
    void livroServiceDeveSairDisponivel() {
        LivroService livroService = applicationContext.getBean(LivroService.class);
        assertNotNull(livroService);
    }

    @Test
    @DisplayName("RelatorioService deve estar disponível no contexto")
    void relatorioServiceDeveSairDisponivel() {
        RelatorioService relatorioService = applicationContext.getBean(RelatorioService.class);
        assertNotNull(relatorioService);
    }
}
