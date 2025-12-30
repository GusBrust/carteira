@echo off
REM Script launcher para Windows - App Carteira
REM Este script pode ser usado como executável ou atalho

REM Obter o diretório do script
set "SCRIPT_DIR=%~dp0"
set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"

REM Obter o diretório raiz do projeto (um nível acima de Scripts/)
for %%I in ("%SCRIPT_DIR%") do set "PROJECT_ROOT=%%~dpI"
cd /d "%PROJECT_ROOT%"

echo === App Carteira - Windows ===
echo.

REM Verificar se JavaFX está instalado
set "JAVAFX_PATH="

if defined JAVAFX_HOME (
    if exist "%JAVAFX_HOME%\lib" (
        set "JAVAFX_PATH=%JAVAFX_HOME%\lib"
    )
)

if not defined JAVAFX_PATH (
    if exist "C:\Program Files\Java\javafx-sdk-21\lib" (
        set "JAVAFX_PATH=C:\Program Files\Java\javafx-sdk-21\lib"
    ) else if exist "C:\javafx-sdk-21\lib" (
        set "JAVAFX_PATH=C:\javafx-sdk-21\lib"
    ) else if exist "%USERPROFILE%\javafx-sdk-21\lib" (
        set "JAVAFX_PATH=%USERPROFILE%\javafx-sdk-21\lib"
    )
)

if not defined JAVAFX_PATH (
    echo [ERRO] JavaFX nao encontrado!
    echo.
    echo Opcoes:
    echo 1. Instalar JavaFX e definir JAVAFX_HOME
    echo 2. Definir variavel: set JAVAFX_HOME=C:\caminho\para\javafx-sdk-21
    pause
    exit /b 1
)

echo [OK] JavaFX encontrado em: %JAVAFX_PATH%
echo.

REM Verificar se a pasta bin existe e está compilada
if not exist "bin\gui\controllers\Main.class" (
    echo [INFO] Compilando projeto...
    call "%SCRIPT_DIR%\compilar.bat"
    if errorlevel 1 (
        echo [ERRO] Erro na compilacao!
        pause
        exit /b 1
    )
)

REM Executar aplicação
echo [INFO] Executando aplicacao...
echo.

java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp "bin;%JAVAFX_PATH%\*" gui.controllers.Main

if errorlevel 1 (
    echo.
    echo [ERRO] Erro ao executar!
    pause
    exit /b 1
)

