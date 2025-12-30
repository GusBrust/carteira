package model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe que representa uma dívida.
 * Uma dívida possui um valor total e um valor pago, permitindo rastrear o progresso do pagamento.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public class Divida implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private String NomeEntidade;
    private String Descricao;
    private double total;
    private double pago;
    
    /**
     * Construtor da classe Divida.
     * 
     * @param NomeEntidade Nome da entidade credora
     * @param Descricao Descrição da dívida
     * @param total Valor total da dívida
     */
    public Divida(String NomeEntidade, String Descricao, double total) {
        this.id = UUID.randomUUID().toString();
        this.NomeEntidade = NomeEntidade;
        this.Descricao = Descricao;
        this.total = total;
        this.pago = 0.0;
    }
    
    /**
     * Construtor da classe Divida com valor pago inicial.
     * 
     * @param NomeEntidade Nome da entidade credora
     * @param Descricao Descrição da dívida
     * @param total Valor total da dívida
     * @param pago Valor já pago
     */
    public Divida(String NomeEntidade, String Descricao, double total, double pago) {
        this.id = UUID.randomUUID().toString();
        this.NomeEntidade = NomeEntidade;
        this.Descricao = Descricao;
        this.total = total;
        this.pago = pago;
    }
    
    /**
     * Retorna o ID único da dívida.
     * 
     * @return ID da dívida
     */
    public String getId() {
        return id;
    }
    
    /**
     * Retorna o nome da entidade credora.
     * 
     * @return Nome da entidade
     */
    public String getNomeEntidade() {
        return NomeEntidade;
    }
    
    /**
     * Define o nome da entidade credora.
     * 
     * @param NomeEntidade Novo nome da entidade
     */
    public void setNomeEntidade(String NomeEntidade) {
        this.NomeEntidade = NomeEntidade;
    }
    
    /**
     * Retorna a descrição da dívida.
     * 
     * @return Descrição
     */
    public String getDescricao() {
        return Descricao;
    }
    
    /**
     * Define a descrição da dívida.
     * 
     * @param Descricao Nova descrição
     */
    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }
    
    /**
     * Retorna o valor total da dívida.
     * 
     * @return Valor total
     */
    public double getTotal() {
        return total;
    }
    
    /**
     * Define o valor total da dívida.
     * 
     * @param total Novo valor total (deve ser maior ou igual a zero)
     * @throws IllegalArgumentException se o valor for negativo
     */
    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("O valor total não pode ser negativo.");
        }
        this.total = total;
    }
    
    /**
     * Retorna o valor já pago da dívida.
     * 
     * @return Valor pago
     */
    public double getPago() {
        return pago;
    }
    
    /**
     * Adiciona um pagamento à dívida.
     * 
     * @param valor Valor do pagamento (deve ser maior que zero)
     * @throws IllegalArgumentException se o valor for menor ou igual a zero, ou se exceder o total
     */
    public void adicionarPagamento(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        if (pago + valor > total) {
            throw new IllegalArgumentException("O valor pago não pode exceder o total da dívida.");
        }
        this.pago += valor;
    }
    
    /**
     * Retorna o valor que ainda falta pagar.
     * 
     * @return Valor em falta (nunca negativo)
     */
    public double getEmFalta() {
        return Math.max(0, total - pago);
    }
    
    /**
     * Verifica se a dívida está completamente paga.
     * 
     * @return true se está paga, false caso contrário
     */
    public boolean estaPaga() {
        return pago >= total;
    }
    
    @Override
    public String toString() {
        return String.format("Divida{NomeEntidade='%s', total=%.2f, pago=%.2f, emFalta=%.2f}",
                NomeEntidade, total, pago, getEmFalta());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Divida divida = (Divida) obj;
        return id != null ? id.equals(divida.id) : divida.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
