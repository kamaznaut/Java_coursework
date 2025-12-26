import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    // порт, который будет слушать сервер
    private static final int PORT = 1234;
    // безопасная карта для хранения активных клиентов: Имя -> Обработчик (ClientHandler)
    private static Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Сервер запущен...");

        // создаю серверный сокет
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // ждем нового клиента. Метод замирает, пока кто-то не подключится
                Socket socket = serverSocket.accept();
                System.out.println("Новое соединение установлено.");

                // создаем новый поток для каждого клиента, чтобы сервер не зависал
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // внутренний класс для обработки общения с каждым клиентом отдельно
    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String name;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // поток для отправки данных клиенту (true - автоочистка буфера)
                out = new PrintWriter(socket.getOutputStream(), true);
                // поток для чтения данных от клиента
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //  сервер просит клиента ввести имя при первом входе
                out.println("Введите ваш никнейм для входа:");
                this.name = in.readLine();

                if (this.name == null || this.name.trim().isEmpty()) {
                    socket.close();
                    return;
                }

                // добавляю клиента в общую карту (адресную книгу)
                clients.put(name, this);
                broadcast("System: " + name + " присоединился к чату!");
                out.println("Добро пожаловать! Команды: /list - список людей, @имя текст - личное сообщение.");

                // основной цикл: слушаем, что присылает клиент
                String input;
                while ((input = in.readLine()) != null) {
                    // команда просмотра списка пользователей
                    if (input.equalsIgnoreCase("/list")) {
                        out.println("В сети: " + String.join(", ", clients.keySet()));
                    }
                    // личные сообщения (начинаются с @)
                    else if (input.startsWith("@")) {
                        handlePrivateMessage(input);
                    }
                    // если введен просто текст без команд
                    else {
                        out.println("System: Используйте @имя для сообщения или команду /list.");
                    }
                }
            } catch (IOException e) {
                // если клиент просто закрыл программу
            } finally {
                // убираем клиента из списка и закрываем сокет
                closeConnection();
            }
        }

        // метод для отправки личного сообщения
        private void handlePrivateMessage(String input) {
            // ожидаемый формат: "@Имя сообщение"
            int firstSpace = input.indexOf(" ");
            if (firstSpace != -1) {
                String targetName = input.substring(1, firstSpace); // берем всё между @ и пробелом
                String message = input.substring(firstSpace + 1);   // всё, что после пробела

                ClientHandler target = clients.get(targetName);
                if (target != null) {
                    target.out.println("[Личное от " + name + "]: " + message);
                } else {
                    out.println("System: Пользователь " + targetName + " не найден.");
                }
            }
        }

        // Рассылка системного сообщения всем пользователям
        private void broadcast(String message) {
            for (ClientHandler client : clients.values()) {
                client.out.println(message);
            }
        }

        // завершение работы
        private void closeConnection() {
            if (name != null) {
                clients.remove(name);
                broadcast("System: " + name + " покинул чат.");
            }
            try { socket.close(); } catch (IOException e) {}
        }
    }
}