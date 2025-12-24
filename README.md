# 💰 App Carteira - Sistema de Rastreamento de Finanças

Aplicação Java para monitoramento e controle de finanças pessoais, desenvolvida como trabalho prático de Programação Orientada a Objetos.

## 📋 Descrição

O **App Carteira** é um sistema completo de gestão financeira que permite aos usuários:
- Gerenciar múltiplas contas bancárias
- Registrar receitas, despesas e transferências entre contas
- Organizar transações por categorias
- Definir e monitorar orçamentos por categoria
- Manter persistência de dados entre execuções

## ✨ Funcionalidades

### 💳 Gestão de Contas
- Criar e gerenciar múltiplas contas
- Visualizar saldo de cada conta
- Realizar depósitos e saques
- Transferências entre contas com validação de saldo

### 📊 Transações
- **Receitas**: Registrar entradas de dinheiro com categoria
- **Despesas**: Registrar gastos com validação de saldo e categoria
- **Transferências**: Mover dinheiro entre contas (sem categoria)

### 🏷️ Categorias
- Criar categorias personalizadas para organizar transações
- Categorias padrão e personalizadas
- Associar categorias a receitas e despesas

### 📈 Orçamentos
- Definir orçamentos por categoria
- Estabelecer valores e períodos
- Monitorar gastos em relação aos orçamentos definidos

### 💾 Persistência de Dados
- Salvamento automático em arquivos binários
- Carregamento automático ao iniciar a aplicação
- Dados armazenados em formato serializado

## 🏗️ Arquitetura

### Estrutura de Classes

#### Classes Principais
- **`Conta`**: Representa uma conta bancária com saldo e operações
- **`Transacao`**: Classe abstrata base para todas as transações
- **`Receita`**: Subclasse para transações de entrada de dinheiro
- **`Despesa`**: Subclasse para transações de saída de dinheiro
- **`Transferencia`**: Subclasse para movimentação entre contas
- **`Categoria`**: Organização e classificação de transações
- **`Orcamento`**: Definição de limites de gastos por categoria
- **`Database`**: Gerenciamento de persistência e operações CRUD

### Padrões de Design Utilizados

- **Herança**: Hierarquia de transações (Transacao → Receita/Despesa/Transferencia)
- **Polimorfismo**: Processamento uniforme de diferentes tipos de transação
- **Encapsulamento**: Proteção de dados e validações nas classes
- **Serialização**: Persistência de objetos em arquivos binários

## 📁 Estrutura do Projeto

```
Carteira/
├── src/                    # Código-fonte Java
│   ├── App.java           # Classe principal
│   ├── Conta.java         # Classe de conta bancária
│   ├── Transacao.java     # Classe abstrata de transação
│   ├── Receita.java       # Transação de receita
│   ├── Despesa.java       # Transação de despesa
│   ├── Transferencia.java # Transação de transferência
│   ├── Categoria.java     # Categoria de transação
│   ├── Orcamento.java     # Orçamento por categoria
│   ├── Database.java      # Gerenciamento de dados
│   └── GerirContas.java   # Classe de teste/exemplo
├── Dados/                  # Arquivos de persistência
│   ├── contas.dat
│   ├── transacoes.dat
│   ├── categorias.dat
│   └── orcamentos.dat
└── README.md
```

## 🚀 Como Usar

### Compilação
```bash
javac -d bin src/*.java
```

### Execução
```bash
java -cp bin App
```

### Exemplo de Uso

```java
// Carregar dados existentes
Database db = Database.carregar();

// Criar uma conta
Conta contaCorrente = new Conta("Conta Corrente", 1000.0);
db.adicionarConta(contaCorrente);

// Criar uma categoria
Categoria alimentacao = new Categoria("Alimentação", "Gastos com comida", false);
db.adicionarCategoria(alimentacao);

// Registrar uma despesa
Despesa compra = new Despesa(50.0, "Supermercado", alimentacao, contaCorrente);
if (compra.temSaldoSuficiente()) {
    compra.processar();
    db.adicionarTransacao(compra);
}

// Registrar uma receita
Receita salario = new Receita(2000.0, "Salário mensal", alimentacao, contaCorrente);
salario.processar();
db.adicionarTransacao(salario);

// Transferir entre contas
Conta poupanca = new Conta("Poupança", 500.0);
db.adicionarConta(poupanca);
Transferencia transferencia = new Transferencia(200.0, "Transferência", contaCorrente, poupanca);
transferencia.processar();
db.adicionarTransacao(transferencia);
```

## 🔒 Validações Implementadas

- ✅ Validação de valores positivos em transações
- ✅ Verificação de saldo suficiente para despesas e transferências
- ✅ Prevenção de transferências para a mesma conta
- ✅ Validação de contas não nulas
- ✅ Tratamento de exceções em operações financeiras

## 📝 Notas Técnicas

- **Linguagem**: Java
- **Persistência**: Serialização Java (ObjectOutputStream/ObjectInputStream)
- **Formato de Dados**: Arquivos binários (.dat)
- **Padrão de Dados**: Uso de `double` para valores monetários

## 👨‍💻 Desenvolvimento

Projeto desenvolvido como trabalho prático de **Programação Orientada a Objetos**, demonstrando:
- Conceitos de herança e polimorfismo
- Encapsulamento e abstração
- Persistência de dados
- Tratamento de exceções
- Boas práticas de programação orientada a objetos

---

**Desenvolvido para fins educacionais** 📚
