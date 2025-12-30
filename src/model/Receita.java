package model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma receita financeira.
 * Uma receita aumenta o saldo da conta associada.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public class Receita extends Transacao {

    /**
     * Construtor da classe Receita.
     * 
     * @param valor Valor da receita
     * @param descricao Descrição da receita
     * @param data Data e hora da receita
     * @param categoria Categoria da receita
     * @param conta Conta associada
     */
    public Receita(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta) {
        super(valor, descricao, data, categoria, conta);
    }

    /**
     * Construtor da classe Receita com data atual.
     * 
     * @param valor Valor da receita
     * @param descricao Descrição da receita
     * @param categoria Categoria da receita
     * @param conta Conta associada
     */
    public Receita(double valor, String descricao, Categoria categoria, Conta conta) {
        super(valor, descricao, LocalDateTime.now(), categoria, conta);
    }

    /**
     * Processa a receita, aumentando o saldo da conta.
     * 
     * @return true se processada com sucesso, false caso contrário
     */
    @Override
    public boolean processar() {
        if (conta == null) {
            return false;
        }
        conta.depositar(valor);
        return true;
    }

    /**
     * Reverte a receita, retirando o valor da conta.
     * Só reverte se houver saldo suficiente.
     * 
     * @return true se revertida com sucesso, false caso contrário
     */
    @Override
    public boolean reverter() {
        if (conta == null) {
            return false;
        }
        if (conta.getSaldo() >= valor) {
            conta.retirar(valor);
            return true;
        }
        return false;
    }
}

