import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        int port = 1234;

        System.out.println("Server starting on port " + port + "...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Waiting for client...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected from " + socket.getInetAddress());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            // чтение никнейма
            String clientName = in.readLine();
            final String name = (clientName == null || clientName.isEmpty())
                    ? "Client"
                    : clientName;

            System.out.println("Client name: " + name);

            // чтение сообщения
            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println(name + ": " + msg);
            }

            System.out.println(name + " disconnected.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
