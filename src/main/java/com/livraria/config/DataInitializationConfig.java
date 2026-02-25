package com.livraria.config;

import com.livraria.entity.Assunto;
import com.livraria.entity.Autor;
import com.livraria.repository.AssuntoRepository;
import com.livraria.repository.AutorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializationConfig {

    @Bean
    public CommandLineRunner initDatabase(AutorRepository autorRepository, AssuntoRepository assuntoRepository) {
        return args -> {
            // Verificar se já existem dados
            if (autorRepository.count() == 0) {
                // Inserir autores padrão
                autorRepository.saveAll(Arrays.asList(
                        Autor.builder().nome("Machado de Assis").build(),
                        Autor.builder().nome("Clarice Lispector").build(),
                        Autor.builder().nome("Paulo Coelho").build(),
                        Autor.builder().nome("Jorge Amado").build(),
                        Autor.builder().nome("Carlos Drummond de Andrade").build()
                ));
            }

            if (assuntoRepository.count() == 0) {
                // Inserir assuntos padrão
                assuntoRepository.saveAll(Arrays.asList(
                        Assunto.builder().descricao("Romance").build(),
                        Assunto.builder().descricao("Ficção").build(),
                        Assunto.builder().descricao("Drama").build(),
                        Assunto.builder().descricao("Aventura").build(),
                        Assunto.builder().descricao("Poesia").build(),
                        Assunto.builder().descricao("Suspense").build(),
                        Assunto.builder().descricao("Fantasia").build(),
                        Assunto.builder().descricao("Biografia").build()
                ));
            }
        };
    }
}
