package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import util.Banco;
import util.Operacao;

public class Servidor {

	private static Banco banco = new Banco(); //Inicializando o banco
	private ServerSocket servidorSocket;
	private ExecutorService threadPool;
	private AtomicBoolean estaRodando;
	private ArrayBlockingQueue<Operacao> filaComandos;
	private ArrayBlockingQueue<Operacao> filaBanco;
	private ArrayBlockingQueue<Operacao> filaLogs;

	public Servidor() throws IOException {
		System.out.println("----- Iniciando Servidor -----");
		this.servidorSocket = new ServerSocket(9876); // Criando o serverSocket do servidor
		this.threadPool = Executors.newCachedThreadPool(); // Pool de Threads do servidor //new FabricaDeThreads()
		this.estaRodando = new AtomicBoolean(true);
		filaComandos = new ArrayBlockingQueue<>(4);
		filaBanco = new ArrayBlockingQueue<>(4);
		filaLogs = new ArrayBlockingQueue<>(4);
		
	}

	public void rodar() {
		while (estaRodando.get()) {
			try {
				Socket socket = servidorSocket.accept(); // Aguardando a conexão com um cliente
				System.out.println("Novo Cliente conectado");
				TarefasServer distribuirTarefas = new TarefasServer(this, socket,threadPool, filaComandos, banco); // Criando o Runnable que
																								// vai tratar como os
																								// comandos vão ser
																								// distribuidos pelo
																								// server
				
				threadPool.execute(distribuirTarefas);	//Utilizando uma tread do pool para executar as operações do servidor
				
				Consumer consumer = new Consumer(filaComandos,filaBanco,filaLogs);		//Thread que vai consumir os comandos da primeira fila
				threadPool.execute(consumer);											//e vai inserir nas outras duas fila
				
				InsereBanco insereBanco = new InsereBanco(banco, filaBanco);
				threadPool.execute(insereBanco);
				
				InsereLog insereLog = new InsereLog("arquivo.txt", filaLogs);
				threadPool.execute(insereLog);
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