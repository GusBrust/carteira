package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe abstrata que representa uma transação financeira.
 * Uma transação pode ser uma despesa, receita ou transferência.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public abstract class Transacao implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    protected double valor;
    protected String descricao;
    protected LocalDateTime data;
    protected Categoria categoria;
    protected Conta conta;

    /**
     * Construtor da classe Transacao.
     * 
     * @param valor Valor da transação (deve ser maior que zero)
     * @param descricao Descrição da transação
     * @param data Data e hora da transação (se null, usa a data/hora atual)
     * @param categoria Categoria da transação (pode ser null)
     * @param conta Conta associada à transação (não pode ser null)
     * @throws IllegalArgumentException se o valor for menor ou igual a zero, ou se a conta for null
     */
    public Transacao(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero.");
        }
        if (conta == null) {
            throw new IllegalArgumentException("A conta não pode ser nula.");
        }
        this.valor = valor;
        this.descricao = descricao;
        this.data = data != null ? data : LocalDateTime.now();
        this.categoria = categoria;
        this.conta = conta;
    }


    /**
     * Processa a transação, atualizando o saldo da conta (ou contas) envolvida(s)
     * @return true se a transação foi processada com sucesso, false caso contrário
     */
    public abstract boolean processar();

    /**
     * Reverte a transação, desfazendo as alterações no saldo
     * @return true se a reversão foi bem-sucedida, false caso contrário
     */
    public abstract boolean reverter();

    /**
     * Retorna o valor da transação.
     * 
     * @return Valor da transação
     */
    public double getValor() {
        return valor;
    }

    /**
     * Altera o valor da transação.
     * 
     * @param valor Novo valor (deve ser maior que zero)
     * @throws IllegalArgumentException se o valor for menor ou igual a zero
     */
    public void alterarValor(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero.");
        }
        this.valor = valor;
    }

    /**
     * Retorna a descrição da transação.
     * 
     * @return Descrição da transação
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Altera a descrição da transação.
     * 
     * @param descricao Nova descrição
     */
    public void alterarDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a data e hora da transação.
     * 
     * @return Data e hora da transação
     */
    public LocalDateTime getData() {
        return data;
    }

    /**
     * Altera a data e hora da transação.
     * 
     * @param data Nova data e hora
     */
    public void alterarData(LocalDateTime data) {
        this.data = data;
    }

    /**
     * Retorna a categoria da transação.
     * 
     * @return Categoria da transação (pode ser null)
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Altera a categoria da transação.
     * 
     * @param categoria Nova categoria (pode ser null)
     */
    public void alterarCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Retorna a conta associada à transação.
     * 
     * @return Conta associada
     */
    public Conta getConta() {
        return conta;
    }

    /**
     * Altera a conta associada à transação.
     * 
     * @param conta Nova conta (não pode ser null)
     * @throws IllegalArgumentException se a conta for null
     */
    public void alterarConta(Conta conta) {
        if (conta == null) {
            throw new IllegalArgumentException("A conta não pode ser nula.");
        }
        this.conta = conta;
    }

    @Override
    public String toString() {
        return String.format("%s{valor=%.2f, descricao='%s', data=%s, categoria=%s, conta=%s}",
                this.getClass().getSimpleName(),
                valor,
                descricao,
                data.format(formatter),
                categoria != null ? categoria.getNome() : "N/A",
                conta != null ? conta.getNome() : "N/A");
    }
}
