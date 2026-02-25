#!/bin/bash
# Script para executar testes da aplicação Livraria (Linux/Mac)

echo ""
echo "========================================"
echo "    TESTES - Livraria App"
echo "========================================"
echo ""

if [ -z "$1" ]; then
    echo "Opções disponíveis:"
    echo ""
    echo "  1 - Executar todos os testes"
    echo "  2 - Executar com cobertura (JaCoCo)"
    echo "  3 - Executar teste específico (LivroServiceImplTest)"
    echo "  4 - Executar teste de Controller"
    echo "  5 - Executar teste de Repository"
    echo "  6 - Listar todos os testes disponíveis"
    echo ""
    read -p "Digite a opção desejada [1-6]: " opcao
else
    opcao=$1
fi

case $opcao in
    1)
        echo ""
        echo "Executando todos os testes..."
        mvn clean test
        ;;
    2)
        echo ""
        echo "Executando testes com relatório JaCoCo..."
        mvn clean test jacoco:report
        echo ""
        echo "Relatório gerado em: target/site/jacoco/index.html"
        ;;
    3)
        echo ""
        echo "Executando testes de LivroServiceImplTest..."
        mvn test -Dtest=LivroServiceImplTest
        ;;
    4)
        echo ""
        echo "Executando testes de LivroControllerTest..."
        mvn test -Dtest=LivroControllerTest
        ;;
    5)
        echo ""
        echo "Executando testes de LivroRepositoryTest..."
        mvn test -Dtest=LivroRepositoryTest
        ;;
    6)
        echo ""
        echo "Listando todos os testes disponíveis..."
        mvn test --dry-run
        ;;
    *)
        echo "Opção inválida!"
        ;;
esac

echo ""
echo "========================================"
echo "    Execução finalizada"
echo "========================================"
echo ""
