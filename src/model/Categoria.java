package model;

import java.io.Serializable;

/**
 * Classe que representa uma categoria de transação.
 * Categorias podem ser padrão (não podem ser modificadas ou removidas) ou personalizadas.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String descricao;
    private boolean padrao;

    /**
     * Construtor da classe Categoria.
     * 
     * @param nome Nome da categoria
     * @param descricao Descrição da categoria
     * @param padrao true se é categoria padrão, false caso contrário
     */
    public Categoria(String nome, String descricao, boolean padrao) {
        this.nome = nome;
        this.descricao = descricao;
        this.padrao = padrao;
    }

    /**
     * Retorna o nome da categoria.
     * 
     * @return Nome da categoria
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da categoria.
     * Categorias padrão não podem ter o nome alterado.
     * 
     * @param nome Novo nome
     * @throws UnsupportedOperationException se a categoria for padrão
     */
    public void setNome(String nome) {
        if (this.padrao) {
            throw new UnsupportedOperationException("Não é possível alterar o nome de uma categoria padrão.");
        }
        this.nome = nome;
    }

    /**
     * Retorna a descrição da categoria.
     * 
     * @return Descrição da categoria
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição da categoria.
     * Categorias padrão não podem ter a descrição alterada.
     * 
     * @param descricao Nova descrição
     * @throws UnsupportedOperationException se a categoria for padrão
     */
    public void setDescricao(String descricao) {
        if (this.padrao) {
            throw new UnsupportedOperationException("Não é possível alterar a descrição de uma categoria padrão.");
        }
        this.descricao = descricao;
    }

    /**
     * Verifica se a categoria é padrão.
     * 
     * @return true se é categoria padrão, false caso contrário
     */
    public boolean isPadrao() {
        return padrao;
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
