package com.livraria.service.impl;

import com.livraria.entity.Livro;
import com.livraria.repository.AssuntoRepository;
import com.livraria.repository.AutorRepository;
import com.livraria.repository.LivroRepository;
import com.livraria.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivroServiceImpl implements LivroService {

    private static final Logger logger = LoggerFactory.getLogger(LivroServiceImpl.class);

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
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }

        boolean autoresVazios = idsAutores == null || idsAutores.isEmpty();
        boolean assuntosVazios = idsAssuntos == null || idsAssuntos.isEmpty();
        if (autoresVazios && assuntosVazios) {
            throw new IllegalArgumentException("Selecione ao menos um autor e um assunto.");
        }
        if (autoresVazios) {
            throw new IllegalArgumentException("Selecione ao menos um autor.");
        }
        if (assuntosVazios) {
            throw new IllegalArgumentException("Selecione ao menos um assunto.");
        }

        Set<Integer> autoresIds = normalizarIds(idsAutores, "autores");
        Set<Integer> assuntosIds = normalizarIds(idsAssuntos, "assuntos");

        livro.setAutores(resolverEntidades(autoresIds, autorRepository::findById, "Autor"));
        livro.setAssuntos(resolverEntidades(assuntosIds, assuntoRepository::findById, "Assunto"));

        Livro salvo = livroRepository.save(livro);
        logger.info("Livro salvo com sucesso: {}", salvo.getTitulo());
        return salvo;
    }

    @Override
    @Transactional
    public void excluir(Integer id) {
        if (!livroRepository.existsById(id)) {
            throw new IllegalArgumentException("Livro não encontrado com ID: " + id);
        }
        livroRepository.deleteById(id);
    }

    /**
     * Método genérico que resolve uma coleção de IDs para suas respectivas entidades.
     * Reduz duplicação de código entre autores e assuntos.
     */
    private <E> Set<E> resolverEntidades(Set<Integer> ids,
                                         Function<Integer, Optional<E>> buscador,
                                         String nomeTipo) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return ids.stream()
                .map(id -> buscador.apply(id)
                        .orElseThrow(() -> new IllegalArgumentException(
                                nomeTipo + " não encontrado com ID: " + id)))
                .collect(Collectors.toSet());
    }

    private Set<Integer> normalizarIds(Set<Integer> ids, String nomeCampo) {
        if (ids == null || ids.isEmpty()) {
            logger.debug("IDs de {} não informados. Usando conjunto vazio.", nomeCampo);
            return new HashSet<>();
        }
        return ids;
    }
}
