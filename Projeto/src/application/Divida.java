package application;

public class Divida {

	private String entidade;
	private String tipo; // "Dívida" ou "Empréstimo"
	private double valorTotal;
	private double valorPago;

	public Divida(String entidade, String tipo, double valorTotal, double valorPago) {
		this.entidade = entidade;
		this.tipo = tipo;
		this.valorTotal = valorTotal;
		this.valorPago = valorPago;
	}

	public String getEntidade() {
		return entidade;
	}

	public String getTipo() {
		return tipo;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public double getValorPago() {
		return valorPago;
	}

	public double getValorFalta() {
		return valorTotal - valorPago;
	}
}
