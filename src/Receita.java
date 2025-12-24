import java.time.LocalDateTime;

public class Receita extends Transacao {

    public Receita(double valor, String descricao, LocalDateTime data, Categoria categoria, Conta conta) {
        super(valor, descricao, data, categoria, conta);
    }

    public Receita(double valor, String descricao, Categoria categoria, Conta conta) {
        super(valor, descricao, LocalDateTime.now(), categoria, conta);
    }

    @Override
    public boolean processar() {
        if (conta == null) {
            return false;
        }
        conta.depositar(valor);
        return true;
    }

    @Override
    public boolean reverter() {
        if (conta == null) {
            return false;
        }
        // Para reverter uma receita, retiramos o valor que foi depositado
        if (conta.getSaldo() >= valor) {
            conta.retirar(valor);
            return true;
        }
        return false;
    }
}

