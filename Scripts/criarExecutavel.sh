#!/bin/bash

# Script para criar um executável nativo usando jpackage (Java 14+)
# Este script cria um executável verdadeiramente standalone

# Obter o diretório raiz do projeto (um nível acima de Scripts/)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}")" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"
cd "$PROJECT_ROOT"

echo "=== Criando Executável Nativo - App Carteira ==="
echo ""

# Verificar se jpackage está disponível
if ! command -v jpackage &> /dev/null; then
    echo "❌ jpackage não encontrado!"
    echo ""
    echo "jpackage está disponível no Java 14+ (JDK, não JRE)."
    echo "Verifique se você tem o JDK instalado:"
    echo "  java -version"
    echo "  javac -version"
    echo ""
    echo "Se não tiver, instale o JDK:"
    echo "  sudo apt install openjdk-21-jdk  # Ubuntu/Debian"
    echo "  sudo dnf install java-21-openjdk-devel  # Fedora"
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
exit 1
echo ""


# Criar JAR primeiro
echo ""
echo "📦 Criando JAR..."
"$SCRIPT_DIR/criarJAR.sh"

if [ $? -ne 0 ]; then
    echo "❌ Erro ao criar JAR!"
    exit 1
fi

echo ""
echo "📦 Criando executável nativo..."
echo ""

# Verificar se o JAR foi criado corretamente
JAR_PATH="dist/Carteira.jar"
if [ ! -f "$JAR_PATH" ]; then
    echo "❌ JAR não encontrado em $JAR_PATH!"
    echo "   Verifique se o script criarJAR.sh executou corretamente."
    exit 1
fi

# Criar diretório app se não existir (para o destino do executável)
if [ ! -d "app" ]; then
    mkdir -p app
fi

# Verificar se a pasta do executável já existe e removê-la se necessário
if [ -d "app/Carteira" ]; then
    echo "⚠️  Diretório app/Carteira já existe."
    echo "🗑️  Removendo diretório antigo..."
    rm -rf "app/Carteira"
    echo "✅ Diretório antigo removido."
    echo ""
fi

# Criar executável usando jpackage
echo "🔨 Executando jpackage com --input=$JAR_PATH..."
echo ""


jpackage \
    --name Carteira \
    --input "$JAR_PATH" \
    --main-jar Carteira.jar \
    --main-class gui.controllers.Main \
    --type app-image \
    --dest app \
    --java-options "--modules-path $JAVAFX_HOME/lib --add-modules javafx.controls,javafx.fxml" \

JPACKAGE_EXIT_CODE=$?

# Limpar diretório dist
rm -rf "dist"


 if [ $? -eq 0 ]; then
    echo ""
    echo ""
    echo "✅ Executável criado com sucesso!"
    echo ""
    echo "📁 Executável em: app/Carteira/"
    echo ""
    echo "🚀 Para executar:"
    echo "   ./app/Carteira/bin/Carteira"
    echo ""
    echo "💡 O executável é standalone e não requer Java instalado no sistema."
fi
exit 1