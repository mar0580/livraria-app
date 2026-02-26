-- Inserir alguns autores padrão
INSERT INTO autor (nome) VALUES ('Machado de Assis');
INSERT INTO autor (nome) VALUES ('Clarice Lispector');
INSERT INTO autor (nome) VALUES ('Paulo Coelho');
INSERT INTO autor (nome) VALUES ('Jorge Amado');
INSERT INTO autor (nome) VALUES ('Carlos Drummond de Andrade');

-- Inserir alguns assuntos padrão
INSERT INTO assunto (descricao) VALUES ('Romance');
INSERT INTO assunto (descricao) VALUES ('Ficção');
INSERT INTO assunto (descricao) VALUES ('Drama');
INSERT INTO assunto (descricao) VALUES ('Aventura');
INSERT INTO assunto (descricao) VALUES ('Poesia');
INSERT INTO assunto (descricao) VALUES ('Suspense');
INSERT INTO assunto (descricao) VALUES ('Fantasia');
INSERT INTO assunto (descricao) VALUES ('Biografia');

-- Criar VIEW que agrega dados de livro, autor e assunto para relatório
CREATE VIEW vw_relatorio_livros_por_autor AS
SELECT
    l.id AS livro_id,
    a.id AS autor_id,
    a.nome AS autor_nome,
    l.titulo AS livro_titulo,
    l.editora AS livro_editora,
    l.edicao AS livro_edicao,
    l.ano_publicacao AS livro_ano_publicacao,
    l.valor AS livro_valor,
    COALESCE(STRING_AGG(DISTINCT ass.descricao, ', ' ORDER BY ass.descricao), '') AS assuntos
FROM livro l
INNER JOIN livro_autor la ON l.id = la.livro_id
INNER JOIN autor a ON la.autor_id = a.id
LEFT JOIN livro_assunto las ON l.id = las.livro_id
LEFT JOIN assunto ass ON las.assunto_id = ass.id
GROUP BY l.id, a.id, a.nome, l.titulo, l.editora, l.edicao, l.ano_publicacao, l.valor
ORDER BY a.nome, l.titulo;
