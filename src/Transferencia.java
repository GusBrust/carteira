import java.time.LocalDateTime;

public class Transferencia extends Transacao {
    private Conta contaDestino;

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

    public Transferencia(double valor, String descricao, Conta contaOrigem, Conta contaDestino) {
        this(valor, descricao, LocalDateTime.now(), contaOrigem, contaDestino);
    }

    @Override
    public boolean processar() {
        try {
            conta.transferir(contaDestino, valor);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean reverter() {
        try {
            contaDestino.transferir(conta, valor);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Conta contaDestino) {
        if (contaDestino == null || contaDestino.equals(conta)) {
            throw new IllegalArgumentException("A conta de destino não pode ser nula ou igual à conta de origem.");
        }
        this.contaDestino = contaDestino;
    }

    @Override
    public void setCategoria(Categoria categoria) {
        // Transferências não possuem categoria
        // Ignora silenciosamente tentativas de definir categoria
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

