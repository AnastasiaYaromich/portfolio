package Chat.Server;
// В коде будет много комментариев. Мне так проще
// быстро ориентироваться.

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Vector;

public class MainServ {
    private Vector<ClientHandler> clients;

    public MainServ() {
        // Создаем специальный массив для хранения списка клиентов
        clients = new Vector<>();
        ServerSocket server = null;
        Socket socket = null;

        try {
            // Создаем подлючение сервера к базе данных
            AuthService.connect();
            //           AuthService.addUser("login1", "pass1", "nick1");
//            AuthService.addUser("login2", "pass2", "nick2");
//           AuthService.addUser("login3", "pass3", "nick3");
//                    AuthService.addUser("login4", "pass4", "nick4");
//            AuthService.addUser("login5", "pass5", "nick5");
//          AuthService.addUser("login6", "pass6", "nick6");
//            AuthService.addUser("login7", "pass7", "nick7");
//         AuthService.addUser("login8", "pass8", "nick8");
//            AuthService.addUser("login9", "pass9", "nick9");
//        AuthService.addUser("login10", "pass10", "nick10");
//            AuthService.addUser("login11", "pass11", "nick11");
//        AuthService.addUser("login12", "pass12", "nick12");

            // Создаем сервер
            server = new ServerSocket(5555);
            System.out.println("Сервер запущен!");
            while (true) {
                // Ожидаем подключения клиента
                socket = server.accept();
                System.out.println("Клиент подключился!");
                // Добавляем текущего клиента в список клиентов
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Закрываем сокет
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                // Закрываем сервер
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Отключаем соединение с базой данных
            AuthService.disconnect();
        }
    }

    public void subscribe(ClientHandler client) {
        // Добавляем нового клиента в список
        clients.add(client);
        broadcastClientsList();
    }

    public void unsubscribe(ClientHandler client) {
        // Удаляем клиента из списка
        clients.remove(client);
        broadcastClientsList();
    }

    // Метод для проверки не занят ли ник
    public boolean isNickBusyAndExist(String nick) {
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    // Метод для рассылки сообщений всем клиентам
    public void broadcastMsg(ClientHandler from, String msg) {
        for (ClientHandler o : clients) {
            if (!AuthService.checkBlackList(o.getNick(), from.getNick())) {
                o.sendMsg(msg);
            }
        }
    }


    public void personalMsg(ClientHandler from, String nickTo, String msg) {
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nickTo) && !AuthService.checkBlackList(o.getNick(), from.getNick())) {
                o.sendMsg("from " + from.getNick() + ": " + msg);
                from.sendMsg("to " + nickTo + ": " + msg);
                return;
            }
        }
        from.sendMsg("Клиент с ником " + nickTo + " не найден в чате");
    }

    public void broadcastClientsList() {
        StringBuilder sb = new StringBuilder();
        sb.append("/clientslist ");
        for (ClientHandler o : clients) {
            sb.append(o.getNick() + " ");
        }
        String out = sb.toString();
        for (ClientHandler o : clients) {
            o.sendMsg(out);
        }
    }



}

