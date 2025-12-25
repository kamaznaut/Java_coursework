import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        // Создаю сокет слушает порт 1234
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Сервер запущен и ждет подключений...");

            while (true) {
                // accept() ждет пока не подключится новый чел
                Socket socket = serverSocket.accept();
                System.out.println("Новое подключение!");

                // создаюобъект-обработчик и запускаю в новом потоке
                // Это позволяет серверу сразу вернуться к accept()
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Класс, который будет общаться с конкретным клиентом в своем потоке
class ClientHandler implements Runnable {
    private Socket socket; // Сокет конкретного клиента

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (// Получаем поток ввода, чтобы читать, что пишет клиент
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String msg;
            // Читаем сообщения, пока клиент не отключится
            while ((msg = in.readLine()) != null) {
                System.out.println("Сообщение от клиента: " + msg);
            }
        } catch (IOException e) {
            System.out.println("Клиент отключился.");
        }
    }
}