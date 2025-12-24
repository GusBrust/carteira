import java.time.LocalDateTime;

public class Despesa extends Transacao {

    public Despesa(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta) {
        super(valor, descricao, data, categoria, conta);
    }

    public Despesa(double valor, String descricao, Categoria categoria, Conta conta) {
        super(valor, descricao, LocalDateTime.now(), categoria, conta);
    }

    @Override
    public boolean processar() {
        if (conta == null) {
            return false;
        }
        // Verifica se há saldo suficiente antes de processar
        if (conta.getSaldo() >= valor) {
            conta.retirar(valor);
            return true;
        }
        return false; // Saldo insuficiente
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

