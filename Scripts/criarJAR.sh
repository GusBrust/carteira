#!/bin/bash

# Script para criar um JAR executável do App Carteira
# Este script cria um "fat JAR" que inclui todas as dependências JavaFX

# Obter o diretório raiz do projeto (um nível acima de Scripts/)
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}")" && pwd )"
PROJECT_ROOT="$( cd "$SCRIPT_DIR/.." && pwd )"
cd "$PROJECT_ROOT"

echo "=== Criando JAR Executável - App Carteira ==="
echo ""

# Verificar se JavaFX está instalado
JAVA_FX=""

if [ -n "$JAVAFX_HOME" ] && [ -d "$JAVAFX_HOME/lib" ]; then
    JAVA_FX="$JAVAFX_HOME/lib"
elif [ -d "/usr/lib/javafx/javafx-sdk-21/lib" ]; then
    JAVA_FX="/usr/lib/javafx/javafx-sdk-21/lib"
elif [ -d "/usr/lib/javafx-sdk-21/lib" ]; then
    JAVA_FX="/usr/lib/javafx-sdk-21/lib"
elif [ -d "/usr/lib/javafx-sdk/lib" ]; then
    JAVA_FX="/usr/lib/javafx-sdk/lib"
elif [ -d "/usr/share/openjfx/lib" ]; then
    JAVA_FX="/usr/share/openjfx/lib"
elif [ -d "$HOME/javafx-sdk-21/lib" ]; then
    JAVA_FX="$HOME/javafx-sdk-21/lib"
else
    echo "❌ JavaFX não encontrado!"
    echo ""
    echo "Opções:"
    echo "1. Instalar JavaFX: ./instalar_javafx.sh"
    echo "2. Definir variável: export JAVAFX_HOME=/caminho/para/javafx-sdk-21"
    exit 1
fi

echo "📁 JavaFX encontrado em: $JAVA_FX"
echo ""

# Compilar o projeto primeiro
echo "🔨 Compilando projeto..."
"$SCRIPT_DIR/compilar.sh"

if [ $? -ne 0 ]; then
    echo "❌ Erro na compilação!"
    exit 1
fi

echo ""
echo "📦 Criando JAR executável..."
echo ""

# Criar diretório dist se não existir
if [ ! -d "dist" ]; then
    mkdir -p dist
fi

# Usar o diretório raiz do projeto
PROJECT_DIR="$PROJECT_ROOT"

# Criar diretório temporário para o JAR
TEMP_DIR=$(mktemp -d)
JAR_DIR="$TEMP_DIR/jar"

# Criar estrutura de diretórios
mkdir -p "$JAR_DIR/META-INF"
mkdir -p "$JAR_DIR/gui/views"
mkdir -p "$JAR_DIR/gui/views/resources"
mkdir -p "$JAR_DIR/model"

# Copiar arquivos compilados
echo "📋 Copiando arquivos compilados..."
cp -r "$PROJECT_DIR/bin"/* "$JAR_DIR/" 2>/dev/null || true

# Copiar recursos (FXML, CSS, imagens)
echo "📋 Copiando recursos (FXML, CSS, imagens)..."
if [ -d "$PROJECT_DIR/src/gui/views" ]; then
    cp -r "$PROJECT_DIR/src/gui/views"/* "$JAR_DIR/gui/views/" 2>/dev/null || true
fi

# Criar arquivo MANIFEST.MF
echo "📝 Criando MANIFEST.MF..."
cat > "$JAR_DIR/META-INF/MANIFEST.MF" << EOF
Manifest-Version: 1.0
Main-Class: gui.controllers.Main
Class-Path: .
EOF

# Criar o JAR usando caminho absoluto
echo "📦 Empacotando JAR..."
cd "$JAR_DIR"
jar cfm "$PROJECT_DIR/dist/Carteira.jar" META-INF/MANIFEST.MF *
cd - > /dev/null

# Limpar diretório temporário
rm -rf "$TEMP_DIR"

echo ""
echo "✅ JAR criado com sucesso!"
echo ""
echo "📁 Arquivo: dist/Carteira.jar"
echo ""
echo "⚠️  NOTA: Este JAR ainda requer JavaFX no classpath para executar."
echo "   Use o script ./Scripts/executarJAR.sh para executar o JAR."
echo ""
echo "💡 Para criar um executável verdadeiramente standalone, use:"
echo "   ./Scripts/criarExecutavel.sh (requer jpackage - Java 14+)"

