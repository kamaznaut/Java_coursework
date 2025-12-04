import java.io.IOException;
import java.net.Socket;


public class Client {
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 12345;


        Socket socket = new Socket(host, port);
        System.out.println("Connected to server");


        socket.close();
    }
}