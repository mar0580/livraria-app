package com.livraria.service.impl;

import com.livraria.entity.Assunto;
import com.livraria.entity.Autor;
import com.livraria.entity.Livro;
import com.livraria.repository.AssuntoRepository;
import com.livraria.repository.AutorRepository;
import com.livraria.repository.LivroRepository;
import com.livraria.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final AssuntoRepository assuntoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Livro buscarPorId(Integer id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado com ID: " + id));
    }

    @Override
    @Transactional
    public Livro salvar(Livro livro, Set<Integer> idsAutores, Set<Integer> idsAssuntos) {
        // Buscar e associar autores
        if (idsAutores != null && !idsAutores.isEmpty()) {
            Set<Autor> autores = idsAutores.stream()
                    .map(id -> autorRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado com ID: " + id)))
                    .collect(Collectors.toSet());
            livro.setAutores(autores);
        } else {
            livro.setAutores(new HashSet<>());
        }

        // Buscar e associar assuntos
        if (idsAssuntos != null && !idsAssuntos.isEmpty()) {
            Set<Assunto> assuntos = idsAssuntos.stream()
                    .map(id -> assuntoRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Assunto não encontrado com ID: " + id)))
                    .collect(Collectors.toSet());
            livro.setAssuntos(assuntos);
        } else {
            livro.setAssuntos(new HashSet<>());
        }

        return livroRepository.save(livro);
    }

    @Override
    @Transactional
    public void excluir(Integer id) {
        if (!livroRepository.existsById(id)) {
            throw new IllegalArgumentException("Livro não encontrado com ID: " + id);
        }
        livroRepository.deleteById(id);
    }
}
