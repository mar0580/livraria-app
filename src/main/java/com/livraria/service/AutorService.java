package com.livraria.service;

import com.livraria.entity.Autor;
import java.util.List;

public interface AutorService {
    List<Autor> listarTodos();
    Autor buscarPorId(Integer id);
    Autor salvar(Autor autor);
    void excluir(Integer id);
}
