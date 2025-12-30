# 🪟 Scripts para Windows - App Carteira

Este guia explica como usar os scripts batch (.bat) para Windows.

## 📋 Scripts Disponíveis

### 1. **Carteira.bat** - Launcher Principal (Recomendado)
Script principal para executar o aplicativo.

**Uso:**
```cmd
Scripts\Carteira.bat
```

**Funcionalidades:**
- Detecta automaticamente o JavaFX instalado
- Compila o projeto se necessário
- Executa a aplicação

**Duplo clique:** Você pode criar um atalho no desktop apontando para este arquivo.

---

### 2. **compilar.bat** - Compilar o Projeto
Compila todos os arquivos Java do projeto.

**Uso:**
```cmd
Scripts\compilar.bat
```

**Funcionalidades:**
- Compila todos os arquivos `.java` do projeto
- Copia recursos (FXML, CSS, imagens) para `bin/`
- Detecta automaticamente o JavaFX

---

### 3. **criarJAR.bat** - Criar JAR Executável
Cria um arquivo JAR do aplicativo.

**Uso:**
```cmd
Scripts\criarJAR.bat
```

**Funcionalidades:**
- Compila o projeto automaticamente
- Cria um JAR em `dist\Carteira.jar`
- Inclui todos os recursos necessários

**Arquivo gerado:**
- `dist\Carteira.jar`

---

### 4. **executarJAR.bat** - Executar o JAR
Executa o JAR criado anteriormente.

**Uso:**
```cmd
Scripts\executarJAR.bat
```

**Requisitos:**
- O JAR deve ter sido criado primeiro (`criarJAR.bat`)
- JavaFX deve estar instalado

---

## 🔧 Pré-requisitos

### Java
- **JDK 11+** (recomendado JDK 21)
- Verificar instalação: `java -version`

### JavaFX
O JavaFX pode ser instalado em vários locais. Os scripts procuram automaticamente em:

1. Variável de ambiente `JAVAFX_HOME`
2. `C:\Program Files\Java\javafx-sdk-21\lib`
3. `C:\javafx-sdk-21\lib`
4. `%USERPROFILE%\javafx-sdk-21\lib`

**Configurar JavaFX manualmente:**
```cmd
set JAVAFX_HOME=C:\caminho\para\javafx-sdk-21
```

**Baixar JavaFX:**
1. Acesse: https://openjfx.io/
2. Baixe o JavaFX SDK 21 para Windows
3. Extraia para um diretório (ex: `C:\javafx-sdk-21`)
4. Defina `JAVAFX_HOME` ou coloque em um dos locais padrão

---

## 🚀 Como Usar

### Opção 1: Executar Diretamente (Recomendado)
```cmd
cd C:\caminho\para\Carteira
Scripts\Carteira.bat
```

### Opção 2: Criar Atalho no Desktop

1. Clique com botão direito no arquivo `Scripts\Carteira.bat`
2. Selecione "Criar atalho"
3. Arraste o atalho para o desktop
4. (Opcional) Renomeie para "Carteira"

### Opção 3: Executar JAR
```cmd
cd C:\caminho\para\Carteira
Scripts\criarJAR.bat
Scripts\executarJAR.bat
```

---

## ⚠️ Problemas Comuns

### "JavaFX não encontrado"
**Solução:**
1. Instale o JavaFX SDK
2. Defina a variável de ambiente:
   ```cmd
   setx JAVAFX_HOME "C:\javafx-sdk-21"
   ```
3. Reinicie o prompt de comando

### "java não é reconhecido como comando"
**Solução:**
1. Instale o JDK
2. Adicione Java ao PATH do sistema
3. Reinicie o prompt de comando

### "Erro ao compilar"
**Solução:**
1. Verifique se o JDK está instalado: `javac -version`
2. Verifique se o JavaFX está no caminho correto
3. Verifique se há erros de sintaxe no código

### "Erro ao executar"
**Solução:**
1. Verifique se o projeto foi compilado: `dir bin\gui\controllers\Main.class`
2. Verifique se o JavaFX está instalado corretamente
3. Verifique se há erros no console

---

## 📝 Notas

- Os scripts usam caminhos relativos, então devem ser executados a partir da raiz do projeto
- Todos os scripts mudam automaticamente para o diretório raiz do projeto
- Os dados são salvos em `Dados\` (criado automaticamente)
- Os arquivos compilados ficam em `bin\`
- Os JARs ficam em `dist\`

---

## 🔄 Equivalência com Scripts Linux

| Linux | Windows |
|-------|---------|
| `Carteira.sh` | `Carteira.bat` |
| `compilar.sh` | `compilar.bat` |
| `criarJAR.sh` | `criarJAR.bat` |
| `executarJAR.sh` | `executarJAR.bat` |

---

**Desenvolvido para Windows** 🪟

