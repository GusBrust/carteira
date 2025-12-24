public class Orcamento {
    private String nome;
    private Double valor;
    private String periodo;
    private Categoria categoria;

    public Orcamento(String nome, Double valor, String periodo, Categoria categoria) {
        this.nome = nome;
        this.valor = valor;
        this.periodo = periodo;
        this.categoria = categoria;
    }

}
