#!/bin/bash

# Script para testar a GUI do App Carteira
# Ajuste o caminho do JavaFX conforme sua instalação

# Obter o diretório raiz do projeto (um nível acima de Scripts/)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}")" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"
cd "$PROJECT_ROOT"

echo "=== Teste da GUI - App Carteira ==="
echo ""

# Verificar se JavaFX está instalado
# Ajuste este caminho conforme sua instalação
JAVAFX_PATH=""

# Tentar encontrar JavaFX automaticamente
if [ -n "$JAVAFX_HOME" ] && [ -d "$JAVAFX_HOME/lib" ]; then
    JAVAFX_PATH="$JAVAFX_HOME/lib"
elif [ -d "/usr/lib/javafx/javafx-sdk-21/lib" ]; then
    JAVAFX_PATH="/usr/lib/javafx/javafx-sdk-21/lib"
elif [ -d "/usr/lib/javafx-sdk-21/lib" ]; then
    JAVAFX_PATH="/usr/lib/javafx-sdk-21/lib"
elif [ -d "/usr/lib/javafx-sdk/lib" ]; then
    JAVAFX_PATH="/usr/lib/javafx-sdk/lib"
elif [ -d "/usr/share/openjfx/lib" ]; then
    JAVAFX_PATH="/usr/share/openjfx/lib"
elif [ -d "$HOME/javafx-sdk-21/lib" ]; then
    JAVAFX_PATH="$HOME/javafx-sdk-21/lib"
else
    echo "⚠️  JavaFX não encontrado automaticamente."
    echo ""
    echo "Opções:"
    echo "1. Instalar JavaFX (veja INSTALAR_JAVAFX.md)"
    echo "2. Definir variável de ambiente:"
    echo "   export JAVAFX_HOME=/caminho/para/javafx-sdk-21"
    echo "   ./Scripts/testGUI.sh"
    echo "3. Ou ajustar JAVAFX_PATH diretamente no script"
    exit 1
fi

echo "📁 JavaFX encontrado em: $JAVAFX_PATH"
echo ""

# Verificar se a pasta bin existe
if [ ! -d "bin" ]; then
    echo "📦 Criando diretório bin..."
    mkdir -p bin
fi

# Compilar projeto
echo "🔨 Compilando projeto..."
echo ""

# Encontrar todos os arquivos Java e compilar
find src -name "*.java" > /tmp/java_files.txt

javac -d bin \
    -cp "${JAVAFX_PATH}/*:src" \
    @/tmp/java_files.txt 2>&1

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Erro na compilação!"
    rm -f /tmp/java_files.txt
    exit 1
fi

rm -f /tmp/java_files.txt

echo "✅ Compilação concluída!"
echo ""

# Verificar se Main.java da GUI existe
if [ ! -f "src/gui/controllers/Main.java" ]; then
    echo "⚠️  src/gui/controllers/Main.java não encontrado!"
    echo "A GUI ainda não foi integrada. Veja COMO_INTEGRAR.md"
    exit 1
fi

# Executar GUI
echo "🚀 Executando GUI..."
echo ""

java --module-path "${JAVAFX_PATH}" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "bin:${JAVAFX_PATH}/*" \
     gui.controllers.Main

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Erro ao executar!"
    echo ""
    echo "Possíveis soluções:"
    echo "1. Verificar se JavaFX está instalado corretamente"
    echo "2. Ajustar JAVAFX_PATH no script"
    echo "3. Verificar se todos os arquivos foram compilados"
    exit 1
fi

