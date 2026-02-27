# Livraria App

## Visão geral
Aplicação web para cadastro e gestão de livros, autores e assuntos, com geração de relatório de livros por autor em HTML e PDF.

## Stack técnica
- Java + Spring Boot
- Spring MVC, Spring Data JPA, Thymeleaf
- PostgreSQL
- JasperReports (relatório PDF)
- Maven

## Estrutura principal
- `src/main/java/com/livraria` — código da aplicação
  - `controller` — controllers MVC
  - `entity` — entidades JPA
  - `repository` — repositórios Spring Data
  - `service` — regras de negócio
  - `exception` — tratamento global de erros
  - `report` — geração de relatórios
- `src/main/resources`
  - `templates` — páginas Thymeleaf
  - `static` — assets (CSS, favicon)
  - `reports` — templates JasperReports (.jrxml)
  - `data.sql` — dados iniciais e view de relatório

## Configuração
Arquivo principal: `src/main/resources/application.yaml`

Configurações necessárias:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

## Banco de dados
- Banco: PostgreSQL
- Modo JPA: `ddl-auto: validate`

### Unicidade de Autor, Assunto e Livro
A aplicação exige unicidade para nomes de autor, descrições de assunto e títulos de livro.
Script SQL (índices únicos case-insensitive): `UNIQUE_CONSTRAINTS.sql`

## Como executar
1. Configure o banco e ajuste `application.yaml`.
2. Execute:
   - Windows: `mvnw.cmd spring-boot:run`
   - Linux/macOS: `./mvnw spring-boot:run`
3. Acesse: `http://localhost:8080`

## Relatórios
- HTML: `/relatorios/livros-por-autor`
- PDF: `/relatorios/livros-por-autor/pdf`

## Tratamento de erros
`GlobalExceptionHandler` centraliza mensagens de erro, incluindo violação de integridade e unicidade.

## Scripts úteis
- `UNIQUE_CONSTRAINTS.sql` — índices únicos no banco
- `testes.bat` / `testes.sh` — execução de testes

## Observações
- O favicon está em `src/main/resources/static/favicon.svg` e referenciado no layout.
