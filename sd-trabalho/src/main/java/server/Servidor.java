package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import util.Banco;
import util.Operacao;

public class Servidor {

	private static Banco banco = new Banco(); // Inicializando o banco
	private ServerSocket servidorSocket;
	private ExecutorService threadPool;
	private AtomicBoolean estaRodando;
	private ArrayBlockingQueue<Operacao> filaComandos;
	private ArrayBlockingQueue<Operacao> filaBanco;
	private ArrayBlockingQueue<Operacao> filaLogs;
	private String nomeArquivo;

	public Servidor(int porta, String nomeDoArquivo) throws IOException {
		System.out.println("----- Iniciando Servidor -----");
		this.servidorSocket = new ServerSocket(porta); // Criando o serverSocket do servidor
		this.threadPool = Executors.newCachedThreadPool(); // Pool de Threads do servidor //new FabricaDeThreads()
		this.estaRodando = new AtomicBoolean(true);
		filaComandos = new ArrayBlockingQueue<>(50);
		filaBanco = new ArrayBlockingQueue<>(50);
		filaLogs = new ArrayBlockingQueue<>(50);
		nomeArquivo = nomeDoArquivo;

	}

	public void rodar() {
		povoaBanco(this.nomeArquivo);
		while (estaRodando.get()) {
			try {
				Socket socket = servidorSocket.accept(); // Aguardando a conexão com um cliente
				System.out.println("Novo Cliente conectado");

				// Criando o Runnabl que vai tratar como os comandos vão ser distribuidos pelo
				// server
				TarefasServer distribuirTarefas = new TarefasServer(this, socket, filaComandos, banco);
				// Utilizando uma tread do pool para executar as operações do servidor
				threadPool.execute(distribuirTarefas);

				Consumer consumer = new Consumer(filaComandos, filaBanco, filaLogs); // Thread que vai consumir os
																						// comandos da primeira fila
				threadPool.execute(consumer); // e vai inserir nas outras duas fila

				InsereBanco insereBanco = new InsereBanco(banco, filaBanco);
				threadPool.execute(insereBanco);

				InsereLog insereLog = new InsereLog(nomeArquivo, filaLogs);
				threadPool.execute(insereLog);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void povoaBanco(String arquivo) {

		System.out.println("Povoando o banco com o " + arquivo);

		List<String[]> lista = new ArrayList<>();

		try {
			FileReader fr = new FileReader(arquivo);
			BufferedReader br = new BufferedReader(fr);
			String str;
			while ((str = br.readLine()) != null) {
				lista.add(str.split(","));
			}
			br.close();
			for (String[] elemento : lista) {
				switch (elemento[0]) {
				case "CREATE":
					banco.setValor(new BigInteger(elemento[1]), elemento[2]);
					break;

				case "DELETE":
					banco.deletaValor(new BigInteger(elemento[1]));
					break;

				case "UPDATE":
					banco.atualizaValor(new BigInteger(elemento[1]), elemento[2]);
					break;
				default:
					break;
				}
			}

		} catch (IOException e) {
			System.out.println("Arquivo não encontrado!");
		}
	}

	public void parar() throws IOException {
		estaRodando.set(false);
		threadPool.shutdown();
		servidorSocket.close();
	}

	public static void main(String[] args) throws IOException {
		Servidor server = new Servidor(9876, "arquivo.txt");
		server.rodar();
		server.parar();

	}
}