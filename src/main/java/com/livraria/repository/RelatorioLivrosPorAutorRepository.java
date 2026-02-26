package com.livraria.repository;

import com.livraria.entity.RelatorioLivrosPorAutor;
import com.livraria.repository.projection.RelatorioLivrosPorAutorProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RelatorioLivrosPorAutorRepository extends JpaRepository<RelatorioLivrosPorAutor, Integer> {

    @Query(value = "SELECT " +
            "autor_id AS autorId, " +
            "autor_nome AS autorNome, " +
            "livro_id AS livroId, " +
            "livro_titulo AS livroTitulo, " +
            "livro_editora AS livroEditora, " +
            "livro_edicao AS livroEdicao, " +
            "livro_ano_publicacao AS livroAnoPublicacao, " +
            "livro_valor AS livroValor, " +
            "assuntos, " +
            "todos_autores AS todosAutores " + // Alias essencial para casar com a Projection
            "FROM vw_relatorio_livros_por_autor " +
            "ORDER BY autor_nome, livro_titulo", nativeQuery = true)
    List<RelatorioLivrosPorAutorProjection> findAllProjections();
}