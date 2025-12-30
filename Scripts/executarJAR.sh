#!/bin/bash

# Script para executar o JAR do App Carteira

# Obter o diretório raiz do projeto (um nível acima de Scripts/)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}")" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"
cd "$PROJECT_ROOT"

echo "=== Executando App Carteira ==="
echo ""

# Verificar se o JAR existe
if [ ! -f "dist/Carteira.jar" ]; then
    echo "❌ JAR não encontrado!"
    echo ""
    echo "Execute primeiro: ./Scripts/criarJAR.sh"
    exit 1
fi

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
echo "🚀 Executando aplicação..."
echo ""

# Executar o JAR
java --module-path "${JAVAFX_PATH}" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "dist/Carteira.jar:${JAVAFX_PATH}/*" \
     gui.controllers.Main

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Erro ao executar!"
    exit 1
fi

