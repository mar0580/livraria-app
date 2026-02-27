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
                        Autor.builder().nome("Machado de Assis").build()
                ));
            }

            if (assuntoRepository.count() == 0) {
                // Inserir assuntos padrão
                assuntoRepository.saveAll(Arrays.asList(
                        Assunto.builder().descricao("Ficção").build()
                ));
            }
        };
    }
}
