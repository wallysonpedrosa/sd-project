package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
				String valor = new String();
				Operacao operacao = filaLogs.take(); // Metodo take é bloqueante
				System.out.println("Salvado as operacoes no arquivo " + arquivo);

				switch (operacao.getTipo()) {
				case CREATE:
					valor = operacao.getTipo() + "," + operacao.getChave() + "," + operacao.getValor();
					break;

				case DELETE:
					valor = operacao.getTipo() + "," + operacao.getChave();
					break;

				case UPDATE:
					valor = operacao.getTipo() + "," + operacao.getChave() + "," + operacao.getValor();
					break;
				default:
					break;
				}
				// System.out.println("Operacao: " + operacao.getTipo());
				// System.out.println("Chave: " + operacao.getChave());
				// System.out.println("Valor: " + operacao.getValor());

				File log = new File(arquivo);
				if (!log.exists()) {
					log.createNewFile();
				}
				FileWriter fw = new FileWriter(arquivo, true);
				PrintWriter printWriter = new PrintWriter(fw);
				printWriter.println(valor);
				printWriter.close();
				fw.close();

			} catch (InterruptedException | IOException e) {
				System.out.println("Erro na escrita do arquvivo.");
				return;
			}
		}
	}

}
