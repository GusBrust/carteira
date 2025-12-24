import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Transacao implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    protected double valor;
    protected String descricao;
    protected LocalDateTime data;
    protected Categoria categoria;
    protected Conta conta;

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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser maior que zero.");
        }
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
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
