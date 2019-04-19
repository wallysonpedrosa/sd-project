package trabalho_sd.sd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GreetServer implements Runnable{
	
	private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;
    
    private static Map<BigInteger, String> baseDados = new HashMap<BigInteger, String>();
 
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        System.out.println(port);
        
        while(true) {
        System.out.println("Esperando");
        clientSocket = serverSocket.accept();
        
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        String greeting = in.readLine();
        System.out.println(greeting);
        if ("hello server".equals(greeting)) {
                out.println("hello client");
            }
            else {
                out.println("unrecognised greeting");
            }
        baseDados = geraBaseDados(baseDados);
        
        for(BigInteger i:baseDados.keySet()) {
        	System.out.println("Key:" + i + " Valor:" + baseDados.get(i));
        }
       }
    }
 
    private static Map<BigInteger, String> geraBaseDados(Map<BigInteger, String> baseDados) {
    	baseDados.put(new BigInteger("1"), "A");
    	baseDados.put(new BigInteger("2"), "B");
		return baseDados;
	}

	public static String insert(String msg) {
		String[] split = msg.split(" ");
		BigInteger key = new BigInteger(split[1]);
		String value = split[2];
		baseDados.put(key, value);
		
		return "Inserido com sucesso";
	}

	public static String update(String msg) {
		String[] split = msg.split(" ");
		BigInteger key = new BigInteger(split[1]);
		String valueNovo = split[2];
		baseDados.remove(key);
		baseDados.put(key, valueNovo);
		return "Atualizado com sucesso";
	}

	public static String read(String msg) {
		String[] split = msg.split(" ");
		BigInteger key = new BigInteger(split[1]);
		return "O dado é : " + (String) baseDados.get(key);
	}

	public static String delete(String msg) {
		String[] split = msg.split(" ");
		BigInteger key = new BigInteger(split[1]);
		baseDados.remove(key);
		return "Deletado com sucesso";
	}
    
	public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
    
    public void run() {
    	// TODO Auto-generated method stub
    }

    public static void main( String[] args ) throws IOException, ClassNotFoundException
    {
//    	GreetServer server=new GreetServer();
//        server.start(9876);
    	//System.out.println("fausto teste teste2".split(" ")[0]);
    	 //create the socket server object
    	serverSocket = new ServerSocket(9876);
    	serverSocket.setReuseAddress(true);
    	baseDados = geraBaseDados(baseDados);
    	
        while(true){
            System.out.println("Waiting for the client request");
            Socket socket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            
            String message = (String) ois.readObject();
            System.out.println("Message Received: " + message);
            String[] crud = message.split(" ");
            String acao = null;
            
            if(crud[0].equals("GET")) { acao= read(message);}
            
            else if(crud[0].equals("PUT")) {acao= update(message);}
            
            else if(crud[0].equals("POST")) {acao =insert(message);}
            
            else if(crud[0].equals("DELETE")) { acao =delete(message);}
           
            else if(acao == null){
            	acao = "Não foi possivel encontrar o metodo!";
            }
            oos.writeObject(acao);
//            
//            String aux = "";
//            
//            for(BigInteger i:baseDados.keySet()) {
//            	 aux += ("Key:" + i + " Valor:" + baseDados.get(i) + "\n");
//            }
//
//            oos.writeObject(aux);
            oos.writeObject("Hi Client "+message);
            
            //close resources
            ois.close();
            oos.close();
            socket.close();
            
            if(message.equalsIgnoreCase("exit")) break;
        }
        
        System.out.println("Shutting down Socket server!!");
        
        //close the ServerSocket object
        serverSocket.close();
    }
    
}
