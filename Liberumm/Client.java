import java.io.*;
import java.net.*;


public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 12345);


        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter your name: ");
        String name = console.readLine();
        out.write(name + "\n");
        out.flush();


        Thread reader = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Server: " + msg);
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


        socket.close();
    }
}