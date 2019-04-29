package server;

import java.util.concurrent.ArrayBlockingQueue;

import util.Banco;
import util.Operacao;

public class InsereBanco implements Runnable {

	private Banco banco;
	private ArrayBlockingQueue<Operacao> filaBanco;

	public InsereBanco(Banco banco, ArrayBlockingQueue<Operacao> filaBanco) {
		this.banco = banco;
		this.filaBanco = filaBanco;
	}

	@Override
	public void run() {

		try {
			Operacao operacao = null;
			while (true) {

//				System.out.println(filaBanco.size());
				operacao = filaBanco.take(); // Operacao take é bloqueante
//				System.out.println(operacao.getTipo());

				switch (operacao.getTipo()) {
				case CREATE:
					banco.setValor(operacao.getChave(), operacao.getValor());
//					System.out.println("valor "+ operacao.getValor() +" inserido no banco");
					break;

				case DELETE:
					banco.deletaValor(operacao.getChave());
//					System.out.println("Elemento com chave  "+ operacao.getChave() +" deletado do banco");
					break;

				case UPDATE:
					banco.atualizaValor(operacao.getChave(), operacao.getValor());
//					System.out.println("Elemento com chave  "+ operacao.getChave() +" atualizado no banco");
					break;
				default:
					break;

				}

			}
		} catch (InterruptedException e) {
			System.out.println("Erro na insercao no banco.");
			return;
		}

	}

}
