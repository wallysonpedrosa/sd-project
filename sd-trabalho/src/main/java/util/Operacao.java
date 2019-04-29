package util;

import java.io.Serializable;
import java.math.BigInteger;

public class Operacao implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Tipo tipo;
	private Status status;
	String mensagem;
	private BigInteger chave;
	private String valor;
	private String elemento;
	
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getMensagem() {
		return mensagem;
	}
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	public BigInteger getChave() {
		return chave;
	}
	public void setChave(BigInteger chave) {
		this.chave = chave;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getValores() {
		return elemento;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
}
