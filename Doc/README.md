# 💰 App Carteira - Sistema de Gestão Financeira Pessoal

Aplicação Java com interface gráfica (JavaFX) para monitoramento e controle de finanças pessoais, desenvolvida como trabalho prático de Programação Orientada a Objetos.

## 📋 Descrição

O **App Carteira** é um sistema completo de gestão financeira com interface gráfica moderna que permite aos usuários:
- Gerenciar uma conta bancária principal
- Registrar receitas e despesas com categorias
- Organizar transações por categorias personalizadas
- Definir e monitorar orçamentos por categoria
- Gerenciar dívidas e pagamentos
- Configurar despesas fixas (recorrentes)
- Visualizar gráficos de despesas e receitas
- Manter persistência de dados entre execuções

## ✨ Funcionalidades

### 💳 Gestão de Conta
- **Conta única**: Sistema simplificado com uma conta principal
- Visualizar saldo atual em tempo real
- Editar nome da conta
- Permitir saldo negativo (para flexibilidade)

### 📊 Transações
- **Receitas**: Registrar entradas de dinheiro com categoria, data e hora
- **Despesas**: Registrar gastos com categoria, data e hora
- **Despesas Fixas**: Configurar despesas recorrentes (diárias, semanais, mensais, trimestrais, semestrais ou anuais)
- Visualizar histórico completo de transações
- Remover transações (com reversão automática do saldo)
- Filtros e visualização por tipo

### 🏷️ Categorias
- Criar categorias personalizadas diretamente na interface
- Categorias padrão do sistema (não podem ser removidas)
- Associar categorias a receitas e despesas
- Organizar transações por categoria

### 📈 Orçamentos
- Definir orçamentos por categoria
- Estabelecer valores e períodos (mensais)
- Monitorar gastos em relação aos orçamentos definidos
- Visualizar progresso com barras de progresso
- Remover orçamentos

### 💳 Dívidas
- Registrar dívidas com entidade, descrição e valor total
- Adicionar pagamentos parciais
- Acompanhar valor restante a pagar
- Marcar dívidas como pagas
- Remover dívidas

### 📊 Visualizações e Gráficos
- **Dashboard**: Visão geral com saldo atual e gráficos
- **Gráfico de Pizza**: Distribuição de despesas por categoria
- **Gráfico de Barras**: Comparação mensal de receitas e despesas (mês atual)
- Visualização clara e intuitiva dos dados financeiros

### 💾 Persistência de Dados
- Salvamento automático em arquivos binários
- Carregamento automático ao iniciar a aplicação
- Dados armazenados em formato serializado (.dat)
- Diretório `Dados/` criado automaticamente

## 🏗️ Arquitetura

### Estrutura de Classes

#### Classes do Modelo (`src/model/`)
- **`Conta`**: Representa uma conta bancária com saldo e operações
- **`Transacao`**: Classe abstrata base para todas as transações
- **`Receita`**: Subclasse para transações de entrada de dinheiro
- **`Despesa`**: Subclasse para transações de saída de dinheiro (com suporte a despesas fixas)
- **`Divida`**: Representa dívidas com pagamentos parciais
- **`Categoria`**: Organização e classificação de transações
- **`Orcamento`**: Definição de limites de gastos por categoria
- **`Database`**: Gerenciamento de persistência e operações CRUD

#### Classes da Interface (`src/gui/`)
- **`Main`**: Classe principal da aplicação JavaFX
- **`InterfaceController`**: Controlador do dashboard principal
- **`adicionarController`**: Controlador para adicionar transações
- **`transacoesController`**: Controlador para visualizar transações
- **`orcamentosController`**: Controlador para gerenciar orçamentos
- **`dividasController`**: Controlador para gerenciar dívidas

### Padrões de Design Utilizados

- **Herança**: Hierarquia de transações (Transacao → Receita/Despesa)
- **Polimorfismo**: Processamento uniforme de diferentes tipos de transação
- **Encapsulamento**: Proteção de dados e validações nas classes
- **Serialização**: Persistência de objetos em arquivos binários
- **MVC (Model-View-Controller)**: Separação entre modelo de dados e interface

## 📁 Estrutura do Projeto

```
Carteira/
├── src/                          # Código-fonte
│   ├── model/                    # Classes do modelo de dados
│   │   ├── Categoria.java
│   │   ├── Conta.java
│   │   ├── Database.java
│   │   ├── Despesa.java
│   │   ├── Divida.java
│   │   ├── Orcamento.java
│   │   ├── Receita.java
│   │   ├── Transacao.java
│   │   └── Transferencia.java   # (não utilizada na UI)
│   └── gui/                      # Interface gráfica
│       ├── controllers/          # Controladores JavaFX
│       │   ├── Main.java
│       │   ├── InterfaceController.java
│       │   ├── adicionarController.java
│       │   ├── dividasController.java
│       │   ├── orcamentosController.java
│       │   └── transacoesController.java
│       └── views/                # Arquivos FXML e recursos
│           ├── Interface.fxml
│           ├── adicionar.fxml
│           ├── dividas.fxml
│           ├── orcamentos.fxml
│           ├── transacoes.fxml
│           ├── application.css
│           └── resources/        # Imagens
│               ├── dashboard.png
│               ├── adicionar.png
│               ├── dividas.png
│               ├── orcamentos.png
│               └── transacoes.png
├── Dados/                        # Arquivos de persistência
│   ├── categorias.dat
│   ├── contas.dat
│   ├── dividas.dat
│   ├── orcamentos.dat
│   └── transacoes.dat
├── Scripts/                      # Scripts de build e execução
│   ├── compilar.sh              # Compila o projeto
│   ├── testGUI.sh               # Testa a GUI
│   ├── criarJAR.sh              # Cria JAR executável
│   ├── executarJAR.sh           # Executa o JAR
│   ├── criarExecutavel.sh       # Cria executável nativo
│   └── Carteira.sh              # Launcher simples
├── Doc/                          # Documentação
│   ├── README.md                # Este arquivo
│   └── CRIAR_EXECUTAVEL.md      # Guia para criar executáveis
└── bin/                          # Arquivos compilados (gerado)
```

## 🚀 Como Usar

### Pré-requisitos

- **Java 11+** (JDK recomendado)
- **JavaFX 21** (ou versão compatível)
- **Linux** (para os scripts de build)

### Compilação e Execução

#### Opção 1: Launcher Simples (Recomendado)
```bash
./Scripts/Carteira.sh
```

#### Opção 2: Compilar e Executar Manualmente
```bash
# Compilar
./Scripts/compilar.sh

# Executar
./Scripts/testGUI.sh
```

#### Opção 3: JAR Executável
```bash
# Criar JAR
./Scripts/criarJAR.sh

# Executar JAR
./Scripts/executarJAR.sh
```

#### Opção 4: Executável Nativo (Standalone)
```bash
# Criar executável (requer JDK 14+ e jpackage)
./Scripts/criarExecutavel.sh

# Executar
./dist/Carteira/bin/Carteira
```

### Navegação na Interface

A aplicação possui uma interface gráfica com as seguintes páginas:

1. **Dashboard** (`Interface.fxml`): Visão geral com saldo, gráficos e resumo
2. **Transações** (`transacoes.fxml`): Lista completa de transações
3. **Adicionar** (`adicionar.fxml`): Adicionar novas receitas ou despesas
4. **Orçamentos** (`orcamentos.fxml`): Gerenciar orçamentos por categoria
5. **Dívidas** (`dividas.fxml`): Gerenciar dívidas e pagamentos

Use os botões na parte inferior da tela para navegar entre as páginas.

## 🔒 Validações Implementadas

- ✅ Validação de valores positivos em transações
- ✅ Verificação de saldo suficiente para despesas (opcional - permite saldo negativo)
- ✅ Validação de contas não nulas
- ✅ Validação de categorias não nulas
- ✅ Validação de datas e horas
- ✅ Validação de despesas fixas (número de repetições e frequência)
- ✅ Tratamento de exceções em operações financeiras
- ✅ Confirmação antes de remover transações, orçamentos ou dívidas

## 📝 Notas Técnicas

- **Linguagem**: Java
- **Framework GUI**: JavaFX 21
- **Persistência**: Serialização Java (ObjectOutputStream/ObjectInputStream)
- **Formato de Dados**: Arquivos binários (.dat)
- **Padrão de Dados**: Uso de `double` para valores monetários
- **Caminhos de Recursos**: Todos os recursos (FXML, CSS, imagens) usam caminhos absolutos para funcionar corretamente no JAR

## 🎨 Interface Gráfica

A aplicação utiliza JavaFX para fornecer uma interface moderna e intuitiva:

- **Design Responsivo**: Layout adaptável
- **Gráficos Interativos**: Visualização de dados financeiros
- **Navegação Intuitiva**: Botões de navegação na parte inferior
- **Feedback Visual**: Mensagens de sucesso e erro
- **Validação em Tempo Real**: Campos validados durante a digitação

## 👨‍💻 Desenvolvimento

Projeto desenvolvido como trabalho prático de **Programação Orientada a Objetos**, demonstrando:

- Conceitos de herança e polimorfismo
- Encapsulamento e abstração
- Persistência de dados
- Tratamento de exceções
- Desenvolvimento de interfaces gráficas
- Arquitetura MVC
- Boas práticas de programação orientada a objetos

## 📚 Documentação Adicional

- **CRIAR_EXECUTAVEL.md**: Guia completo para criar executáveis do aplicativo
- **ESTRUTURA_PROJETO.md**: Análise detalhada da estrutura do projeto

## 🔧 Resolução de Problemas

### JavaFX não encontrado
- Instale JavaFX ou defina a variável `JAVAFX_HOME`
- Veja os scripts de instalação ou configure manualmente

### Erro ao carregar recursos
- Verifique se os arquivos FXML, CSS e imagens estão no diretório correto
- Todos os recursos devem usar caminhos absolutos começando com `/`

### Erro ao salvar dados
- Verifique permissões de escrita no diretório `Dados/`
- O diretório será criado automaticamente se não existir

---

**Desenvolvido para fins educacionais** 📚
