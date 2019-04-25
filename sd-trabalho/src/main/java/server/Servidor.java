package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import util.Banco;

public class Servidor {

	private static Banco banco = new Banco(); //Inicializando o banco
	private ServerSocket servidorSocket;
	private ExecutorService threadPool;
	private AtomicBoolean estaRodando;
	// private ArrayBlockingQueue filaComando;

	public Servidor() throws IOException {
		System.out.println("----- Iniciando Servidor -----");
		this.servidorSocket = new ServerSocket(9876); // Criando o serverSocket do servidor
		this.threadPool = Executors.newCachedThreadPool(); // Pool de Threads do servidor //new FabricaDeThreads()
		this.estaRodando = new AtomicBoolean(true);
		// this.filaComando = new ArrayBlockingQueue<>(2);
		
	}

	public void rodar() {
		while (estaRodando.get()) {
			try {
				Socket socket = servidorSocket.accept(); // Aguardando a conexão com um cliente
				System.out.println("Novo Cliente conectado");
				TarefasServer distribuirTarefas = new TarefasServer(this, socket, threadPool,banco); // Criando o Runnable que
																								// vai tratar como os
																								// comandos vão ser
																								// distribuidos pelo
																								// server
				threadPool.execute(distribuirTarefas);	//Utilizando uma tread do pool para executar as operações do servidor
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void parar() throws IOException {
		estaRodando.set(false);
		threadPool.shutdown();
		servidorSocket.close();
	}

	public static void main(String[] args) throws IOException {
		Servidor server = new Servidor();
		server.rodar();
		server.parar();

	}
}