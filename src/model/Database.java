package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.FileInputStream;

public class Database implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final String PATH = "Dados/";
  private Conta conta;
  private ArrayList<Transacao> transacoes;
  private ArrayList<Orcamento> orcamentos;
  private ArrayList<Categoria> categorias;
  private ArrayList<Divida> dividas;

  public Database() {
    // Cria uma conta padrão se não existir
    this.conta = new Conta("Conta Principal", 0.0);
    this.transacoes = new ArrayList<Transacao>();
    this.orcamentos = new ArrayList<Orcamento>();
    this.categorias = new ArrayList<Categoria>();
    this.dividas = new ArrayList<Divida>();
    inicializarCategoriasPadrao();
  }

  /**
   * Inicializa as categorias padrão do sistema.
   * Estas categorias não podem ser deletadas ou modificadas.
   * São criadas automaticamente se não existirem.
   */
  private void inicializarCategoriasPadrao() {
    // Lista de categorias padrão
    Categoria[] categoriasPadrao = {
        new Categoria("Alimentação", "Gastos com comida e bebida", true),
        new Categoria("Transporte", "Gastos com transporte e combustível", true),
        new Categoria("Saúde", "Gastos com saúde e medicamentos", true),
        new Categoria("Educação", "Gastos com educação e cursos", true),
        new Categoria("Lazer", "Gastos com entretenimento e lazer", true),
        new Categoria("Moradia", "Gastos com aluguel, contas e manutenção", true),
        new Categoria("Salário", "Receita de salário", true),
        new Categoria("Outros", "Outras receitas e despesas", true)
    };

    // Adiciona apenas as categorias padrão que ainda não existem
    for (Categoria categoriaPadrao : categoriasPadrao) {
      boolean existe = false;
      for (Categoria categoriaExistente : this.categorias) {
        if (categoriaExistente.getNome().equalsIgnoreCase(categoriaPadrao.getNome())) {
          existe = true;
          // Se existe mas não está marcada como padrão, atualiza
          if (!categoriaExistente.isPadrao()) {
            // Não podemos modificar diretamente, então removemos e adicionamos a padrão
            // Mas isso só acontece se não for padrão, então vamos apenas garantir
            break;
          }
        }
      }
      if (!existe) {
        this.categorias.add(categoriaPadrao);
      }
    }
  }

  /**
   * Retorna o valor total de todas as transações de uma categoria
   * 
   * @param categoria Categoria a ser verificada
   * @return Valor total das transações da categoria
   */
  public double getValorCategoria(Categoria categoria) {
    double valor = 0;
    for (Transacao transacao : this.transacoes) {
      if (transacao instanceof Despesa && transacao.getCategoria() != null) {
        if (transacao.getCategoria().getNome().equalsIgnoreCase(categoria.getNome())) {
          valor += transacao.getValor();
        }
      }
    }
    return valor;
  }
  

  /**
   * Verifica se já existe uma conta com o mesmo nome
   * 
   * @param nome         Nome da conta a verificar
   * @param contaExcluir Conta a excluir da verificação (útil ao renomear). Pode
   *                     ser null.
   * @return true se já existe uma conta com esse nome, false caso contrário
   */
  /**
   * Obtém a conta única do sistema
   * 
   * @return A conta única
   */
  public Conta getConta() {
    if (this.conta == null) {
      this.conta = new Conta("Conta Principal", 0.0);
    }
    return this.conta;
  }

  /**
   * Define a conta única do sistema
   * 
   * @param conta Conta a ser definida
   */
  public void setConta(Conta conta) {
    if (conta == null) {
      throw new IllegalArgumentException("A conta não pode ser nula.");
    }
    this.conta = conta;
    salvar("contas");
  }

  /**
   * Atualiza a conta e salva automaticamente
   */
  public void atualizarConta() {
    salvar("contas");
  }

  /**
   * Processa uma transação e salva automaticamente
   * Este é o método recomendado para processar transações
   * Atualiza automaticamente os orçamentos relacionados
   * 
   * @param transacao Transação a ser processada
   * @return true se processada com sucesso, false caso contrário
   */
  public boolean processarTransacao(Transacao transacao) {
    if (transacao.processar()) {
      this.transacoes.add(transacao);
      salvar("transacoes");
      // Também salva contas pois o saldo pode ter mudado
      salvar("contas");
      // Atualiza orçamentos relacionados (apenas para despesas)
      atualizarOrcamentoPorTransacao(transacao);
      // Atualiza dívidas relacionadas (apenas para despesas com dividaId)
      atualizarDividaPorTransacao(transacao);
      return true;
    }
    return false;
  }

  public void adicionarTransacao(Transacao transacao) {
    if (this.transacoes.contains(transacao)) {
      return;
    }
    this.transacoes.add(transacao);
    salvar("transacoes");
    salvar("contas");
  }

  public void removerTransacao(Transacao transacao) {
    if (this.transacoes.contains(transacao)) {
      this.transacoes.remove(transacao);
      salvar("transacoes");
      salvar("contas");
    }
  }

  /**
   * Cria um orçamento na base de dados se ele ainda não existir,
   * e salva automaticamente.
   * 
   * @param orcamento Orcamento a ser criado.
   */
  public void criarOuAtualizarOrcamento(Orcamento orcamento) {
    if (orcamento == null) {
      throw new IllegalArgumentException("O orçamento não pode ser nulo.");
    }
    if (!this.orcamentos.contains(orcamento)) {
      this.orcamentos.add(orcamento);
      salvar("orcamentos");
    }
  }

  /**
   * Remove um orçamento do banco de dados
   * 
   * @param orcamento Orçamento a ser removido
   */
  public void removerOrcamento(Orcamento orcamento) {
    if (orcamento != null && this.orcamentos.contains(orcamento)) {
      this.orcamentos.remove(orcamento);
      salvar("orcamentos");
    }
  }

  /**
   * Busca um orçamento pelo ID
   * 
   * @param id ID do orçamento
   * @return O orçamento encontrado ou null se não existir
   */
  public Orcamento buscarOrcamento(String id) {
    if (id == null || this.orcamentos == null) {
      return null;
    }
    for (Orcamento orcamento : this.orcamentos) {
      if (orcamento.getId().equals(id)) {
        return orcamento;
      }
    }
    return null;
  }

  /**
   * Reseta os orçamentos que estão em meses anteriores.
   * Atualiza o período para o mês atual e zera o valor gasto.
   * Deve ser chamado periodicamente (ex: ao carregar a Database).
   */
  public void resetarOrcamentosMensais() {
    LocalDateTime agora = LocalDateTime.now();
    int mesAtual = agora.getMonthValue();
    int anoAtual = agora.getYear();
    boolean houveAlteracao = false;

    for (Orcamento orcamento : this.orcamentos) {
      int mesOrcamento = orcamento.getDataInicio().getMonthValue();
      int anoOrcamento = orcamento.getDataInicio().getYear();

      // Se o orçamento é de um mês/ano anterior, reseta
      if (anoOrcamento < anoAtual ||
          (anoOrcamento == anoAtual && mesOrcamento < mesAtual)) {
        // Reseta o valor gasto
        orcamento.alterarValorGasto(0);

        // Atualiza o período para o mês atual
        LocalDateTime novoInicio = agora.withDayOfMonth(1)
            .withHour(0).withMinute(0).withSecond(0).withNano(0);
        int ultimoDia = agora.toLocalDate().lengthOfMonth();
        LocalDateTime novoFim = agora.withDayOfMonth(ultimoDia)
            .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        // Atualiza as datas (precisa de método setter no Orcamento)
        orcamento.alterarPeriodo(novoInicio, novoFim);
        houveAlteracao = true;
      }
    }

    if (houveAlteracao) {
      salvar("orcamentos");
    }
  }

  /**
   * Recalcula o valor gasto de todos os orçamentos baseado nas transações
   * existentes.
   * Útil ao carregar a Database para garantir que os valores estão corretos.
   */
  public void recalcularValorGastoOrcamentos() {
    for (Orcamento orcamento : this.orcamentos) {
      double valorGastoTotal = 0;

      // Soma todas as despesas da categoria dentro do período do orçamento
      for (Transacao transacao : this.transacoes) {
        if (transacao instanceof Despesa &&
            transacao.getCategoria() != null &&
            transacao.getCategoria().equals(orcamento.getCategoria())) {

          LocalDateTime dataTransacao = transacao.getData();
          boolean dentroDoPeriodo = (dataTransacao.isAfter(orcamento.getDataInicio()) ||
              dataTransacao.isEqual(orcamento.getDataInicio())) &&
              (dataTransacao.isBefore(orcamento.getDataFim()) ||
                  dataTransacao.isEqual(orcamento.getDataFim()));

          if (dentroDoPeriodo) {
            valorGastoTotal += transacao.getValor();
          }
        }
      }

      orcamento.alterarValorGasto(valorGastoTotal);
    }

    salvar("orcamentos");
  }

  /**
   * Atualiza o orçamento relacionado à categoria de uma transação, se existir.
   * Apenas DESPESAS são contabilizadas no orçamento.
   * Verifica se a transação está dentro do período do orçamento.
   * 
   * @param transacao A transação que foi processada
   */
  public void atualizarOrcamentoPorTransacao(Transacao transacao) {
    if (transacao == null || transacao.getCategoria() == null) {
      return;
    }

    // Apenas despesas afetam o orçamento (receitas não)
    if (!(transacao instanceof Despesa)) {
      return;
    }

    LocalDateTime dataTransacao = transacao.getData();

    for (Orcamento orcamento : this.orcamentos) {
      if (orcamento.getCategoria().equals(transacao.getCategoria())) {
        // Verifica se a transação está dentro do período do orçamento
        boolean dentroDoPeriodo = (dataTransacao.isAfter(orcamento.getDataInicio()) ||
            dataTransacao.isEqual(orcamento.getDataInicio())) &&
            (dataTransacao.isBefore(orcamento.getDataFim()) ||
                dataTransacao.isEqual(orcamento.getDataFim()));

        if (dentroDoPeriodo) {
          orcamento.alterarValorGasto(orcamento.getValorGasto() + transacao.getValor());
          salvar("orcamentos");
        }
        break;
      }
    }
  }

  /**
   * Atualiza a dívida relacionada quando uma despesa é processada
   * Apenas funciona para despesas que têm um dividaId associado
   * 
   * @param transacao Transação processada
   */
  public void atualizarDividaPorTransacao(Transacao transacao) {
    if (transacao == null || !(transacao instanceof Despesa)) {
      return;
    }

    Despesa despesa = (Despesa) transacao;
    
    // Verifica se a despesa está relacionada a uma dívida
    if (!despesa.estaRelacionadaADivida()) {
      return;
    }

    String dividaId = despesa.getDividaId();
    Divida divida = buscarDivida(dividaId);

    if (divida != null) {
      try {
        divida.adicionarPagamento(despesa.getValor());
        salvar("dividas");
      } catch (IllegalArgumentException e) {
        // Se o pagamento exceder o total, não atualiza
        System.out.println("Aviso: " + e.getMessage());
      }
    }
  }

  public boolean existeCategoria(Categoria categoria) {
    return this.categorias.contains(categoria);
  }

  public void adicionarCategoria(Categoria categoria) {
    if (this.categorias.contains(categoria)) {
      return;
    }
    this.categorias.add(categoria);
    salvar("categorias");
  }

  /**
   * Remove uma categoria do banco de dados
   * Categorias padrão não podem ser removidas
   * 
   * @param categoria Categoria a ser removida
   * @throws IllegalArgumentException se a categoria for padrão
   */
  public void removerCategoria(Categoria categoria) {
    if (categoria == null) {
      throw new IllegalArgumentException("A categoria não pode ser nula.");
    }
    if (categoria.isPadrao()) {
      throw new IllegalArgumentException("Não é possível remover a categoria padrão '" + categoria.getNome() + "'.");
    }
    if (this.categorias.contains(categoria)) {
      this.categorias.remove(categoria);
      salvar("categorias");
    }
  }

  /**
   * Remove uma categoria pelo nome
   * Categorias padrão não podem ser removidas
   * 
   * @param nome Nome da categoria a ser removida
   * @throws IllegalArgumentException se a categoria for padrão ou não existir
   */
  public void removerCategoriaPorNome(String nome) {
    if (nome == null || nome.trim().isEmpty()) {
      throw new IllegalArgumentException("O nome da categoria não pode ser vazio.");
    }
    Categoria categoria = buscarCategoriaPorNome(nome);
    if (categoria == null) {
      throw new IllegalArgumentException("Categoria '" + nome + "' não encontrada.");
    }
    if (categoria.isPadrao()) {
      throw new IllegalArgumentException("Não é possível remover a categoria padrão '" + categoria.getNome() + "'.");
    }
    removerCategoria(categoria);
  }

  /**
   * Busca uma categoria pelo nome
   * 
   * @param nome Nome da categoria
   * @return Categoria encontrada ou null se não existir
   */
  public Categoria buscarCategoriaPorNome(String nome) {
    if (nome == null || nome.trim().isEmpty()) {
      return null;
    }
    for (Categoria categoria : this.categorias) {
      if (categoria.getNome().equalsIgnoreCase(nome.trim())) {
        return categoria;
      }
    }
    return null;
  }

  /**
   * Retorna uma lista com a única conta (para compatibilidade)
   * 
   * @return Lista contendo a única conta
   */
  public ArrayList<Conta> getContas() {
    ArrayList<Conta> lista = new ArrayList<Conta>();
    if (this.conta != null) {
      lista.add(this.conta);
    }
    return lista;
  }

  public ArrayList<Transacao> getTransacoes() {
    if (this.transacoes.isEmpty()) {
      return new ArrayList<Transacao>();
    }
    return transacoes;
  }

  public ArrayList<Orcamento> getOrcamentos() {
    if (this.orcamentos.isEmpty()) {
      return new ArrayList<Orcamento>();
    }
    return orcamentos;
  }

  public ArrayList<Categoria> getCategorias() {
    if (this.categorias.isEmpty()) {
      return new ArrayList<Categoria>();
    }
    return categorias;
  }

  public ArrayList<Divida> getDividas() {
    if (this.dividas == null) {
      return new ArrayList<Divida>();
    }
    return dividas;
  }

  /**
   * Adiciona uma dívida ao banco de dados
   * 
   * @param divida Dívida a ser adicionada
   */
  public void adicionarDivida(Divida divida) {
    if (divida == null) {
      throw new IllegalArgumentException("A dívida não pode ser nula.");
    }
    if (this.dividas.contains(divida)) {
      return;
    }
    this.dividas.add(divida);
    salvar("dividas");
  }

  /**
   * Remove uma dívida do banco de dados
   * 
   * @param divida Dívida a ser removida
   */
  public void removerDivida(Divida divida) {
    if (divida != null && this.dividas.contains(divida)) {
      this.dividas.remove(divida);
      salvar("dividas");
    }
  }

  /**
   * Busca uma dívida pelo ID
   * 
   * @param id ID da dívida
   * @return A dívida encontrada ou null se não existir
   */
  public Divida buscarDivida(String id) {
    if (id == null || this.dividas == null) {
      return null;
    }
    for (Divida divida : this.dividas) {
      if (divida.getId().equals(id)) {
        return divida;
      }
    }
    return null;
  }

  public String getIdDividaPorNome(String nome) {
    for (Divida divida : this.dividas) {
      if (divida.getNomeEntidade().equalsIgnoreCase(nome)) {
        return divida.getId();
      }
    }
    return "";
  }

  /**
   * Garante que o diretório de dados existe
   */
  private void garantirDiretorio() {
    File diretorio = new File(PATH);
    if (!diretorio.exists()) {
      diretorio.mkdirs();
    }
  }

  /**
   * Método para salvar uma lista específica do banco de dados em um arquivo
   * 
   * @param nome Nome do tipo de dado a salvar (contas, transacoes, orcamentos,
   *             categorias)
   */
  public void salvar(String nome) {
    nome = nome.toLowerCase();
    garantirDiretorio();

    try {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PATH + nome + ".dat"));

      switch (nome) {
        case "contas":
          // Salva como ArrayList para compatibilidade (com apenas uma conta)
          ArrayList<Conta> listaConta = new ArrayList<Conta>();
          if (this.conta != null) {
            listaConta.add(this.conta);
          }
          out.writeObject(listaConta);
          break;
        case "transacoes":
          out.writeObject(this.transacoes);
          break;
        case "orcamentos":
          out.writeObject(this.orcamentos);
          break;
        case "categorias":
          out.writeObject(this.categorias);
          break;
        case "dividas":
          out.writeObject(this.dividas);
          break;
        default:
          System.out.println("Nome de arquivo inválido para salvar: " + nome);
          out.close();
          return;
      }

      out.close();
    } catch (IOException e) {
      System.out.println("Erro ao salvar " + nome + ": " + e.getMessage());
    }
  }

  /**
   * Salva todos os dados do banco de dados de uma vez
   */
  public void salvarTudo() {
    salvar("contas");
    salvar("transacoes");
    salvar("orcamentos");
    salvar("categorias");
    salvar("dividas");
  }

  /**
   * Método para carregar o estado do banco de dados a partir dos arquivos
   * Garante que as categorias padrão sempre existam após o carregamento
   * 
   * @return O objeto Database carregado dos arquivos
   */
  public static Database carregar() {
    Database db = new Database();
    String[] nomes = { "contas", "orcamentos", "categorias", "transacoes", "dividas" };

    for (String nome : nomes) {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PATH + nome + ".dat"))) {
        Object obj = in.readObject();

        switch (nome) {
          case "contas":
            if (obj instanceof ArrayList) {
              @SuppressWarnings("unchecked")
              ArrayList<Conta> contasCarregadas = (ArrayList<Conta>) obj;
              // Pega a primeira conta (ou cria uma padrão se não houver)
              if (!contasCarregadas.isEmpty()) {
                db.conta = contasCarregadas.get(0);
              } else {
                db.conta = new Conta("Conta Principal", 0.0);
              }
            } else if (obj instanceof Conta) {
              // Compatibilidade: se for um objeto Conta diretamente
              db.conta = (Conta) obj;
            } else {
              // Se não conseguir carregar, cria uma conta padrão
              db.conta = new Conta("Conta Principal", 0.0);
            }
            break;
          case "transacoes":
            if (obj instanceof ArrayList) {
              @SuppressWarnings("unchecked")
              ArrayList<Transacao> transacoesCarregadas = (ArrayList<Transacao>) obj;
              db.transacoes = transacoesCarregadas;
            }
            break;
          case "orcamentos":
            if (obj instanceof ArrayList) {
              @SuppressWarnings("unchecked")
              ArrayList<Orcamento> orcamentosCarregados = (ArrayList<Orcamento>) obj;
              db.orcamentos = orcamentosCarregados;
            }
            break;
          case "categorias":
            if (obj instanceof ArrayList) {
              @SuppressWarnings("unchecked")
              ArrayList<Categoria> categoriasCarregadas = (ArrayList<Categoria>) obj;
              db.categorias = categoriasCarregadas;
            }
            break;
          case "dividas":
            if (obj instanceof ArrayList) {
              @SuppressWarnings("unchecked")
              ArrayList<Divida> dividasCarregadas = (ArrayList<Divida>) obj;
              db.dividas = dividasCarregadas;
            }
            break;
        }
      } catch (java.io.FileNotFoundException e) {
        // Arquivo não existe ainda, mantém lista vazia (normal na primeira execução)
        // Não imprime erro para não poluir a saída
      } catch (IOException | ClassNotFoundException e) {
        System.out.println("Erro ao carregar " + nome + ": " + e.getMessage());
      }
    }

    // Garante que as categorias padrão sempre existam após carregar
    db.inicializarCategoriasPadrao();
    // Salva se alguma categoria padrão foi adicionada
    db.salvar("categorias");

    // Reseta orçamentos mensais se necessário
    db.resetarOrcamentosMensais();

    // Recalcula valores gastos baseado nas transações existentes
    db.recalcularValorGastoOrcamentos();

    return db;
  }

  @Override
  public String toString() {
    return "Database{" +
        "conta=" + conta +
        ", transacoes=" + transacoes +
        ", orcamentos=" + orcamentos +
        ", categorias=" + categorias +
        ", dividas=" + dividas +
        '}';
  }
}
