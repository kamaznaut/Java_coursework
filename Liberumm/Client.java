import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String SERVER_IP = "127.0.0.1"; // Локальный адрес (тот же компьютер)
        int PORT = 1234;

        try (Socket socket = new Socket(SERVER_IP, PORT)) {
            // Поток для отправки текста на сервер
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            // ОТДЕЛЬНЫЙ ПОТОК ДЛЯ ЧТЕНИЯ (чтобы видеть сообщения от других в любое время)
            Thread readerThread = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String serverMsg;
                    // Цикл постоянно ждет входящую строку от сервера
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println("\n" + serverMsg);
                        System.out.print("> "); // Подсказка для ввода
                    }
                } catch (IOException e) {
                    System.out.println("Соединение с сервером закрыто.");
                }
            });
            // фоновый поток чтения
            readerThread.start();

            // ОСНОВНОЙ ПОТОК ДЛЯ ВВОДА
            System.out.println("Подключено к чату.");
            while (true) {
                // ждем пока пользователь наберет текст и нажмет Enter
                if (scanner.hasNextLine()) {
                    String msg = scanner.nextLine();
                    // Если написать exit — клиент закроется чтобы не использовать ctrl + c
                    if (msg.equalsIgnoreCase("exit")) break;
                    // Отправляем строку на сервер
                    out.println(msg);
                }
            }

        } catch (IOException e) {
            System.err.println("Не удалось подключиться к серверу. Убедитесь, что он запущен.");
        }
    }
}