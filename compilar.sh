#!/bin/bash

# Script para compilar o projeto com JavaFX
# Ajuste o caminho do JavaFX conforme sua instalação

echo "=== Compilando Projeto Carteira ==="
echo ""

# Verificar se JavaFX está instalado
JAVAFX_PATH=""

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
    echo "❌ JavaFX não encontrado!"
    echo ""
    echo "Opções:"
    echo "1. Instalar JavaFX: ./instalar_javafx.sh"
    echo "2. Definir variável: export JAVAFX_HOME=/caminho/para/javafx-sdk-21"
    exit 1
fi

echo "📁 JavaFX encontrado em: $JAVAFX_PATH"
echo ""

# Criar diretório bin se não existir
if [ ! -d "bin" ]; then
    echo "📦 Criando diretório bin..."
    mkdir -p bin
fi

# Compilar todos os arquivos Java
echo "🔨 Compilando todos os arquivos Java..."
echo ""

# Encontrar todos os arquivos Java e compilar
find src -name "*.java" > /tmp/java_files.txt

javac -d bin \
    -cp "${JAVAFX_PATH}/*:src" \
    @/tmp/java_files.txt

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Erro na compilação!"
    rm -f /tmp/java_files.txt
    exit 1
fi

rm -f /tmp/java_files.txt

# Copiar recursos (FXML, CSS, imagens) para bin
echo "📋 Copiando recursos (FXML, CSS, imagens)..."
if [ -d "src/gui/views" ]; then
    mkdir -p bin/gui/views
    cp -r src/gui/views/* bin/gui/views/ 2>/dev/null || true
fi

echo ""
echo "✅ Compilação concluída com sucesso!"
echo ""
echo "📁 Arquivos compilados em: bin/"
echo ""
echo "🚀 Para executar a GUI:"
echo "   ./testGUI.sh"
echo ""
echo "   Ou manualmente:"
echo "   java --module-path ${JAVAFX_PATH} --add-modules javafx.controls,javafx.fxml -cp bin:${JAVAFX_PATH}/* gui.controllers.Main"

