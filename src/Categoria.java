import java.io.Serializable;

public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String descricao;
    private boolean padrao;

    public Categoria(String nome, String descricao, boolean padrao) {
        this.nome = nome;
        this.descricao = descricao;
        this.padrao = padrao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isPadrao() {
        return padrao;
    }

    public void setPadrao(boolean padrao) {
        this.padrao = padrao;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", padrao=" + padrao +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categoria categoria = (Categoria) obj;
        return nome != null ? nome.equals(categoria.nome) : categoria.nome == null;
    }

    @Override
    public int hashCode() {
        return nome != null ? nome.hashCode() : 0;
    }
}
