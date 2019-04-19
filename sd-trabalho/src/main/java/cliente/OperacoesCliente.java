package cliente;

import util.Operacao;
import util.Tipo;

public class OperacoesCliente implements Runnable{
	
	private Operacao operacao;

	public OperacoesCliente(Operacao operacao2) {
		this.operacao = operacao2;
	}

	public OperacoesCliente() {
	}

	@Override
	public void run() {
		if(Tipo.CREATE.equals(this.operacao.getTipo())) {
			System.out.println("\nResposta do servidor:");
			System.out.println("Operacao: " + this.operacao.getTipo() + "\nStatus: " + this.operacao.getStatus() + "\nMensagem: " + this.operacao.getMensagem());
		}
		
		if(Tipo.READ.equals(this.operacao.getTipo())) {
			System.out.println("\nResposta do Servidor:");
			System.out.println("Operacao: " +  this.operacao.getTipo() + "\nStatus: " +  this.operacao.getStatus() + "\nMensagem: " +  this.operacao.getMensagem());
			
		}
		if(Tipo.UPDATE.equals(this.operacao.getTipo())) {
			System.out.println("\nResposta do servidor:");
			System.out.println("Operacao: " + this.operacao.getTipo() + "\nStatus: " + this.operacao.getStatus() + "\nMensagem: " + this.operacao.getMensagem());
		}
		
		if(Tipo.DELETE.equals(this.operacao.getTipo())) {
			System.out.println("\nResposta do servidor:");
			System.out.println("Operacao: " + this.operacao.getTipo() + "\nStatus: " + this.operacao.getStatus() + "\nMensagem: " + this.operacao.getMensagem());
		}
		if(Tipo.SAIR.equals(this.operacao.getTipo())) {
			
			System.out.println("\nResposta do servidor:");
			System.out.println("Operacao: " + this.operacao.getTipo() + "\nStatus: " + this.operacao.getStatus() + "\nMensagem: " + this.operacao.getMensagem());
			
		}
		if(Tipo.AJUDA.equals(this.operacao.getTipo())) {

			System.out.println("\nCREATE - Inserir no Banco \nDeve ser Informado a chave e o valor do elemento");
			System.out.println("\nREAD - Ler uma instancia \nDeve ser Informada a chave do elemento");
			System.out.println("\nUPDATE - Atualizar uma Instancia do Banco \nDeve ser informado chave e valor");
			System.out.println("\nDELETE - Apagar uma Instancia do Banco \nDeve ser informado a chave do elemento");
			System.out.println("\nSAIR - Desconectar do Banco");
		}
	}
	
}
