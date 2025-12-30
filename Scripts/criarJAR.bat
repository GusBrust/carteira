@echo off
REM Script para criar um JAR executável do App Carteira no Windows

REM Obter o diretório do script
set "SCRIPT_DIR=%~dp0"
set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"

REM Obter o diretório raiz do projeto (um nível acima de Scripts/)
for %%I in ("%SCRIPT_DIR%") do set "PROJECT_ROOT=%%~dpI"
cd /d "%PROJECT_ROOT%"

echo === Criando JAR Executavel - App Carteira ===
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

REM Compilar o projeto primeiro
echo [INFO] Compilando projeto...
call "%SCRIPT_DIR%\compilar.bat"

if errorlevel 1 (
    echo [ERRO] Erro na compilacao!
    pause
    exit /b 1
)

echo.
echo [INFO] Criando JAR executavel...
echo.

REM Criar diretório dist se não existir
if not exist "dist" mkdir dist

REM Criar diretório temporário para o JAR
set "TEMP_DIR=%TEMP%\carteira_jar_%RANDOM%"
mkdir "%TEMP_DIR%"
set "JAR_DIR=%TEMP_DIR%\jar"

REM Criar estrutura de diretórios
mkdir "%JAR_DIR%\META-INF"
mkdir "%JAR_DIR%\gui\views"
mkdir "%JAR_DIR%\gui\views\resources"
mkdir "%JAR_DIR%\model"

REM Copiar arquivos compilados
echo [INFO] Copiando arquivos compilados...
xcopy /Y /E /I "bin\*" "%JAR_DIR%\" >nul 2>&1

REM Copiar recursos (FXML, CSS, imagens)
echo [INFO] Copiando recursos (FXML, CSS, imagens)...
if exist "src\gui\views" (
    xcopy /Y /E /I "src\gui\views\*" "%JAR_DIR%\gui\views\" >nul 2>&1
)

REM Criar arquivo MANIFEST.MF
echo [INFO] Criando MANIFEST.MF...
(
echo Manifest-Version: 1.0
echo Main-Class: gui.controllers.Main
echo Class-Path: .
) > "%JAR_DIR%\META-INF\MANIFEST.MF"

REM Criar o JAR
echo [INFO] Empacotando JAR...
cd /d "%JAR_DIR%"
jar cfm "%PROJECT_ROOT%\dist\Carteira.jar" META-INF\MANIFEST.MF *
cd /d "%PROJECT_ROOT%"

REM Limpar diretório temporário
rmdir /S /Q "%TEMP_DIR%"

echo.
echo [OK] JAR criado com sucesso!
echo.
echo [INFO] Arquivo: dist\Carteira.jar
echo.
echo [AVISO] Este JAR ainda requer JavaFX no classpath para executar.
echo    Use o script Scripts\executarJAR.bat para executar o JAR.
echo.
echo [INFO] Para criar um executavel verdadeiramente standalone, use:
echo    Scripts\criarExecutavel.bat (requer jpackage - Java 14+)

