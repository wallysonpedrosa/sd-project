package trabalho_sd.sd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class GreetCliente implements Runnable{
	
		
	    private static Socket clientSocket;
	    private static PrintWriter out;
	    private static BufferedReader  in;

	 
	    public static void startConnection() throws UnknownHostException, IOException {
	    	InetAddress host = InetAddress.getLocalHost();
	        clientSocket = new Socket(host.getHostName(), 9876);
	        
	        out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in = BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    }
	 
	    private static BufferedReader BufferedReader(InputStreamReader inputStreamReader) {
			// TODO Auto-generated method stub
			return null;
		}

		public static String sendMessage(String msg) throws IOException {
	        out.println(msg);
	        String resp = in.readLine();
	        return resp;
	    }
	    
//	    public String getMessage() throws IOException {
//	    	 String message = (String) in.read;
//	        return message;
//	    }
	 
	    public void stopConnection() throws IOException {
	        in.close();
	        out.close();
	        clientSocket.close();
	    }

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public static void main( String[] args ) throws IOException, ClassNotFoundException, InterruptedException
    {
//		//GreetClient client = new GreetClient();
//		System.out.println("eai");
//		startConnection();
//		
//      	String response = sendMessage("hello server");
//      	System.out.println("eai");
//		System.out.println(response);
			
		    InetAddress host = InetAddress.getLocalHost();
	        Socket socket = null;
	        ObjectOutputStream oos = null;
	        ObjectInputStream ois = null;
	       
	        for(int i=0; i<2;i++){
	        	
	            socket = new Socket(host.getHostName(), 9876);
	            oos = new ObjectOutputStream(socket.getOutputStream());
	            System.out.println("Sending request to Socket Server");

	            oos.writeObject("GET 1");

	            ois = new ObjectInputStream(socket.getInputStream());
	            String message = (String) ois.readObject();
	            System.out.println("Message: " + message);
	            
	            //close resources
	            ois.close();
	            oos.close();
	            Thread.sleep(100);
	        }
		
    }
    
}
