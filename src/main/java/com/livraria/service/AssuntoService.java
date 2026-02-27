package com.livraria.service;

import com.livraria.entity.Assunto;
import java.util.List;

public interface AssuntoService {
    List<Assunto> listarTodos();
    Assunto buscarPorId(Integer id);
    Assunto salvar(Assunto assunto);
    void excluir(Integer id);
}
