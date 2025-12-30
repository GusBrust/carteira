package model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma transferência entre contas.
 * Uma transferência move valor de uma conta origem para uma conta destino.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public class Transferencia extends Transacao {
    private static final long serialVersionUID = 1L;
    private Conta contaDestino;

    /**
     * Construtor da classe Transferencia.
     * 
     * @param valor Valor a transferir
     * @param descricao Descrição da transferência
     * @param data Data e hora da transferência
     * @param contaOrigem Conta de origem
     * @param contaDestino Conta de destino
     * @throws IllegalArgumentException se a conta de destino for null ou igual à origem
     */
    public Transferencia(double valor, String descricao, LocalDateTime data, 
                        Conta contaOrigem, Conta contaDestino) {
        super(valor, descricao, data, null, contaOrigem);
        if (contaDestino == null) {
            throw new IllegalArgumentException("A conta de destino não pode ser nula.");
        }
        if (contaOrigem.equals(contaDestino)) {
            throw new IllegalArgumentException("A conta de origem e destino não podem ser a mesma.");
        }
        this.contaDestino = contaDestino;
    }

    /**
     * Construtor da classe Transferencia com data atual.
     * 
     * @param valor Valor a transferir
     * @param descricao Descrição da transferência
     * @param contaOrigem Conta de origem
     * @param contaDestino Conta de destino
     * @throws IllegalArgumentException se a conta de destino for null ou igual à origem
     */
    public Transferencia(double valor, String descricao, Conta contaOrigem, Conta contaDestino) {
        this(valor, descricao, LocalDateTime.now(), contaOrigem, contaDestino);
    }

    /**
     * Processa a transferência, movendo o valor da conta origem para a conta destino.
     * 
     * @return true se processada com sucesso, false caso contrário
     */
    @Override
    public boolean processar() {
        try {
            conta.transferir(contaDestino, valor);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Reverte a transferência, movendo o valor de volta da conta destino para a conta origem.
     * 
     * @return true se revertida com sucesso, false caso contrário
     */
    @Override
    public boolean reverter() {
        try {
            contaDestino.transferir(conta, valor);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Retorna a conta de destino da transferência.
     * 
     * @return Conta de destino
     */
    public Conta getContaDestino() {
        return contaDestino;
    }

    /**
     * Altera a conta de destino da transferência.
     * 
     * @param contaDestino Nova conta de destino
     * @throws IllegalArgumentException se a conta de destino for null ou igual à origem
     */
    public void alterarContaDestino(Conta contaDestino) {
        if (contaDestino == null || contaDestino.equals(conta)) {
            throw new IllegalArgumentException("A conta de destino não pode ser nula ou igual à conta de origem.");
        }
        this.contaDestino = contaDestino;
    }

    /**
     * Transferências não possuem categoria.
     * Este método ignora silenciosamente tentativas de definir categoria.
     * 
     * @param categoria Categoria (ignorada)
     */
    @Override
    public void alterarCategoria(Categoria categoria) {
    }

    @Override
    public String toString() {
        return String.format("Transferencia{valor=%.2f, descricao='%s', data=%s, contaOrigem=%s, contaDestino=%s}",
                valor,
                descricao,
                getData().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                conta != null ? conta.getNome() : "N/A",
                contaDestino != null ? contaDestino.getNome() : "N/A");
    }
}

