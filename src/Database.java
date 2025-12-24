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
  public void adicionarConta(Conta conta) {
    if (this.contas.contains(conta)) {
      return;
    }
    this.contas.add(conta);
    salvar("contas");
  }

  public void removerConta(int id) {
    for (Conta conta : this.contas) {
      if (conta.getId() == id) {
        this.contas.remove(conta);
        salvar("contas");
        return;
      }
    }
    System.out.println("Conta com ID " + id + " não encontrada.");
  }

  public void adicionarTransacao(Transacao transacao) {
    this.transacoes.add(transacao);
    salvar("transacoes");
  }

  public void adicionarOrcamento(Orcamento orcamento) {
    this.orcamentos.add(orcamento);
    salvar("orcamentos");
  }

  public void adicionarCategoria(Categoria categoria) {
    this.categorias.add(categoria);
    salvar("categorias");
  }

  public ArrayList<Conta> getContas() {
    return contas;
  }

  public ArrayList<Transacao> getTransacoes() {
    return transacoes;
  }

  public ArrayList<Orcamento> getOrcamentos() {
    return orcamentos;
  }

  public ArrayList<Categoria> getCategorias() {
    return categorias;
  }

  /**
   * Método para salvar o estado atual do banco de dados em um arquivo
   * @throws IOException Se ocorrer um erro durante a operação de salvamento
   */
  public void salvar(String nome) {
    // Código para salvar o estado atual do banco de dados em um arquivo
    nome = nome.toLowerCase();
    if (nome.equals("contas") || nome.equals("orcamentos") || nome.equals("categorias") || nome.equals("transacoes")) {
      try {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PATH + nome + ".dat"));
        out.writeObject(this);
        out.close();
      } catch (Exception e) {
        System.out.println("Erro ao salvar o database: " + e.getMessage());
      }
    } else {
      System.out.println("Nome de arquivo inválido para salvar o database.");
    }
    
  }

  /**
   * Método para carregar o estado do banco de dados a partir de um arquivo
   * @return O objeto Database carregado do arquivo
   */
  public static Database carregar() {
    Database db = new Database();
    String[] nomes = {"contas", "orcamentos", "categorias", "transacoes"};
    for (String nome : nomes) {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PATH + nome + ".dat"))) {
        Database temp = (Database) in.readObject();
        if (nome.equals("contas")) db.contas = temp.contas;
        if (nome.equals("orcamentos")) db.orcamentos = temp.orcamentos;
        if (nome.equals("categorias")) db.categorias = temp.categorias;
        if (nome.equals("transacoes")) db.transacoes = temp.transacoes;
      } catch (Exception e) {
        // Arquivo não encontrado ou erro ao carregar, ignora e mantém lista vazia
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
