import java.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Conta implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private int id;
    private static int proximoId;
    private String nome;
    private Double saldo;
    private LocalDateTime dataAbertura;

    public Conta(String nome, Double saldoInicial) {
        this.id = proximoId++;
        this.nome = nome;
        this.saldo = saldoInicial;
        this.dataAbertura = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public void depositar(Double valor) {
        this.saldo += valor;
    }

    public void retirar(Double valor) {
        this.saldo -= valor;
    }

    public void transferir(Conta destino, Double valor) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Conta conta = (Conta) obj;
        return id == conta.id;
    }

}
