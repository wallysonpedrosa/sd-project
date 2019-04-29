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

public class MenuCliente implements Runnable {

	private static Thread imprimeTela = new Thread(new OperacoesCliente());

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

					output.writeObject(operacao);
					output.flush();

					imprimeTela = new Thread(new OperacoesCliente((Operacao) input.readObject()));
					imprimeTela.start();
					imprimeTela.join();
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

					imprimeTela = new Thread(new OperacoesCliente((Operacao) input.readObject()));
					imprimeTela.start();
					imprimeTela.join();
					break;
					
				case "READVALUES":
					
					output = new ObjectOutputStream(socket.getOutputStream());
					input = new ObjectInputStream(socket.getInputStream());
					
					operacao.setTipo(Tipo.READVALUES);
					operacao.setStatus(Status.SOLICITACAO);
					operacao.getValores();
					output.writeObject(operacao);
					output.flush();

					imprimeTela = new Thread (new OperacoesCliente((Operacao) input.readObject()));
					imprimeTela.start();
					imprimeTela.join();
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

					imprimeTela = new Thread(new OperacoesCliente((Operacao) input.readObject()));
					imprimeTela.start();
					imprimeTela.join();
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

					imprimeTela = new Thread(new OperacoesCliente((Operacao) input.readObject()));
					imprimeTela.start();
					imprimeTela.join();
					break;

				case "SAIR":

					output = new ObjectOutputStream(socket.getOutputStream());
					input = new ObjectInputStream(socket.getInputStream());

					operacao.setTipo(Tipo.SAIR);

					output.writeObject(operacao);
					output.flush();

					imprimeTela = new Thread(new OperacoesCliente((Operacao) input.readObject()));
					imprimeTela.start();
					imprimeTela.join();

					fechaMenuCliente(scanner, output, output, input, socket);
					continua = false;
					break;

				case "AJUDA":

					System.out
							.println("\nCREATE - Inserir no Banco \nDeve ser Informado a chave e o valor do elemento");
					System.out.println("\nREAD - Ler uma instancia \nDeve ser Informada a chave do elemento");
					System.out
							.println("\nUPDATE - Atualizar uma Instancia do Banco \nDeve ser informado chave e valor");
					System.out.println(
							"\nDELETE - Apagar uma Instancia do Banco \nDeve ser informado a chave do elemento");
					System.out.println("\nSAIR - Desconectar do Banco");

					break;
				default:
					System.out.println("\nOperacao Invalida - Digite AJUDA para mostrar todas as operacoes");
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void fechaMenuCliente(Scanner scanner, ObjectOutputStream output, ObjectOutputStream output2,
			ObjectInputStream input, Socket socket) throws IOException {
		scanner.close();
		output.close();
		input.close();
		socket.close();
	}
}
