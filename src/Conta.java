import java.time.LocalDate;

public class Conta {
    private String nome;
    private Double saldo;
    private static LocalDate dataInicio;

     public Conta( String nome, Double saldoInicial){
        this.nome = nome;
        this.saldo = saldoInicial;
        

     }

}
