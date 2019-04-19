package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

import util.Operacao;
import util.Status;
import util.Tipo;

public class Cliente {
	public static void main(String[] args) {
		
		try {
			Socket socket = new Socket("localhost", 9876);
			
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			
			Operacao operacao = new Operacao ();
			
			//CREATE
//			operacao.setTipo(Tipo.CREATE);
//			operacao.setStatus(Status.SOLICITACAO);
//			operacao.setChave(BigInteger.valueOf(1));
//			operacao.setValor("AAA");
			//FIM CREATE
			
			//READ
			operacao.setTipo(Tipo.READ);
			operacao.setStatus(Status.SOLICITACAO);
//			operacao.setChave(BigInteger.valueOf(2));
			//FIM READ
			
			
			output.writeObject(1);
			output.flush();
			
			Operacao reply = (Operacao) input.readObject();
			System.out.println("Resposta do Servidor:");
			
			//CREATE
			System.out.println(reply.getStatus() + " " + reply.getMensagem());
			//FIM CREATE
			
			//READ
//			System.out.println(reply.getStatus());
//			System.out.println(reply.getValor());
			//FIM READ
				
			output.close();
			input.close();
			socket.close();
			
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
