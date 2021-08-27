package Chat.Client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller {

    @FXML
    TextArea textArea;

    @FXML
    TextField textField;

    @FXML
    HBox bottomPanel;

    @FXML
    HBox upperPanel;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button authButton;

    @FXML
    ListView<String> clientsList;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    private int index;

    final String IP_ADRESS = "localhost";
    final  int PORT = 5555;

    private boolean isAuthorized;

    // Метод определющий видимость окон в зависимости
    // от состояния авторизации.
    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
        if (!isAuthorized) {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            bottomPanel.setVisible(false);
            bottomPanel.setManaged(false);
            clientsList.setVisible(false);
            clientsList.setManaged(false);
        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            bottomPanel.setVisible(true);
            bottomPanel.setManaged(true);
            clientsList.setVisible(true);
            clientsList.setManaged(true);
        }
    }

    public void connect() {
        try {
            socket = new Socket(IP_ADRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // На текущий момент мы не авторизованы.
            // Вызываем метод setAuthorized для установки видимости панелей.
            setAuthorized(false);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            // Читаем входящий поток с сервера
                            String str = in.readUTF();
                            // Если полученное с сервера сообщение начинается с authok
                            // делаем следующее -->
                            if (str.startsWith("/authok")) {
                                // На текущий момент мы авторизованы.
                                // Вызываем метод setAuthorized для установки видимости панелей.
                                setAuthorized(true);
                                break;
                            } else {
                                // Если полученное с сервера сообщение не начинается с authok
                                // добавляем в textArea сообщение неверный логин и пароль
//                                msg.add(new Label(str + "\n"));
//                                if (index % 2 == 0) {
//                                    msg.get(index).setAlignment(Pos.CENTER_LEFT);
//                                    System.out.println("1");
//                                } else {
//                                    msg.get(index).setAlignment(Pos.CENTER_RIGHT);
//                                    System.out.println("2");
                                textArea.appendText(str + "\n");
//                                }
//                                chatBox.getChildren().add(msg.get(index));
//                                index++;
                            }
                        }

                        while (true) {
                            // Читаем входящий поток с сервера
                            // если сообщение сервера содержит строку
                            // serverclosed завершаем работу
                            String str = in.readUTF();
                            if (str.equals("/serverclosed")) {
                                break;
                            }
                            if (str.startsWith("/clientslist ")) {
                                String[] tokens = str.split(" ");
                                Platform.runLater(() -> {
                                    clientsList.getItems().clear();
                                    for (int i = 1; i < tokens.length; i++) {
                                        clientsList.getItems().add(tokens[i]);
                                    }
                                });
                                // Добавляем в textArea сообщение /serverclosed
                            } else {
                                //   textArea.appendText(str + "\n");
//                                msg.add(new Label(str + "\n"));
//                                if (index % 2 == 0) {
//                                    msg.get(index).setAlignment(Pos.CENTER_LEFT);
//                                    System.out.println("1");
//                                } else {
//                                    msg.get(index).setAlignment(Pos.CENTER_RIGHT);
//                                    System.out.println("2");
                                textArea.appendText(str + "\n");
//                                }
//                                chatBox.getChildren().add(msg.get(index));
//                                index++;
                            }
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
                        // На текущий момент мы не авторизованы.
                        setAuthorized(false);
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
        try {
            // Клиент написал сообщение в textField
            // и нажал конпку Send.
            // отправляем сообщение на сервер
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // При нажатии кнопки tryToAuth вызывается данный метод
    public void tryToAuth() {
        // Если соединения с сервером нет - создаем сокет, вызвав метод connect()
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            // Отправляем серверу сообщение для с логином и паролем клиента
            out.writeUTF("/auth " + loginField.getText() + " " + passwordField.getText());
            loginField.clear();
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



