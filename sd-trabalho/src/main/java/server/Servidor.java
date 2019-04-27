package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
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
	private String arquivo;

	public Servidor() throws IOException {
		System.out.println("----- Iniciando Servidor -----");
		this.servidorSocket = new ServerSocket(9876); // Criando o serverSocket do servidor
		this.threadPool = Executors.newCachedThreadPool(); // Pool de Threads do servidor //new FabricaDeThreads()
		this.estaRodando = new AtomicBoolean(true);
		filaComandos = new ArrayBlockingQueue<>(4);
		filaBanco = new ArrayBlockingQueue<>(4);
		filaLogs = new ArrayBlockingQueue<>(4);
		arquivo = "arquivo.txt";
		povoaBanco(arquivo);
		
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
				
				InsereLog insereLog = new InsereLog(arquivo, filaLogs);
				threadPool.execute(insereLog);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void povoaBanco(String arquivo){
		System.out.println("Povoando o banco com o " + arquivo);
		
		/*Scanner scanner;
		try {
			scanner = new Scanner(new FileReader(arquivo));
			while (scanner.hasNextLine()) {
	            String[] columns = scanner.nextLine().split(":");
	            banco.setValor(new BigInteger(columns[0]), columns[1]);
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			 List<String> linhas = Files.readAllLines(Paths.get(arquivo));
			 for (String linha : linhas) {
			        String[] registro = linha.split(",");
			        if(registro[0].equals("DELETE")) {
			        	break;
			        } else {
			        	banco.setValor(new BigInteger(registro[1]), registro[2]);
			        }
			 }
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
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