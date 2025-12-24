import java.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Conta implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final String id;
    private String nome;
    private double saldo;
    private LocalDateTime dataAbertura;

    public Conta(String nome, double saldoInicial) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.saldo = saldoInicial;
        this.dataAbertura = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void depositar(double valor) throws IllegalArgumentException {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor a depositar deve ser maior que zero.");
        }
        this.saldo += valor;
    }

    public void retirar(double valor) throws IllegalArgumentException {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor a retirar deve ser maior que zero.");
        }
        if (this.saldo < valor) {
            throw new IllegalArgumentException("Saldo insuficiente para retirar o valor.");
        }
        this.saldo -= valor;
    }

    public void transferir(Conta destino, double valor) throws IllegalArgumentException {
        this.retirar(valor);
        destino.depositar(valor);
    }

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
     * O hashCode é usado por coleções (HashSet, HashMap) para distribuir objetos.
     * IMPORTANTE: Se dois objetos são iguais (equals retorna true), 
     * eles DEVEM ter o mesmo hashCode.
     * Como equals() compara por ID, hashCode() também usa o ID.
     * @return hashCode do ID da conta
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
