String msg;
            while ((msg = in.readLine()) != null) {
        // Если клиент ввел /list
        if (msg.equalsIgnoreCase("/list")) {
        // Превращаем список ключей Map в одну строку и отправляем клиенту
        out.println("Сейчас в сети: " + String.join(", ", Server.clients.keySet()));
        }
        // Если сообщение начинается с @ (например @Ivan привет)
        else if (msg.startsWith("@")) {
String[] parts = msg.split(" ", 2); // Делим строку на 2 части: [@Ivan] и [привет]
String targetName = parts[0].substring(1); // Убираем символ @, получаем "Ivan"

// Ищем обработчик Ивана в нашей Map
ClientHandler target = Server.clients.get(targetName);
                    if (target != null && parts.length > 1) {
        // Отправляем сообщение напрямую в поток вывода Ивана
        target.out.println("[Личное от " + clientName + "]: " + parts[1]);
                    } else {
                            out.println("Пользователь " + targetName + " не найден.");
                    }
                            }
                            }