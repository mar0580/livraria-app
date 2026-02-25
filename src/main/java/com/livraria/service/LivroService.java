package com.livraria.service;

import com.livraria.entity.Livro;

import java.util.List;
import java.util.Set;

public interface LivroService {
    List<Livro> listarTodos();
    Livro buscarPorId(Integer id);
    Livro salvar(Livro livro, Set<Integer> idsAutores, Set<Integer> idsAssuntos);
    void excluir(Integer id);
}
