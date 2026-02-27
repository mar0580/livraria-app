package com.livraria.service.impl;

import com.livraria.entity.Autor;
import com.livraria.repository.AutorRepository;
import com.livraria.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;

    private static final Logger logger = LoggerFactory.getLogger(AutorServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Autor buscarPorId(Integer id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Autor não encontrado com ID: {}", id);
                    return new IllegalArgumentException("Autor inválido: " + id);
                });
    }

    @Override
    @Transactional
    public Autor salvar(Autor autor) {
        Autor salvo = autorRepository.save(autor);
        logger.debug("Autor salvo com ID={}, Nome={}", salvo.getId(), salvo.getNome());
        return salvo;
    }

    @Override
    @Transactional
    public void excluir(Integer id) {
        autorRepository.deleteById(id);
        logger.info("Exclusão solicitada para Autor id={}", id);
    }
}
