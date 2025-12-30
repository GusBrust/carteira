package model;

import java.io.Serializable;
import java.util.UUID;

public class Divida implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private String NomeEntidade;
    private String Descricao;
    private double total;
    private double pago;
    
    public Divida(String NomeEntidade, String Descricao, double total) {
        this.id = UUID.randomUUID().toString();
        this.NomeEntidade = NomeEntidade;
        this.Descricao = Descricao;
        this.total = total;
        this.pago = 0.0;
    }
    
    public Divida(String NomeEntidade, String Descricao, double total, double pago) {
        this.id = UUID.randomUUID().toString();
        this.NomeEntidade = NomeEntidade;
        this.Descricao = Descricao;
        this.total = total;
        this.pago = pago;
    }
    
    public String getId() {
        return id;
    }
    
    public String getNomeEntidade() {
        return NomeEntidade;
    }
    
    public void setNomeEntidade(String NomeEntidade) {
        this.NomeEntidade = NomeEntidade;
    }
    
    public String getDescricao() {
        return Descricao;
    }
    
    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("O valor total não pode ser negativo.");
        }
        this.total = total;
    }
    
    public double getPago() {
        return pago;
    }
    
    public void adicionarPagamento(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        if (pago + valor > total) {
            throw new IllegalArgumentException("O valor pago não pode exceder o total da dívida.");
        }
        this.pago += valor;
    }
    
    public double getEmFalta() {
        return Math.max(0, total - pago);
    }
    
    public boolean estaPaga() {
        return pago >= total;
    }
    
    @Override
    public String toString() {
        return String.format("Divida{NomeEntidade='%s', total=%.2f, pago=%.2f, emFalta=%.2f}",
                NomeEntidade, total, pago, getEmFalta());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Divida divida = (Divida) obj;
        return id != null ? id.equals(divida.id) : divida.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
