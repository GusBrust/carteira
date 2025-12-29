import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Orcamento implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private String nome;
    private double valorLimite;
    private double valorGasto;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Categoria categoria;

    public Orcamento(String nome, double valorLimite, Categoria categoria) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.valorLimite = valorLimite;
        this.valorGasto = 0;
        // Define o período do orçamento como o primeiro dia do mês atual até o último dia do mês atual
        LocalDateTime agora = LocalDateTime.now();
        this.dataInicio = agora.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        int ultimoDia = agora.toLocalDate().lengthOfMonth();
        this.dataFim = agora.withDayOfMonth(ultimoDia).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        this.categoria = categoria;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getValorLimite() {
        return valorLimite;
    }

    public double getValorGasto() {
        return valorGasto;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void alterarValorLimite(double valorLimite) {
        this.valorLimite = valorLimite;
    }

    public void alterarValorGasto(double valorGasto) {
        this.valorGasto = valorGasto;
    }

    public void alterarCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Altera o período do orçamento
     * @param dataInicio Nova data de início
     * @param dataFim Nova data de fim
     */
    public void alterarPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    @Override
    public String toString() {
        return "Orcamento{" +
                "nome='" + nome + '\'' +
                ", valorLimite=" + valorLimite +
                ", valorGasto=" + valorGasto +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", categoria=" + categoria +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Orcamento orcamento = (Orcamento) obj;
        return id != null ? id.equals(orcamento.id) : orcamento.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
