import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 1234)) {

            // создаю отдельный поток для прослушивания сервера
            // чтобы чтение сообщений не мешало нам печатать свои
            new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String serverMsg;
                    // цикл постоянно ждет строку от сервера
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println("\nСервер: " + serverMsg);
                        System.out.print("> "); // просто визуальный маркер ввода
                    }
                } catch (IOException e) {
                    System.out.println("Соединение потеряно.");
                }
            }).start();

            // поток вывода (пишем на сервер)
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String myMsg = scanner.nextLine(); // ждем ввод пользователя
                out.println(myMsg); // отправляем на сервер
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}