# 📋 Makefile - Guia de Uso

Este Makefile simplifica a compilação e execução do projeto **App Carteira**.

## 🚀 Uso Rápido

### Compilar e Executar (Recomendado)
```bash
make run
```

### Apenas Compilar
```bash
make
# ou
make compile
```

### Criar JAR
```bash
make jar
```

### Executar JAR
```bash
make run-jar
```

### Ver Ajuda
```bash
make help
```

---

## 📋 Targets Disponíveis

| Target | Descrição |
|--------|-----------|
| `make` ou `make compile` | Compila o projeto |
| `make run` | Compila e executa a aplicação |
| `make jar` | Cria um JAR executável em `dist/Carteira.jar` |
| `make run-jar` | Executa o JAR criado anteriormente |
| `make clean` | Remove arquivos compilados (`bin/`) |
| `make clean-all` | Remove tudo (`bin/` e `dist/`) |
| `make help` | Mostra ajuda completa |
| `make check-javafx` | Verifica se JavaFX está instalado |
| `make check` | Verifica instalação (Java, JavaC, JavaFX) |
| `make rebuild` | Limpa e recompila o projeto |
| `make full` | Cria JAR e executa |

---

## 🔧 Pré-requisitos

### Java
- **JDK 11+** (recomendado JDK 21)
- Verificar: `java -version` e `javac -version`

### JavaFX
O Makefile detecta automaticamente o JavaFX nos seguintes locais:

1. Variável de ambiente `JAVAFX_HOME`
2. `/usr/lib/javafx/javafx-sdk-21/lib`
3. `/usr/lib/javafx-sdk-21/lib`
4. `/usr/lib/javafx-sdk/lib`
5. `/usr/share/openjfx/lib`
6. `$HOME/javafx-sdk-21/lib`

**Configurar manualmente:**
```bash
export JAVAFX_HOME=/caminho/para/javafx-sdk-21
```

---

## 📝 Exemplos de Uso

### Desenvolvimento Diário
```bash
# Compilar e executar
make run

# Após fazer alterações, recompilar
make rebuild
```

### Criar Distribuição
```bash
# Criar JAR
make jar

# Testar JAR
make run-jar
```

### Limpeza
```bash
# Limpar apenas arquivos compilados
make clean

# Limpar tudo (incluindo JAR)
make clean-all
```

### Verificar Instalação
```bash
# Verificar se tudo está configurado
make check
```

---

## ⚙️ Configuração Avançada

### Variáveis do Makefile

Você pode sobrescrever variáveis ao executar `make`:

```bash
# Especificar caminho do JavaFX
make JAVAFX_HOME=/caminho/custom/javafx-sdk-21 run

# Ou definir variável de ambiente
export JAVAFX_HOME=/caminho/custom/javafx-sdk-21
make run
```

### Estrutura de Diretórios

O Makefile espera a seguinte estrutura:
```
Carteira/
├── Makefile
├── src/
│   ├── model/
│   └── gui/
│       ├── controllers/
│       └── views/
├── bin/          (gerado)
└── dist/         (gerado)
```

---

## 🔍 Troubleshooting

### "JavaFX não encontrado"
```bash
# Verificar se JavaFX está instalado
make check-javafx

# Definir variável de ambiente
export JAVAFX_HOME=/caminho/para/javafx-sdk-21
```

### "Erro na compilação"
```bash
# Verificar instalação completa
make check

# Limpar e recompilar
make clean && make
```

### "JAR não encontrado"
```bash
# Criar JAR primeiro
make jar

# Depois executar
make run-jar
```

---

## 🆚 Comparação com Scripts

| Ação | Script | Makefile |
|------|--------|----------|
| Compilar | `./Scripts/compilar.sh` | `make compile` |
| Executar | `./Scripts/Carteira.sh` | `make run` |
| Criar JAR | `./Scripts/criarJAR.sh` | `make jar` |
| Executar JAR | `./Scripts/executarJAR.sh` | `make run-jar` |
| Limpar | Manual | `make clean` |

**Vantagens do Makefile:**
- ✅ Mais curto e direto
- ✅ Detecta automaticamente arquivos modificados
- ✅ Integração com IDEs
- ✅ Padrão da indústria
- ✅ Cross-platform (com make instalado)

---

## 📚 Recursos Adicionais

- **Scripts Bash**: `Scripts/*.sh` (Linux/Mac)
- **Scripts Batch**: `Scripts/*.bat` (Windows)
- **Documentação**: `Doc/README.md`

---

**Dica:** Use `make help` para ver todas as opções disponíveis!

