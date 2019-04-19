package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import util.Banco;
import util.Operacao;
import util.Status;
import util.Tipo;

public class Server implements Runnable {

	private ServerSocket serverSocket;
	private static Banco banco = new Banco();

	public void criaConexao(int porta) throws IOException {
		serverSocket = new ServerSocket(9876);
	}

	public Socket esperaConexao() throws IOException {
		Socket socket = serverSocket.accept();
		return socket;
	}

	public void aguardaCliente() throws InterruptedException {
		serverSocket.wait();
	}

	public void trataConexao(Socket socket) throws IOException {
		
		try {

			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			Operacao operacao = (Operacao) input.readObject();
			System.out.println("Operacao " + operacao.getTipo());

			Operacao reply = new Operacao();

			switch (operacao.getTipo()) {
			case CREATE:
				reply.setTipo(Tipo.CREATE);

				if (banco.existeElemento(operacao.getChave())) {
					reply.setStatus(Status.ERRO);
					reply.setMensagem("Um elemento com a chave informada ja existe no banco");

				} else if (operacao.getChave() == null || operacao.getValor() == null) {

					reply.setStatus(Status.ERRO);
					reply.setMensagem("Informe Chave e Valor para inserir no banco");

				} else {
					banco.setValor(operacao.getChave(), operacao.getValor());
					reply.setStatus(Status.OK);
					reply.setMensagem("Elemento inserido com sucesso");
				}
				output.writeObject(reply);
				output.flush();
				break;

			case READ:

				reply.setTipo(Tipo.READ);
				if (banco.existeElemento(operacao.getChave())) {
					reply.setStatus(Status.OK);
					reply.setMensagem(
							"Chave: " + operacao.getChave() + " Valor: " + banco.getValor(operacao.getChave()));
				} else {

					reply.setStatus(Status.ERRO);
					reply.setMensagem("Nao existe nenhum elemento com a chave informada");
				}

				output.writeObject(reply);
				output.flush();
				break;

			case UPDATE:

				reply.setTipo(Tipo.UPDATE);
				if (banco.existeElemento(operacao.getChave())) {
					banco.atualizaValor(operacao.getChave(), operacao.getValor());
					reply.setStatus(Status.OK);
					reply.setMensagem("Elemento Atualizado com Sucesso");

				} else if (operacao.getChave() == null || operacao.getValor() == null) {

					reply.setStatus(Status.ERRO);
					reply.setMensagem("Informe Chave e Valor para atualizar no banco");

				} else {
					reply.setStatus(Status.ERRO);
					reply.setMensagem("Nao existe nenhum elemento com a chave informada");
				}

				output.writeObject(reply);
				output.flush();
				break;

			case DELETE:

				reply.setTipo(Tipo.DELETE);
				if (banco.existeElemento(operacao.getChave())) {
					banco.deletaValor(operacao.getChave());
					reply.setStatus(Status.OK);
					reply.setMensagem("Elemento Deletado com Sucesso");

				} else if (operacao.getChave() == null || operacao.getValor() == null) {

					reply.setStatus(Status.ERRO);
					reply.setMensagem("Informe Chave para excluir no banco");

				} else {
					reply.setStatus(Status.ERRO);
					reply.setMensagem("Nao existe nenhum elemento com a chave informada");
				}

				output.writeObject(reply);
				output.flush();
				break;

			case SAIR:

				System.out.println("Finalizando conexao com cliente.");
				reply.setTipo(Tipo.SAIR);
				reply.setStatus(Status.OK);
				reply.setMensagem("Volte Sempre =)");

				output.writeObject(reply);
				output.flush();

				output.close();
				input.close();
				socket.close();

				break;
			}

		} catch (IOException | ClassNotFoundException | NullPointerException | ClassCastException e) {
			socket.close();

		}
	}

	public static void main(String[] args) {
		Thread t = new 	Thread(new Server());
		t.start();
	
	}

	@Override
	public void run() {
		try {
			Server server = new Server();
			server.criaConexao(9876);
			while (true) {
				System.out.println("Aguardando Conexao com cliente...");
				Socket socket = server.esperaConexao();
				System.out.println("Cliente conectado!");

				while (!socket.isClosed()) {
					System.out.println("Aguardando Cliente...");
					server.trataConexao(socket);

				}
			}
		
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
