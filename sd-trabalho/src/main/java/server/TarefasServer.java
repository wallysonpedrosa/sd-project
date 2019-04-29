package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import util.Banco;
import util.Operacao;
import util.Status;
import util.Tipo;

public class TarefasServer implements Runnable {

	private Servidor servidor;
	private Socket socket;
	private Banco banco;
	private ArrayBlockingQueue<Operacao> filaComandos;

	public TarefasServer(Servidor servidor, Socket socket, ArrayBlockingQueue<Operacao> filaComandos, Banco banco) {
		this.servidor = servidor;
		this.socket = socket;
		this.banco = banco;
		this.filaComandos = filaComandos;
	}

	@Override
	public void run() {

		try {

			while (true) {
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
				Operacao operacao = (Operacao) input.readObject();
//				System.out.println("Operacao " + operacao.getTipo());
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
						// banco.setValor(operacao.getChave(), operacao.getValor());
						filaComandos.put(operacao);
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
					
				case READVALUES:

					reply.setTipo(Tipo.READVALUES);
					List<String> elementos = banco.getElementos();
					
					if (elementos==null) {
						reply.setStatus(Status.ERRO);
						reply.setMensagem("Nao existem elementos no banco.");						
					} else {
						reply.setStatus(Status.OK);
						String elemento = new String();
						for(int i=0; i < elementos.size(); i++){
							elemento += elementos.get(i) + "\n";
						}
						reply.setMensagem(elemento);
					}

					output.writeObject(reply);
					output.flush();
					break;

				case UPDATE:

					reply.setTipo(Tipo.UPDATE);
					if (banco.existeElemento(operacao.getChave())) {
						// banco.atualizaValor(operacao.getChave(), operacao.getValor());
						filaComandos.put(operacao);
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
						// banco.deletaValor(operacao.getChave());
						filaComandos.put(operacao);
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
					return;

				default:
					System.out.println("Erro - Finalizando conexao com cliente");
					return;
				}
			}

		} catch (IOException | ClassNotFoundException | NullPointerException | ClassCastException
				| InterruptedException e) {
			System.out.println("Erro - Finalizando conexao com cliente");
			return;
		}
	}
}
