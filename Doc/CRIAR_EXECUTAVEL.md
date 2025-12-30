# Como Criar um Executável do App Carteira

Este guia explica as diferentes formas de criar um executável para o App Carteira.

## Opções Disponíveis

### 1. Script Launcher Simples (Recomendado para uso diário)

O script `Carteira.sh` é um launcher simples que compila e executa o aplicativo automaticamente.

**Uso:**
```bash
./Carteira.sh
```

**Vantagens:**
- Simples e rápido
- Compila automaticamente se necessário
- Funciona em qualquer sistema Linux com Java e JavaFX

**Desvantagens:**
- Requer Java e JavaFX instalados no sistema
- Não é um executável verdadeiramente standalone

**Criar atalho no desktop:**
```bash
# Criar arquivo .desktop
cat > ~/Desktop/Carteira.desktop << EOF
[Desktop Entry]
Version=1.0
Type=Application
Name=Carteira
Comment=Gestão Financeira Pessoal
Exec=/caminho/completo/para/Carteira.sh
Icon=application-x-executable
Terminal=false
Categories=Finance;Office;
EOF

chmod +x ~/Desktop/Carteira.desktop
```

---

### 2. JAR Executável

Cria um arquivo JAR que pode ser executado, mas ainda requer JavaFX no classpath.

**Criar o JAR:**
```bash
./criarJAR.sh
```

**Executar o JAR:**
```bash
./executarJAR.sh
```

**Vantagens:**
- Arquivo único (JAR)
- Mais fácil de distribuir
- Pode ser executado em qualquer sistema com Java e JavaFX

**Desvantagens:**
- Ainda requer JavaFX instalado
- Precisa de script auxiliar para executar

**Arquivo gerado:**
- `dist/Carteira.jar`

---

### 3. Executável Nativo (Standalone)

Cria um executável verdadeiramente standalone que não requer Java instalado no sistema.

**Requisitos:**
- Java 14+ (JDK, não JRE)
- jpackage (incluído no JDK 14+)

**Criar executável:**
```bash
./criarExecutavel.sh
```

**Vantagens:**
- Executável verdadeiramente standalone
- Não requer Java instalado no sistema destino
- Inclui todas as dependências
- Pode ser distribuído como um aplicativo completo

**Desvantagens:**
- Requer JDK 14+ para criar
- Gera arquivos maiores
- Específico para o sistema operacional onde foi criado

**Arquivo gerado:**
- `dist/Carteira/` (diretório com executável e dependências)
- Executável em: `dist/Carteira/bin/Carteira`

**Nota:** O executável criado no Linux só funcionará em sistemas Linux similares. Para Windows ou macOS, você precisaria criar o executável naquele sistema específico.

---

## Estrutura de Diretórios Importante

O aplicativo usa o diretório `Dados/` para salvar os arquivos de dados. Este diretório deve estar no mesmo local que o executável ou JAR.

```
Carteira/
├── Carteira.sh          # Launcher simples
├── Carteira.jar         # JAR executável (em dist/)
├── Carteira/            # Executável nativo (em dist/)
│   └── bin/
│       └── Carteira     # Executável
└── Dados/               # Diretório de dados (criado automaticamente)
    ├── contas.dat
    ├── transacoes.dat
    ├── orcamentos.dat
    ├── categorias.dat
    └── dividas.dat
```

---

## Distribuição

### Para distribuir o aplicativo:

1. **Usando o launcher (Carteira.sh):**
   - Distribua o diretório completo do projeto
   - O usuário precisa ter Java e JavaFX instalados
   - Execute: `./Carteira.sh`

2. **Usando o JAR:**
   - Distribua apenas `dist/Carteira.jar` e `executarJAR.sh`
   - O usuário precisa ter Java e JavaFX instalados
   - Execute: `./executarJAR.sh`

3. **Usando o executável nativo:**
   - Distribua o diretório `dist/Carteira/` completo
   - O usuário não precisa ter Java instalado
   - Execute: `./dist/Carteira/bin/Carteira`

---

## Resolução de Problemas

### "JavaFX não encontrado"
- Instale JavaFX ou defina `JAVAFX_HOME`
- Veja `INSTALAR_JAVAFX.md` para mais detalhes

### "jpackage não encontrado"
- Instale o JDK (não apenas o JRE)
- `sudo apt install openjdk-21-jdk` (Ubuntu/Debian)
- `sudo dnf install java-21-openjdk-devel` (Fedora)

### "Erro ao executar"
- Verifique se o diretório `Dados/` existe e tem permissões de escrita
- Verifique se todos os recursos (FXML, CSS, imagens) foram copiados

---

## Recomendações

- **Para desenvolvimento:** Use `Carteira.sh` ou `./testGUI.sh`
- **Para distribuição simples:** Use o JAR (`criarJAR.sh`)
- **Para distribuição profissional:** Use o executável nativo (`criarExecutavel.sh`)

