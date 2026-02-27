-- Verificar duplicidades antes de aplicar os índices (case-insensitive)
-- Se alguma consulta retornar linhas, trate os dados antes de prosseguir.
SELECT LOWER(nome) AS nome_normalizado, COUNT(*)
FROM autor
GROUP BY LOWER(nome)
HAVING COUNT(*) > 1;

SELECT LOWER(descricao) AS descricao_normalizada, COUNT(*)
FROM assunto
GROUP BY LOWER(descricao)
HAVING COUNT(*) > 1;

SELECT LOWER(titulo) AS titulo_normalizado, COUNT(*)
FROM livro
GROUP BY LOWER(titulo)
HAVING COUNT(*) > 1;

-- Criar índices únicos case-insensitive
CREATE UNIQUE INDEX IF NOT EXISTS uq_autor_nome_ci
    ON autor (LOWER(nome));

CREATE UNIQUE INDEX IF NOT EXISTS uq_assunto_descricao_ci
    ON assunto (LOWER(descricao));

CREATE UNIQUE INDEX IF NOT EXISTS uq_livro_titulo_ci
    ON livro (LOWER(titulo));
