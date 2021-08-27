package Chat.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {
    private MainServ serv;
    private Socket socket;
    private String nick;
    DataInputStream in;
    DataOutputStream out;

    // Создаем ссылку на объект, который позволяет установить соединение
    // между сервером и базой данных
    private static Connection connection;

    // Создаем ссылку на объект, с помощью которого мы сможем
    // создавать запросы к базе данных и получать результат из базы
    private static Statement stmt;

    public ClientHandler(MainServ serv, Socket socket) {
        try {
            this.serv = serv;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            //    this.blackList = new ArrayList<>();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            // Считываем входящий поток от клиента
                            String str = in.readUTF();
                            // Если строка переданная клиентом начинается
                            // c /auth выполняем следующее -->
                            if (str.startsWith("/auth")) {
                                // Создаем массив, содержащий логин и пароль,
                                // переданные клиентом
                                String[] tokens = str.split(" ");
                                // Вызываем метод getNickByLoginAndPass передавая в качестве
                                // аргуметов логин и пароль клиента
                                String currentNick = AuthService.getNickByLoginAndPass(tokens[1], tokens[2]);
                                // Если верувшаяся из базы данных строка не null делаем следующее -->
                                if (currentNick != null) {
                                    if (!serv.isNickBusyAndExist(currentNick)) {
                                        // Вызываем метод sendMsg c аргуметом /authok
                                        sendMsg("/authok");
                                        // Присваиваем переменной nick значение строки currentNick
                                        nick = currentNick;
                                        // Вызываем метод subscribe передавая в качестве
                                        // аргумента текущего клиента
                                        serv.subscribe(ClientHandler.this);
                                        break;
                                    } else {
                                        // Отправляем сообщение что учетная запись уже используется
                                        sendMsg("Учетная запись уже используется");
                                    }
                                } else {
                                    // Если верувшаяся из базы данных строка null делаем следующее -->
                                    // Вызываем метод sendMsg c аргуметом "Неверный логин/пароль"
                                    sendMsg("Неверный логин/пароль");
                                }
                            }
                        }


                        while (true) {
                            // Читаем входящий поток от клиента
                            String str = in.readUTF();
                            // Проверяем, если сообщение клиента заканчивается /end
                            // оправляем ему сообщение /serverclosed
                            if (str.startsWith("/")) {
                                if (str.equals("/end")) {
                                    out.writeUTF("/serverclosed");
                                    break;
                                }
                                // Иначе рассылаем сообщение данного клиента
                                // всем клиентам на сервере либо одному клиенту
                                // если это личное сообщение
                                if (str.startsWith("/w")) {
                                    // реализация личных сообщений
                                    String[] tokens = str.split(" ", 3);
                                    serv.personalMsg(ClientHandler.this, tokens[1], tokens[2]);
                                }
                                if (str.startsWith("/blacklist ")) {
                                    String[] tokens = str.split(" ");
                                    // проверка что добаляемый в blacklist ник существует
                                    if (serv.isNickBusyAndExist(tokens[1]) && !getNick().equals(tokens[1])) {
                                        AuthService.addBlackList(getNick(), tokens[1]);
                                        //  blackList.add(tokens[1]);
                                        sendMsg(tokens[1] + " добавлен в черный" + "\n" + " список");
                                    } else {
                                        if (getNick().equals(tokens[1])) {
                                            sendMsg("Нельзя добавлять себя " + "\n" + "в черный список");
                                        }
                                        if (!serv.isNickBusyAndExist(tokens[1])) {
                                            sendMsg("Пользователь под ником " + "\n" + tokens[1] + " не существует");
                                        }
                                    }
                                }
                            } else {
                                serv.broadcastMsg(ClientHandler.this, nick + ": " + str);
                            }
                            System.out.println("Client: " + str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            // Закрываем исходящий поток
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            // Закрываем входящий поток
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            // Закрываем сокет
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Удаляем клиента из списка клиентов
                        serv.unsubscribe(ClientHandler.this);
                    }

                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            // Отправляем клиенту сообщение
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public String getNick() {
        return nick;
    }
}