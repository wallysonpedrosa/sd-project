package trabalho_sd.sd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
private Socket client;
private static ServerSocket server;
private static int port = 9876;

    public static void main( String[] args ) throws IOException
    {
    	GreetServer server=new GreetServer();
        server.start(port);
    }
    
    
//    @Test
//    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
//        GreetClient client = new GreetClient();
//        client.startConnection("127.0.0.1", 6666);
//        String response = client.sendMessage("hello server");
//        assertEquals("hello client", response);
//    }
//    
    
    
//    @Test
//    public void givenClient_whenServerEchosMessage_thenCorrect() {
//        String resp1 = client.sendMessage("hello");
//        String resp2 = client.sendMessage("world");
//        String resp3 = client.sendMessage("!");
//        String resp4 = client.sendMessage(".");
//         
//        assertEquals("hello", resp1);
//        assertEquals("world", resp2);
//        assertEquals("!", resp3);
//        assertEquals("good bye", resp4);
//    }
}
