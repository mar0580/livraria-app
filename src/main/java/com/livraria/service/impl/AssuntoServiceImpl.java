package com.livraria.service.impl;

import com.livraria.entity.Assunto;
import com.livraria.repository.AssuntoRepository;
import com.livraria.service.AssuntoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssuntoServiceImpl implements AssuntoService {

    private final AssuntoRepository assuntoRepository;

    private static final Logger logger = LoggerFactory.getLogger(AssuntoServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public List<Assunto> listarTodos() {
        return assuntoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Assunto buscarPorId(Integer id) {
        return assuntoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Assunto não encontrado com ID: {}", id);
                    return new IllegalArgumentException("Assunto inválido: " + id);
                });
    }

    @Override
    @Transactional
    public Assunto salvar(Assunto assunto) {
        Assunto salvo = assuntoRepository.save(assunto);
        logger.debug("Assunto salvo com ID={}", salvo.getId());
        return salvo;
    }

    @Override
    @Transactional
    public void excluir(Integer id) {
        assuntoRepository.deleteById(id);
        logger.info("Exclusão solicitada para Assunto id={}", id);
    }
}
