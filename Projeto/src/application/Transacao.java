package application;

import java.time.LocalDate;

public class Transacao {

	private LocalDate data;
	private String tipo; // "Receita" ou "Despesa"
	private String categoria;
	private String metodo;
	private double valor;
	private boolean fixa;

	public Transacao(LocalDate data, String tipo, String categoria, String metodo, double valor, boolean fixa) {
		this.data = data;
		this.tipo = tipo;
		this.categoria = categoria;
		this.metodo = metodo;
		this.valor = valor;
		this.fixa = fixa;
	}

	public LocalDate getData() {
		return data;
	}

	public String getTipo() {
		return tipo;
	}

	public String getCategoria() {
		return categoria;
	}

	public String getMetodo() {
		return metodo;
	}

	public double getValor() {
		return valor;
	}

	public boolean isFixa() {
		return fixa;
	}

}
