package com.livraria.repository;

import com.livraria.entity.RelatorioLivrosPorAutor;
import com.livraria.repository.projection.RelatorioLivrosPorAutorProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RelatorioLivrosPorAutorRepository extends JpaRepository<RelatorioLivrosPorAutor, Integer> {

    @Query(value = """
    SELECT
        autor_id AS autorId,
        autor_nome AS autorNome,
        STRING_AGG(livro_titulo, ', ') AS livros,
        STRING_AGG(livro_editora, ', ') AS editoras,
        STRING_AGG(livro_ano_publicacao, ', ') AS anosPublicacao,
        STRING_AGG(livro_valor::text, '; ') AS valores,
        STRING_AGG(assuntos, ', ') AS assuntos,
        STRING_AGG(todos_autores, ', ') AS todosAutores
    FROM
        vw_relatorio_livros_por_autor
    GROUP BY
        autor_id,
        autor_nome
    ORDER BY
        autor_nome
    """, nativeQuery = true)
    List<RelatorioLivrosPorAutorProjection> findAllProjections();
}