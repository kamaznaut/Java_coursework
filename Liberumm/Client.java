import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String SERVER_IP = "192.168.0.10"; //  IP СЕРВЕРА
        int PORT = 1234;

        try (Socket socket = new Socket(SERVER_IP, PORT)) {

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            //отправляем серверу имя
            out.println(name);

            System.out.println("Connected. Type messages:");

            while (true) {
                String msg = scanner.nextLine();
                out.println(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
