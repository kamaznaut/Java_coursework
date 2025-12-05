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


        Thread reader = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Client: " + msg);
                }
            } catch (IOException ignored) {}
        });


        reader.start();


        String msg;
        while ((msg = console.readLine()) != null) {
            out.write(msg + "\n");
            out.flush();
            if (msg.equals("/quit")) break;
        }


        client.close();
        serverSocket.close();
    }
}