@echo off
REM Script para executar testes da aplicação Livraria
REM Uso: testes.bat [opcao]

setlocal enabledelayedexpansion

echo.
echo ========================================
echo    TESTES - Livraria App
echo ========================================
echo.

if "%1"=="" (
    echo Opcoes disponiveis:
    echo.
    echo   1 - Executar todos os testes
    echo   2 - Executar com cobertura (JaCoCo)
    echo   3 - Executar teste especifico (LivroServiceImplTest)
    echo   4 - Executar teste de Controller
    echo   5 - Executar teste de Repository
    echo   6 - Listar todos os testes disponiveis
    echo.
    set /p opcao="Digite a opcao desejada [1-6]: "
) else (
    set opcao=%1
)

if "%opcao%"=="1" (
    echo.
    echo Executando todos os testes...
    call mvn test
)

if "%opcao%"=="2" (
    echo.
    echo Executando testes com relatorio JaCoCo...
    call mvn clean test jacoco:report
    echo.
    echo Relatorio gerado em: target/site/jacoco/index.html
)

if "%opcao%"=="3" (
    echo.
    echo Executando testes de LivroServiceImplTest...
    call mvn test -Dtest=LivroServiceImplTest
)

if "%opcao%"=="4" (
    echo.
    echo Executando testes de LivroControllerTest...
    call mvn test -Dtest=LivroControllerTest
)

if "%opcao%"=="5" (
    echo.
    echo Executando testes de LivroRepositoryTest...
    call mvn test -Dtest=LivroRepositoryTest
)

if "%opcao%"=="6" (
    echo.
    echo Listando todos os testes disponiveis...
    call mvn test --dry-run
)

echo.
echo ========================================
echo    Execucao finalizada
echo ========================================
echo.
