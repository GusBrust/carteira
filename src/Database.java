import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.FileInputStream;

class Database implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final String PATH = "Dados/";
  private ArrayList<Conta> contas;
  private ArrayList<Transacao> transacoes;
  private ArrayList<Orcamento> orcamentos;
  private ArrayList<Categoria> categorias;

  public Database() {
    this.contas = new ArrayList<Conta>();
    this.transacoes = new ArrayList<Transacao>();
    this.orcamentos = new ArrayList<Orcamento>();
    this.categorias = new ArrayList<Categoria>();
  }

  // --------------------------------------------------------------------------
  // Métodos de manipulação
  // --------------------------------------------------------------------------
  /**
   * Verifica se já existe uma conta com o mesmo nome
   * @param nome Nome da conta a verificar
   * @param contaExcluir Conta a excluir da verificação (útil ao renomear). Pode ser null.
   * @return true se já existe uma conta com esse nome, false caso contrário
   */
  public boolean existeContaComNome(String nome, Conta contaExcluir) {
    if (nome == null || nome.trim().isEmpty()) {
      return false;
    }
    for (Conta conta : this.contas) {
      // Ignora a conta que está sendo renomeada
      if (contaExcluir != null && conta.getId().equals(contaExcluir.getId())) {
        continue;
      }
      if (conta.getNome().equalsIgnoreCase(nome.trim())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Verifica se já existe uma conta com o mesmo nome
   * @param nome Nome da conta a verificar
   * @return true se já existe uma conta com esse nome, false caso contrário
   */
  public boolean existeContaComNome(String nome) {
    return existeContaComNome(nome, null);
  }

  /**
   * Atualiza o nome de uma conta, validando se o novo nome já existe
   * @param conta Conta a ser atualizada
   * @param novoNome Novo nome para a conta
   * @throws IllegalArgumentException se já existir uma conta com o novo nome
   */
  public void atualizarNomeConta(Conta conta, String novoNome) {
    if (conta == null) {
      throw new IllegalArgumentException("A conta não pode ser nula.");
    }
    if (novoNome == null || novoNome.trim().isEmpty()) {
      throw new IllegalArgumentException("O nome da conta não pode ser vazio.");
    }
    if (existeContaComNome(novoNome, conta)) {
      throw new IllegalArgumentException("Já existe uma conta com o nome '" + novoNome + "'.");
    }
    conta.setNome(novoNome.trim());
    salvar("contas");
  }

  /**
   * Adiciona uma nova conta ao banco de dados
   * Valida se já existe uma conta com o mesmo nome
   * @param conta Conta a ser adicionada
   * @throws IllegalArgumentException se já existir uma conta com o mesmo nome
   */
  public void adicionarConta(Conta conta) {
    if (conta == null) {
      throw new IllegalArgumentException("A conta não pode ser nula.");
    }
    
    if (existeContaComNome(conta.getNome())) {
      throw new IllegalArgumentException("Já existe uma conta com o nome '" + conta.getNome() + "'.");
    }
    
    this.contas.add(conta);
    salvar("contas");
  }

  /**
   * Remove uma conta pelo ID
   * @param id ID da conta (String UUID)
   */
  public void removerConta(String id) {
    for (Conta conta : this.contas) {
      if (conta.getId().equals(id)) {
        this.contas.remove(conta);
        salvar("contas");
        return;
      }
    }
    System.out.println("Conta com ID " + id + " não encontrada.");
  }

  /**
   * Busca uma conta pelo ID
   * @param id ID da conta (String UUID)
   * @return Conta encontrada ou null se não existir
   */
  public Conta buscarConta(String id) {
    for (Conta conta : this.contas) {
      if (conta.getId().equals(id)) {
        return conta;
      }
    }
    return null;
  }

  /**
   * Busca uma conta pelo nome
   * @param nome Nome da conta
   * @return Conta encontrada ou null se não existir
   */
  public Conta buscarContaPorNome(String nome) {
    if (nome == null || nome.trim().isEmpty()) {
      return null;
    }
    for (Conta conta : this.contas) {
      if (conta.getNome().equalsIgnoreCase(nome.trim())) {
        return conta;
      }
    }
    return null;
  }

  /**
   * Atualiza o saldo de uma conta e salva automaticamente
   * Use este método quando modificar o saldo de uma conta diretamente
   * @param id ID da conta (String UUID)
   */
  public void atualizarConta(String id) {
    if (buscarConta(id) != null) {
      salvar("contas");
    }
  }

  /**
   * Processa uma transação e salva automaticamente
   * Este é o método recomendado para processar transações
   * @param transacao Transação a ser processada
   * @return true se processada com sucesso, false caso contrário
   */
  public boolean processarTransacao(Transacao transacao) {
    if (transacao.processar()) {
      this.transacoes.add(transacao);
      salvar("transacoes");
      // Também salva contas pois o saldo pode ter mudado
      salvar("contas");
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

  public void adicionarOrcamento(Orcamento orcamento) {
    if (this.orcamentos.contains(orcamento)) {
      return;
    }
    this.orcamentos.add(orcamento);
    salvar("orcamentos");
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

  public void removerCategoria(Categoria categoria) {
    if (this.categorias.contains(categoria)) {
      this.categorias.remove(categoria);
      salvar("categorias");
    }
  }

  public ArrayList<Conta> getContas() {
    if (this.contas.isEmpty()) {
      return new ArrayList<Conta>();
    }
    return contas;
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
   * @param nome Nome do tipo de dado a salvar (contas, transacoes, orcamentos, categorias)
   */
  public void salvar(String nome) {
    nome = nome.toLowerCase();
    garantirDiretorio();
    
    try {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PATH + nome + ".dat"));
      
      switch (nome) {
        case "contas":
          out.writeObject(this.contas);
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
  }

  /**
   * Método para carregar o estado do banco de dados a partir dos arquivos
   * @return O objeto Database carregado dos arquivos
   */
  public static Database carregar() {
    Database db = new Database();
    String[] nomes = {"contas", "orcamentos", "categorias", "transacoes"};
    
    for (String nome : nomes) {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PATH + nome + ".dat"))) {
        Object obj = in.readObject();
        
        switch (nome) {
          case "contas":
            if (obj instanceof ArrayList) {
              @SuppressWarnings("unchecked")
              ArrayList<Conta> contasCarregadas = (ArrayList<Conta>) obj;
              db.contas = contasCarregadas;
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
        }
      } catch (java.io.FileNotFoundException e) {
        // Arquivo não existe ainda, mantém lista vazia (normal na primeira execução)
        // Não imprime erro para não poluir a saída
      } catch (IOException | ClassNotFoundException e) {
        System.out.println("Erro ao carregar " + nome + ": " + e.getMessage());
      }
    }
    
    return db;
  }

  @Override
  public String toString() {
    return "Database{" +
        "contas=" + contas +
        ", transacoes=" + transacoes +
        ", orcamentos=" + orcamentos +
        ", categorias=" + categorias +
        '}';
  }

}
