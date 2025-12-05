import java.io.*;
import java.net.*;


public class Server {
    public static void main(String[] args) throws IOException {
        int port = 12345;


        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started");


        Socket client = serverSocket.accept();
        System.out.println("Client connected");


        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));


        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));


        while (true) {
            System.out.print("Server > ");
            String msg = console.readLine();
            out.write(msg + "\n");
            out.flush();


            if (msg.equals("/quit")) break;
        }


        client.close();
        serverSocket.close();
    }
}