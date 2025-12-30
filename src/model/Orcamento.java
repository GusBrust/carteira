package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe que representa um orçamento financeiro.
 * Um orçamento define um limite de gastos para uma categoria em um período específico.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public class Orcamento implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private String nome;
    private double valorLimite;
    private double valorGasto;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Categoria categoria;

    /**
     * Construtor da classe Orcamento.
     * Define o período como o mês atual (do primeiro ao último dia).
     * 
     * @param nome Nome do orçamento
     * @param valorLimite Valor limite do orçamento
     * @param categoria Categoria do orçamento
     */
    public Orcamento(String nome, double valorLimite, Categoria categoria) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.valorLimite = valorLimite;
        this.valorGasto = 0;
        LocalDateTime agora = LocalDateTime.now();
        this.dataInicio = agora.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        int ultimoDia = agora.toLocalDate().lengthOfMonth();
        this.dataFim = agora.withDayOfMonth(ultimoDia).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        this.categoria = categoria;
    }

    /**
     * Retorna o ID único do orçamento.
     * 
     * @return ID do orçamento
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome do orçamento.
     * 
     * @return Nome do orçamento
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o valor limite do orçamento.
     * 
     * @return Valor limite
     */
    public double getValorLimite() {
        return valorLimite;
    }

    /**
     * Retorna o valor gasto no orçamento.
     * 
     * @return Valor gasto
     */
    public double getValorGasto() {
        return valorGasto;
    }

    /**
     * Retorna a data de início do período do orçamento.
     * 
     * @return Data de início
     */
    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    /**
     * Retorna a data de fim do período do orçamento.
     * 
     * @return Data de fim
     */
    public LocalDateTime getDataFim() {
        return dataFim;
    }

    /**
     * Retorna a categoria do orçamento.
     * 
     * @return Categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Altera o valor limite do orçamento.
     * 
     * @param valorLimite Novo valor limite
     */
    public void alterarValorLimite(double valorLimite) {
        this.valorLimite = valorLimite;
    }

    /**
     * Altera o valor gasto no orçamento.
     * 
     * @param valorGasto Novo valor gasto
     */
    public void alterarValorGasto(double valorGasto) {
        this.valorGasto = valorGasto;
    }

    /**
     * Altera a categoria do orçamento.
     * 
     * @param categoria Nova categoria
     */
    public void alterarCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Altera o período do orçamento.
     * 
     * @param dataInicio Nova data de início
     * @param dataFim Nova data de fim
     */
    public void alterarPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    @Override
    public String toString() {
        return "Orcamento{" +
                "nome='" + nome + '\'' +
                ", valorLimite=" + valorLimite +
                ", valorGasto=" + valorGasto +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", categoria=" + categoria +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Orcamento orcamento = (Orcamento) obj;
        return id != null ? id.equals(orcamento.id) : orcamento.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
