package server;

import java.util.concurrent.ArrayBlockingQueue;

import util.Operacao;

public class Consumer implements Runnable {

	private ArrayBlockingQueue<Operacao> filaComandos;
	private ArrayBlockingQueue<Operacao> filaBanco;
	private ArrayBlockingQueue<Operacao> filaLogs;

	public Consumer(ArrayBlockingQueue<Operacao> filaComandos, ArrayBlockingQueue<Operacao> filaBanco,
			ArrayBlockingQueue<Operacao> filaLogs) {
		this.filaComandos = filaComandos;
		this.filaBanco = filaBanco;
		this.filaLogs = filaLogs;
	}

	@Override
	public void run() {
		try {
			Operacao operacao = null;
			while (true) {
				operacao = filaComandos.take();
				filaBanco.put(operacao);
				filaLogs.put(operacao);
			}
		} catch (InterruptedException e) {
			return;
		}

	}
}
