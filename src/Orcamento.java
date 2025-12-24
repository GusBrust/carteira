public class Orcamento {
    private String nome;
    private double valor;
    private String periodo;
    private Categoria categoria;

    public Orcamento(String nome, double valor, String periodo, Categoria categoria) {
        this.nome = nome;
        this.valor = valor;
        this.periodo = periodo;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public double getValor() {
        return valor;
    }

    public String getPeriodo() {
        return periodo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Orcamento{" +
                "nome='" + nome + '\'' +
                ", valor=" + valor +
                ", periodo='" + periodo + '\'' +
                ", categoria=" + categoria +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Orcamento orcamento = (Orcamento) obj;
        return nome != null ? nome.equals(orcamento.nome) : orcamento.nome == null;
    }

    @Override
    public int hashCode() {
        return nome != null ? nome.hashCode() : 0;
    }
}
