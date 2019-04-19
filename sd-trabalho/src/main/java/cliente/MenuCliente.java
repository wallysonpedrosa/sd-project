package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import util.Operacao;
import util.Status;
import util.Tipo;

public class MenuCliente implements Runnable{

	public static void main(String[] args) {
		Thread t = new Thread(new MenuCliente());
		t.start();
	}

	@Override
	public void run() {
		Socket socket;
		try {

			socket = new Socket("localhost", 9876);
			ObjectOutputStream output;
			ObjectInputStream input;
			
			System.out.println("---------- Conexao Estabelecida com Sucesso -----------");
			boolean continua = true;
			Scanner scanner = new Scanner(System.in);
			String tipo;
			Operacao operacao = new Operacao();
			Operacao reply;

			while (continua) {
				System.out.println("\nEscolha a Operacao:");
				tipo = scanner.next();

				switch (tipo) {
				case "CREATE":
					
					output = new ObjectOutputStream(socket.getOutputStream());
					input = new ObjectInputStream(socket.getInputStream());
					
					operacao.setTipo(Tipo.CREATE);
					operacao.setStatus(Status.SOLICITACAO);
						System.out.println("Digite a Chave:");
					operacao.setChave(BigInteger.valueOf(scanner.nextInt()));
						System.out.println("Digite o valor:");
					operacao.setValor(scanner.next());

					Thread op = new Thread(new OperacoesCliente());
					op.start();
					op.join();
					
					output.writeObject(operacao);
					output.flush();
					
					reply = (Operacao) input.readObject();
					System.out.println("\nResposta do servidor:");

					System.out.println("Operacao: " + reply.getTipo() + "\nStatus: " + reply.getStatus() + "\nMensagem: " + reply.getMensagem());
					break;

				case "READ":
					
					output = new ObjectOutputStream(socket.getOutputStream());
					input = new ObjectInputStream(socket.getInputStream());
					
					operacao.setTipo(Tipo.READ);
					operacao.setStatus(Status.SOLICITACAO);
					System.out.println("Digite a Chave:");
					operacao.setChave(BigInteger.valueOf(scanner.nextInt()));
					
					output.writeObject(operacao);
					output.flush();

					reply = (Operacao) input.readObject();
					System.out.println("\nResposta do Servidor:");

					System.out.println("Operacao: " + reply.getTipo() + "\nStatus: " + reply.getStatus() + "\nMensagem: " + reply.getMensagem());
					
					break;

				case "UPDATE":

					output = new ObjectOutputStream(socket.getOutputStream());
					input = new ObjectInputStream(socket.getInputStream());
					
					operacao.setTipo(Tipo.UPDATE);
					operacao.setStatus(Status.SOLICITACAO);
					System.out.println("Digite a Chave:");
					operacao.setChave(BigInteger.valueOf(scanner.nextInt()));
					System.out.println("Digite o Valor:");
					operacao.setValor(scanner.next());

					output.writeObject(operacao);
					output.flush();
					
					reply = (Operacao) input.readObject();
					System.out.println("\nResposta do servidor:");

					System.out.println("Operacao: " + reply.getTipo() + "\nStatus: " + reply.getStatus() + "\nMensagem: " + reply.getMensagem());
					break;

				case "DELETE":

					output = new ObjectOutputStream(socket.getOutputStream());
					input = new ObjectInputStream(socket.getInputStream());
					
					operacao.setTipo(Tipo.DELETE);
					operacao.setStatus(Status.SOLICITACAO);
					System.out.println("Digite a Chave:");
					operacao.setChave(BigInteger.valueOf(scanner.nextInt()));
					
					output.writeObject(operacao);
					output.flush();
					
					reply = (Operacao) input.readObject();
					System.out.println("\nResposta do servidor:");

					System.out.println("Operacao: " + reply.getTipo() + "\nStatus: " + reply.getStatus() + "\nMensagem: " + reply.getMensagem());
					break;

				case "SAIR":
					
					output = new ObjectOutputStream(socket.getOutputStream());
					input = new ObjectInputStream(socket.getInputStream());
					
					operacao.setTipo(Tipo.SAIR);

					output.writeObject(operacao);
					output.flush();

					reply = (Operacao) input.readObject();
					System.out.println("\nResposta do servidor:");
					System.out.println("Operacao: " + reply.getTipo() + "\nStatus: " + reply.getStatus() + "\nMensagem: " + reply.getMensagem());
					
					scanner.close();
					output.close();
					input.close();
					socket.close();
					continua = false;
					break;

				case "AJUDA":

					System.out.println("\nCREATE - Inserir no Banco \nDeve ser Informado a chave e o valor do elemento");
					System.out.println("\nREAD - Ler uma instancia \nDeve ser Informada a chave do elemento");
					System.out.println("\nUPDATE - Atualizar uma Instancia do Banco \nDeve ser informado chave e valor");
					System.out.println("\nDELETE - Apagar uma Instancia do Banco \nDeve ser informado a chave do elemento");
					System.out.println("\nSAIR - Desconectar do Banco");
					
					break;

				default:
					System.out.println("\nOperacao Invalida - Digite AJUDA para mostrar todas as operacoes");
				}

			}

		} catch (

		UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
