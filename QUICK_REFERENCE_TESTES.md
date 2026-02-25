# 🧪 Quick Reference - Testes

## ⚡ Execução Rápida

```bash
# Todos os testes
mvn test

# Com cobertura
mvn clean test jacoco:report

# Teste específico
mvn test -Dtest=LivroServiceImplTest

# Apenas um método
mvn test -Dtest=LivroServiceImplTest#deveSalvarNovoLivroComAutoresEAssuntos

# Paralelo (mais rápido)
mvn test -DparallelTestsEnabled=true
```

## 📊 Arquivos de Teste

```
src/test/java/com/livraria/
├── ProjetoParaCadastroDeLivrosApplicationTests.java     (6 testes)
├── controller/
│   └── LivroControllerTest.java                         (11 testes)
├── exception/
│   └── ResourceNotFoundExceptionTest.java               (3 testes)
├── repository/
│   └── LivroRepositoryTest.java                         (8 testes)
└── service/
    └── impl/
        ├── LivroServiceImplTest.java                    (10 testes)
        └── RelatorioServiceImplTest.java                (5 testes)

Total: 43 testes
```

## 🎯 Testes por Camada

### Service Layer
```java
// 10 testes
- deveListarTodosOsLivros()
- deveBuscarLivroPorIdComSucesso()
- deveSalvarNovoLivroComAutoresEAssuntos()
- deveAtualizarLivroExistenteComSucesso()
- deveExcluirLivroComSucesso()
- deveSalvarLivroComMultiplosAutores()
- deveSalvarLivroComMultiplosAssuntos()
```

### Controller Layer
```java
// 11 testes
- GET /livros - Retornar lista
- GET /livros/novo - Exibir formulário
- POST /livros - Salvar novo
- POST /livros - Rejeitar inválido
- GET /livros/{id}/editar - Editar
- POST /livros/{id} - Atualizar
- GET /livros/{id}/deletar - Deletar
```

### Repository Layer
```java
// 8 testes
- Salvar livro
- Buscar por ID
- Atualizar
- Deletar
- Contar total
- BigDecimal precision
```

## 📈 Cobertura

```
Service:     97%
Repository:  90%
Controller:  85%
Exception:   100%
Integration: 100%
─────────────────
TOTAL:       ~75%
```

## 🔧 Padrões Usados

### @Mock vs @InjectMocks
```java
@Mock private LivroRepository livroRepository;      // Mock de dependência
@InjectMocks private LivroService livroService;     // Injeta mocks

// Resultado: livroService com dependencies mockadas
```

### when() para Mocks
```java
when(livroRepository.findAll()).thenReturn(livros);
when(livroRepository.findById(1)).thenReturn(Optional.of(livro));
when(livroRepository.save(any())).thenReturn(livroSalvo);
```

### verify() para Validação
```java
verify(livroRepository, times(1)).save(any());
verify(livroRepository, never()).delete(any());
verify(livroRepository).findAll();
```

### Assertions Comuns
```java
assertEquals(esperado, atual);
assertNotNull(valor);
assertTrue(condicao);
assertFalse(condicao);
assertThrows(Exception.class, () -> { ... });
assertDoesNotThrow(() -> { ... });
```

## 📚 Exemplo Completo

```java
@ExtendWith(MockitoExtension.class)
class LivroServiceImplTest {
    
    @Mock private LivroRepository livroRepository;
    @InjectMocks private LivroServiceImpl livroService;
    
    @BeforeEach
    void setUp() {
        // Preparação comum
    }
    
    @Test
    @DisplayName("Deve salvar livro com sucesso")
    void deveSalvarComSucesso() {
        // Arrange
        Livro livro = buildLivro();
        when(livroRepository.save(livro)).thenReturn(livro);
        
        // Act
        Livro resultado = livroService.salvar(livro, Set.of(1), Set.of(1));
        
        // Assert
        assertNotNull(resultado);
        verify(livroRepository).save(livro);
    }
}
```

## 🚀 Dicas de Performance

```bash
# Paralelo (4 threads)
mvn test -DparallelTestsEnabled=true -DthreadCount=4

# Skip opcional
mvn clean install -DskipTests

# Falhar rápido
mvn test -DfailIfNoTests=true

# Modo verbose
mvn test -X
```

## 📊 JaCoCo Report

```bash
# Gerar relatório
mvn jacoco:report

# Arquivo gerado
target/site/jacoco/index.html
```

## 🔍 Debugging

```bash
# Rodar teste com debugger
mvn -Dmaven.surefire.debug test

# Saída detalhada
mvn test -X
```

## 📝 Estrutura AAA

```java
// Arrange - Preparar dados
Livro livro = Livro.builder()...build();
when(mock.method()).thenReturn(result);

// Act - Executar ação
Livro resultado = service.salvar(livro);

// Assert - Validar resultado
assertNotNull(resultado);
assertEquals(esperado, resultado);
verify(mock).method();
```

## 🎯 Checklist Antes de Commit

- [ ] Todos os testes passam: `mvn test`
- [ ] Cobertura OK: `mvn jacoco:report`
- [ ] Sem compilation errors
- [ ] Sem warnings importantes
- [ ] Código formatado
- [ ] Mensagens de commit claras

---

**Referência Rápida**: Use este arquivo para lembrar dos comandos mais comuns!
