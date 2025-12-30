package model;

import java.time.LocalDateTime;

public class Despesa extends Transacao {
    private String dividaId; // ID da dívida relacionada, se houver
    private boolean despesaFixa; // true se a despesa é fixa, false caso contrário

    public Despesa(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta) {
        super(valor, descricao, data, categoria, conta);
        this.dividaId = null;
        this.despesaFixa = false;
    }

    public Despesa(double valor, String descricao, Categoria categoria, Conta conta) {
        super(valor, descricao, LocalDateTime.now(), categoria, conta);
        this.dividaId = null;
        this.despesaFixa = false;
    }
    
    public Despesa(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta, String dividaId) {
        super(valor, descricao, data, categoria, conta);
        this.dividaId = dividaId;
        this.despesaFixa = false;
    }
    
    public Despesa(double valor, String descricao, Categoria categoria, Conta conta, String dividaId) {
        super(valor, descricao, LocalDateTime.now(), categoria, conta);
        this.dividaId = dividaId;
        this.despesaFixa = false;
    }
    
    // Construtores com despesaFixa
    public Despesa(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta, boolean despesaFixa) {
        super(valor, descricao, data, categoria, conta);
        this.dividaId = null;
        this.despesaFixa = despesaFixa;
    }
    
    public Despesa(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta, String dividaId, boolean despesaFixa) {
        super(valor, descricao, data, categoria, conta);
        this.dividaId = dividaId;
        this.despesaFixa = despesaFixa;
    }
    
    public String getDividaId() {
        return dividaId;
    }
    
    public void setDividaId(String dividaId) {
        this.dividaId = dividaId;
    }
    
    public boolean estaRelacionadaADivida() {
        return dividaId != null && !dividaId.isEmpty();
    }
    
    public boolean isDespesaFixa() {
        return despesaFixa;
    }
    
    public void setDespesaFixa(boolean despesaFixa) {
        this.despesaFixa = despesaFixa;
    }

    @Override
    public boolean processar() {
        if (conta == null) {
            return false;
        }
        // Permite saldo negativo - processa a despesa independente do saldo
        conta.retirar(valor);
        return true;
    }

    @Override
    public boolean reverter() {
        if (conta == null) {
            return false;
        }
        // Para reverter uma despesa, devolvemos o valor que foi retirado
        conta.depositar(valor);
        return true;
    }

    /**
     * Verifica se há saldo suficiente para a despesa
     * @return true se há saldo suficiente, false caso contrário
     */
    public boolean temSaldoSuficiente() {
        return conta != null && conta.getSaldo() >= valor;
    }
}

