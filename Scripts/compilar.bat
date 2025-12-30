@echo off
REM Script para compilar o projeto com JavaFX no Windows

REM Obter o diretório do script
set "SCRIPT_DIR=%~dp0"
set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"

REM Obter o diretório raiz do projeto (um nível acima de Scripts/)
for %%I in ("%SCRIPT_DIR%") do set "PROJECT_ROOT=%%~dpI"
cd /d "%PROJECT_ROOT%"

echo === Compilando Projeto Carteira ===
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

REM Criar diretório bin se não existir
if not exist "bin" (
    echo [INFO] Criando diretorio bin...
    mkdir bin
)

REM Compilar todos os arquivos Java
echo [INFO] Compilando todos os arquivos Java...
echo.

REM Criar lista temporária de arquivos Java
set "TEMP_FILE=%TEMP%\java_files_%RANDOM%.txt"
dir /s /b src\*.java > "%TEMP_FILE%"

REM Compilar todos os arquivos Java
javac -d bin -cp "%JAVAFX_PATH%\*;src" @%TEMP_FILE%

if errorlevel 1 (
    del "%TEMP_FILE%" 2>nul
    echo.
    echo [ERRO] Erro na compilacao!
    pause
    exit /b 1
)

del "%TEMP_FILE%" 2>nul

if errorlevel 1 (
    echo.
    echo [ERRO] Erro na compilacao!
    pause
    exit /b 1
)

REM Copiar recursos (FXML, CSS, imagens) para bin
echo [INFO] Copiando recursos (FXML, CSS, imagens)...
if exist "src\gui\views" (
    if not exist "bin\gui\views" mkdir bin\gui\views
    xcopy /Y /E /I "src\gui\views\*" "bin\gui\views\" >nul 2>&1
)

echo.
echo [OK] Compilacao concluida com sucesso!
echo.
echo [INFO] Arquivos compilados em: bin\
echo.
echo [INFO] Para executar a GUI:
echo    Scripts\Carteira.bat
echo.
echo    Ou manualmente:
echo    java --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.fxml -cp "bin;%JAVAFX_PATH%\*" gui.controllers.Main

