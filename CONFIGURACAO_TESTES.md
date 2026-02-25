# Configuração Maven para Testes

## Opções de Execução

### Executar todos os testes
```bash
mvn clean test
```

### Executar com saída detalhada
```bash
mvn clean test -X
```

### Executar e gerar relatório de cobertura
```bash
mvn clean test jacoco:report
# Abra: target/site/jacoco/index.html
```

### Executar testes paralelos (mais rápido)
```bash
mvn test -DparallelTestsEnabled=true -DthreadCount=4
```

### Pular testes na compilação
```bash
mvn clean install -DskipTests
```

### Falhar na primeira falha
```bash
mvn test -DfailIfNoTests=true
```

## Variáveis Úteis

- `maven.test.skip=true` - Pula testes
- `maven.test.failure.ignore=true` - Continua mesmo com falhas
- `test=NomeDaTeste` - Executa apenas um teste
- `test=NomeDaTeste#metodoDeTeste` - Executa um método específico

## Configuração do JaCoCo

O plugin JaCoCo está configurado para:
- Gerar relatório de cobertura automaticamente
- Fase: `test` (após testes)
- Formato: HTML (target/site/jacoco/index.html)

## CI/CD Integration

Para integrar com GitHub Actions, use:
```yaml
- name: Run tests
  run: mvn clean test
  
- name: Generate coverage report
  run: mvn jacoco:report
```
