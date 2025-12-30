#!/bin/bash

# Script launcher simples para o App Carteira
# Este script pode ser usado como executável ou atalho

# Obter o diretório raiz do projeto (um nível acima de Scripts/)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}")" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"
cd "$PROJECT_ROOT"

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

# Verificar se a pasta bin existe e está compilada
if [ ! -d "bin" ] || [ ! -f "bin/gui/controllers/Main.class" ]; then
    echo "🔨 Compilando projeto..."
    "$SCRIPT_DIR/compilar.sh"
    if [ $? -ne 0 ]; then
        echo "❌ Erro na compilação!"
        exit 1
    fi
fi

# Executar aplicação
java --module-path "${JAVAFX_PATH}" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "bin:${JAVAFX_PATH}/*" \
     gui.controllers.Main

