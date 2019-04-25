package server;

import java.util.concurrent.ArrayBlockingQueue;

import util.Operacao;

public class InsereLog implements Runnable {

	private String arquivo;
	private ArrayBlockingQueue<Operacao> filaLogs;

	public InsereLog(String arquivo, ArrayBlockingQueue<Operacao> filaLogs) {
		this.arquivo = arquivo;
		this.filaLogs = filaLogs;

	}

	@Override
	public void run() {
		while (true) {

			try {
				
				Operacao operacao = filaLogs.take();  //Metodo take é bloqueante 
				System.out.println("Salvado as operacoes no arquivo " + arquivo);
				System.out.println("Operacao: " + operacao.getTipo());
				System.out.println("Chave: " + operacao.getChave());
				System.out.println("Valor: " + operacao.getValor());
				
				
			} catch (InterruptedException e) {
				return;
			}
		}
	}

}
