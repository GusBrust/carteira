# Makefile para App Carteira
# Projeto JavaFX - Gerenciamento de Finanças Pessoais

# =============================================================================
# CONFIGURAÇÕES
# =============================================================================

# Diretórios
SRC_DIR = src
BIN_DIR = bin
DIST_DIR = dist
JAR_NAME = Carteira.jar
MAIN_CLASS = gui.controllers.Main

# JavaFX - Detecção automática
JAVAFX_PATH := $(shell \
	if [ -n "$$JAVAFX_HOME" ] && [ -d "$$JAVAFX_HOME/lib" ]; then \
		echo "$$JAVAFX_HOME/lib"; \
	elif [ -d "/usr/lib/javafx/javafx-sdk-21/lib" ]; then \
		echo "/usr/lib/javafx/javafx-sdk-21/lib"; \
	elif [ -d "/usr/lib/javafx-sdk-21/lib" ]; then \
		echo "/usr/lib/javafx-sdk-21/lib"; \
	elif [ -d "/usr/lib/javafx-sdk/lib" ]; then \
		echo "/usr/lib/javafx-sdk/lib"; \
	elif [ -d "/usr/share/openjfx/lib" ]; then \
		echo "/usr/share/openjfx/lib"; \
	elif [ -d "$$HOME/javafx-sdk-21/lib" ]; then \
		echo "$$HOME/javafx-sdk-21/lib"; \
	else \
		echo ""; \
	fi \
)

# Verificar se JavaFX foi encontrado
ifeq ($(JAVAFX_PATH),)
    $(warning ⚠️  JavaFX não encontrado! Defina JAVAFX_HOME ou instale JavaFX)
    $(warning    Exemplo: export JAVAFX_HOME=/caminho/para/javafx-sdk-21)
endif

# JavaFX Modules
JAVAFX_MODULES = javafx.controls,javafx.fxml

# Classpath
CLASSPATH = $(BIN_DIR):$(JAVAFX_PATH)/*

# Compilador Java
JAVAC = javac
JAVA = java
JAR = jar

# Flags de compilação
JAVAC_FLAGS = -d $(BIN_DIR) -cp "$(JAVAFX_PATH)/*:$(SRC_DIR)" -encoding UTF-8

# Flags de execução
JAVA_FLAGS = --module-path "$(JAVAFX_PATH)" --add-modules $(JAVAFX_MODULES) -cp "$(CLASSPATH)"

# =============================================================================
# TARGETS PRINCIPAIS
# =============================================================================

.PHONY: all compile run clean jar help check-javafx

# Target padrão
all: check-javafx compile
	@echo ""
	@echo "✅ Projeto compilado com sucesso!"
	@echo ""
	@echo "🚀 Para executar: make run"
	@echo "📦 Para criar JAR: make jar"

# Verificar JavaFX
check-javafx:
	@if [ -z "$(JAVAFX_PATH)" ]; then \
		echo "❌ Erro: JavaFX não encontrado!"; \
		echo ""; \
		echo "Opções:"; \
		echo "1. Definir variável: export JAVAFX_HOME=/caminho/para/javafx-sdk-21"; \
		echo "2. Instalar JavaFX em um dos locais padrão"; \
		exit 1; \
	else \
		echo "📁 JavaFX encontrado em: $(JAVAFX_PATH)"; \
	fi

# Compilar o projeto
compile: check-javafx
	@echo "=== Compilando Projeto Carteira ==="
	@echo ""
	@mkdir -p $(BIN_DIR)
	@echo "🔨 Compilando arquivos Java..."
	@find $(SRC_DIR) -name "*.java" > /tmp/java_files.txt
	@$(JAVAC) $(JAVAC_FLAGS) @/tmp/java_files.txt || (rm -f /tmp/java_files.txt && exit 1)
	@rm -f /tmp/java_files.txt
	@echo "📋 Copiando recursos (FXML, CSS, imagens)..."
	@if [ -d "$(SRC_DIR)/gui/views" ]; then \
		mkdir -p $(BIN_DIR)/gui/views; \
		cp -r $(SRC_DIR)/gui/views/* $(BIN_DIR)/gui/views/ 2>/dev/null || true; \
	fi
	@echo ""
	@echo "✅ Compilação concluída!"

# Executar a aplicação
run: compile
	@echo ""
	@echo "🚀 Executando aplicação..."
	@echo ""
	@$(JAVA) $(JAVA_FLAGS) $(MAIN_CLASS)

# Criar JAR
jar: compile
	@echo ""
	@echo "=== Criando JAR Executável ==="
	@echo ""
	@mkdir -p $(DIST_DIR)
	@TEMP_DIR=$$(mktemp -d) && \
	JAR_DIR=$$TEMP_DIR/jar && \
	mkdir -p $$JAR_DIR/META-INF $$JAR_DIR/gui/views $$JAR_DIR/gui/views/resources $$JAR_DIR/model && \
	echo "📦 Copiando arquivos compilados..." && \
	cp -r $(BIN_DIR)/* $$JAR_DIR/ && \
	echo "📋 Copiando recursos..." && \
	if [ -d "$(SRC_DIR)/gui/views" ]; then \
		cp -r $(SRC_DIR)/gui/views/* $$JAR_DIR/gui/views/; \
	fi && \
	echo "📝 Criando MANIFEST.MF..." && \
	echo "Manifest-Version: 1.0" > $$JAR_DIR/META-INF/MANIFEST.MF && \
	echo "Main-Class: $(MAIN_CLASS)" >> $$JAR_DIR/META-INF/MANIFEST.MF && \
	echo "Class-Path: ." >> $$JAR_DIR/META-INF/MANIFEST.MF && \
	echo "🔨 Empacotando JAR..." && \
	cd $$JAR_DIR && \
	$(JAR) cfm $(abspath $(DIST_DIR)/$(JAR_NAME)) META-INF/MANIFEST.MF * && \
	cd - > /dev/null && \
	rm -rf $$TEMP_DIR && \
	echo "" && \
	echo "✅ JAR criado com sucesso!" && \
	echo "" && \
	echo "📦 Arquivo: $(DIST_DIR)/$(JAR_NAME)" && \
	echo "" && \
	echo "⚠️  Este JAR ainda requer JavaFX no classpath para executar." && \
	echo "   Use: make run-jar"

# Executar JAR
run-jar: check-javafx
	@if [ ! -f "$(DIST_DIR)/$(JAR_NAME)" ]; then \
		echo "❌ JAR não encontrado!"; \
		echo ""; \
		echo "Execute primeiro: make jar"; \
		exit 1; \
	fi
	@echo ""
	@echo "🚀 Executando JAR..."
	@echo ""
	@$(JAVA) $(JAVA_FLAGS) -cp "$(DIST_DIR)/$(JAR_NAME):$(JAVAFX_PATH)/*" $(MAIN_CLASS)

# Limpar arquivos compilados
clean:
	@echo "🧹 Limpando arquivos compilados..."
	@rm -rf $(BIN_DIR)
	@echo "✅ Limpeza concluída!"

# Limpar tudo (incluindo JAR)
clean-all: clean
	@echo "🧹 Limpando JAR..."
	@rm -rf $(DIST_DIR)
	@echo "✅ Limpeza completa!"

# Ajuda
help:
	@echo "═══════════════════════════════════════════════════════════════"
	@echo "  Makefile - App Carteira"
	@echo "═══════════════════════════════════════════════════════════════"
	@echo ""
	@echo "📋 Targets disponíveis:"
	@echo ""
	@echo "  make              - Compila o projeto (padrão)"
	@echo "  make compile      - Compila o projeto"
	@echo "  make run          - Compila e executa a aplicação"
	@echo "  make jar          - Cria um JAR executável"
	@echo "  make run-jar      - Executa o JAR criado"
	@echo "  make clean        - Remove arquivos compilados (bin/)"
	@echo "  make clean-all    - Remove tudo (bin/ e dist/)"
	@echo "  make help         - Mostra esta ajuda"
	@echo "  make check-javafx - Verifica se JavaFX está instalado"
	@echo ""
	@echo "📝 Exemplos:"
	@echo ""
	@echo "  # Compilar e executar"
	@echo "  make run"
	@echo ""
	@echo "  # Criar JAR"
	@echo "  make jar"
	@echo ""
	@echo "  # Limpar e recompilar"
	@echo "  make clean && make"
	@echo ""
	@echo "🔧 Configuração:"
	@echo ""
	@if [ -z "$(JAVAFX_PATH)" ]; then \
		echo "  ⚠️  JavaFX: NÃO ENCONTRADO"; \
		echo "     Defina: export JAVAFX_HOME=/caminho/para/javafx-sdk-21"; \
	else \
		echo "  ✅ JavaFX: $(JAVAFX_PATH)"; \
	fi
	@echo ""
	@echo "═══════════════════════════════════════════════════════════════"

# =============================================================================
# TARGETS ADICIONAIS
# =============================================================================

# Verificar instalação
check:
	@echo "=== Verificando Instalação ==="
	@echo ""
	@echo "Java:"
	@java -version 2>&1 | head -1 || echo "❌ Java não encontrado"
	@echo ""
	@echo "JavaC:"
	@javac -version 2>&1 || echo "❌ JavaC não encontrado"
	@echo ""
	@echo "JavaFX:"
	@if [ -z "$(JAVAFX_PATH)" ]; then \
		echo "❌ JavaFX não encontrado"; \
	else \
		echo "✅ JavaFX: $(JAVAFX_PATH)"; \
	fi
	@echo ""
	@echo "Estrutura do projeto:"
	@echo "  src/: $$([ -d "$(SRC_DIR)" ] && echo "✅" || echo "❌")"
	@echo "  bin/: $$([ -d "$(BIN_DIR)" ] && echo "✅ (compilado)" || echo "⚠️  (não compilado)")"
	@echo "  dist/: $$([ -d "$(DIST_DIR)" ] && echo "✅" || echo "⚠️  (não criado)")"

# Recompilar (limpar e compilar)
rebuild: clean compile
	@echo ""
	@echo "✅ Recompilação concluída!"

# Compilar e executar JAR
full: jar run-jar

