package server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import util.Banco;
import util.Operacao;

public class InsereLog implements Runnable {

	private String arquivo;
	private ArrayBlockingQueue<Operacao> filaLogs;
	private Map<BigInteger, String> banco;
	private Banco elemento;

	public InsereLog(String arquivo, ArrayBlockingQueue<Operacao> filaLogs) {
		this.arquivo = arquivo;
		this.filaLogs = filaLogs;

	}

	@Override
	public void run() {
		
	
		banco = null;
		
		while (true) {

			try {
				
				Operacao operacao = filaLogs.take();  //Metodo take é bloqueante 
				System.out.println("Salvado as operacoes no arquivo " + arquivo);
				System.out.println("Operacao: " + operacao.getTipo());
				System.out.println("Chave: " + operacao.getChave());
				System.out.println("Valor: " + operacao.getValor());
				
				FileWriter arq = new FileWriter("C:\\Teste\\arquivo.txt");
				PrintWriter printWriter = new PrintWriter(arq);
		    
			    elemento = null;
			    String valor = new String();
			    
			    for (BigInteger chave : banco.keySet()) {
			    	valor = (chave + " : " + elemento.getValor(chave));
			    	printWriter.println(valor);
				}
			    
			    System.out.println(valor);
			    
			    printWriter.close();
			    arq.close();
				
			} catch (InterruptedException e) {
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
