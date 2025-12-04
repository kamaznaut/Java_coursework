import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) throws IOException {
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);


        Socket client = serverSocket.accept();
        System.out.println("Client connected: " + client.getRemoteSocketAddress());


        client.close();
        serverSocket.close();
    }
}