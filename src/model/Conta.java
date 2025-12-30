package model;

import java.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe que representa uma conta bancária.
 * Uma conta possui um saldo que pode ser positivo ou negativo.
 * 
 * @author Sistema Carteira
 * @version 1.0
 */
public class Conta implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final String id;
    private String nome;
    private double saldo;
    private LocalDateTime dataAbertura;

    /**
     * Construtor da classe Conta.
     * 
     * @param nome Nome da conta
     * @param saldoInicial Saldo inicial da conta
     */
    public Conta(String nome, double saldoInicial) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.saldo = saldoInicial;
        this.dataAbertura = LocalDateTime.now();
    }

    /**
     * Retorna o ID único da conta.
     * 
     * @return ID da conta
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome da conta.
     * 
     * @return Nome da conta
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da conta.
     * 
     * @param nome Novo nome da conta
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o saldo atual da conta.
     * 
     * @return Saldo da conta
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Define o saldo da conta.
     * 
     * @param saldo Novo saldo
     */
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    /**
     * Deposita um valor na conta.
     * 
     * @param valor Valor a depositar (deve ser maior que zero)
     * @throws IllegalArgumentException se o valor for menor ou igual a zero
     */
    public void depositar(double valor) throws IllegalArgumentException {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor a depositar deve ser maior que zero.");
        }
        this.saldo += valor;
    }

    /**
     * Retira um valor da conta.
     * Permite saldo negativo.
     * 
     * @param valor Valor a retirar (deve ser maior que zero)
     * @throws IllegalArgumentException se o valor for menor ou igual a zero
     */
    public void retirar(double valor) throws IllegalArgumentException {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor a retirar deve ser maior que zero.");
        }
        this.saldo -= valor;
    }

    /**
     * Transfere um valor desta conta para outra conta.
     * 
     * @param destino Conta de destino
     * @param valor Valor a transferir (deve ser maior que zero)
     * @throws IllegalArgumentException se o valor for menor ou igual a zero
     */
    public void transferir(Conta destino, double valor) throws IllegalArgumentException {
        this.retirar(valor);
        destino.depositar(valor);
    }

    /**
     * Retorna a data de abertura da conta.
     * 
     * @return Data de abertura
     */
    public LocalDateTime getDataAbertura() {
        return this.dataAbertura;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", saldo=" + saldo +
                ", dataAbertura=" + dataAbertura.format(formatter) +
                '}';
    }

    /**
     * Compara duas contas pela igualdade.
     * Duas contas são consideradas iguais se tiverem o mesmo ID (UUID).
     * 
     * @param obj Objeto a ser comparado
     * @return true se as contas têm o mesmo ID, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Conta conta = (Conta) obj;
        return id.equals(conta.id);
    }

    /**
     * Retorna o código hash da conta baseado no ID.
     * 
     * @return hashCode do ID da conta
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
