package model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma despesa financeira.
 * Uma despesa reduz o saldo da conta associada.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public class Despesa extends Transacao {
    private String dividaId;
    private boolean despesaFixa;

    /**
     * Construtor da classe Despesa.
     * 
     * @param valor Valor da despesa
     * @param descricao Descrição da despesa
     * @param data Data e hora da despesa
     * @param categoria Categoria da despesa
     * @param conta Conta associada
     */
    public Despesa(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta) {
        super(valor, descricao, data, categoria, conta);
        this.dividaId = null;
        this.despesaFixa = false;
    }

    /**
     * Construtor da classe Despesa com data atual.
     * 
     * @param valor Valor da despesa
     * @param descricao Descrição da despesa
     * @param categoria Categoria da despesa
     * @param conta Conta associada
     */
    public Despesa(double valor, String descricao, Categoria categoria, Conta conta) {
        super(valor, descricao, LocalDateTime.now(), categoria, conta);
        this.dividaId = null;
        this.despesaFixa = false;
    }
    
    /**
     * Construtor da classe Despesa com dívida relacionada.
     * 
     * @param valor Valor da despesa
     * @param descricao Descrição da despesa
     * @param data Data e hora da despesa
     * @param categoria Categoria da despesa
     * @param conta Conta associada
     * @param dividaId ID da dívida relacionada
     */
    public Despesa(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta, String dividaId) {
        super(valor, descricao, data, categoria, conta);
        this.dividaId = dividaId;
        this.despesaFixa = false;
    }
    
    /**
     * Construtor da classe Despesa com dívida relacionada e data atual.
     * 
     * @param valor Valor da despesa
     * @param descricao Descrição da despesa
     * @param categoria Categoria da despesa
     * @param conta Conta associada
     * @param dividaId ID da dívida relacionada
     */
    public Despesa(double valor, String descricao, Categoria categoria, Conta conta, String dividaId) {
        super(valor, descricao, LocalDateTime.now(), categoria, conta);
        this.dividaId = dividaId;
        this.despesaFixa = false;
    }
    
    /**
     * Construtor da classe Despesa com indicação de despesa fixa.
     * 
     * @param valor Valor da despesa
     * @param descricao Descrição da despesa
     * @param data Data e hora da despesa
     * @param categoria Categoria da despesa
     * @param conta Conta associada
     * @param despesaFixa true se a despesa é fixa, false caso contrário
     */
    public Despesa(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta, boolean despesaFixa) {
        super(valor, descricao, data, categoria, conta);
        this.dividaId = null;
        this.despesaFixa = despesaFixa;
    }
    
    /**
     * Construtor completo da classe Despesa.
     * 
     * @param valor Valor da despesa
     * @param descricao Descrição da despesa
     * @param data Data e hora da despesa
     * @param categoria Categoria da despesa
     * @param conta Conta associada
     * @param dividaId ID da dívida relacionada
     * @param despesaFixa true se a despesa é fixa, false caso contrário
     */
    public Despesa(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta, String dividaId, boolean despesaFixa) {
        super(valor, descricao, data, categoria, conta);
        this.dividaId = dividaId;
        this.despesaFixa = despesaFixa;
    }
    
    /**
     * Retorna o ID da dívida relacionada.
     * 
     * @return ID da dívida ou null se não houver
     */
    public String getDividaId() {
        return dividaId;
    }
    
    /**
     * Define o ID da dívida relacionada.
     * 
     * @param dividaId ID da dívida
     */
    public void setDividaId(String dividaId) {
        this.dividaId = dividaId;
    }
    
    /**
     * Verifica se a despesa está relacionada a uma dívida.
     * 
     * @return true se está relacionada a uma dívida, false caso contrário
     */
    public boolean estaRelacionadaADivida() {
        return dividaId != null && !dividaId.isEmpty();
    }
    
    /**
     * Verifica se a despesa é fixa.
     * 
     * @return true se é despesa fixa, false caso contrário
     */
    public boolean isDespesaFixa() {
        return despesaFixa;
    }
    
    /**
     * Define se a despesa é fixa.
     * 
     * @param despesaFixa true se é despesa fixa, false caso contrário
     */
    public void setDespesaFixa(boolean despesaFixa) {
        this.despesaFixa = despesaFixa;
    }

    /**
     * Processa a despesa, reduzindo o saldo da conta.
     * Permite saldo negativo.
     * 
     * @return true se processada com sucesso, false caso contrário
     */
    @Override
    public boolean processar() {
        if (conta == null) {
            return false;
        }
        // Permite saldo negativo - processa a despesa independente do saldo
        conta.retirar(valor);
        return true;
    }

    /**
     * Reverte a despesa, devolvendo o valor à conta.
     * 
     * @return true se revertida com sucesso, false caso contrário
     */
    @Override
    public boolean reverter() {
        if (conta == null) {
            return false;
        }
        conta.depositar(valor);
        return true;
    }

    /**
     * Verifica se há saldo suficiente para a despesa.
     * 
     * @return true se há saldo suficiente, false caso contrário
     */
    public boolean temSaldoSuficiente() {
        return conta != null && conta.getSaldo() >= valor;
    }
}

