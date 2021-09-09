package ais_plus;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ais_plus.controller.MfcController;
import ais_plus.model.DataMfc_Model;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ais_plus.controller.LoginController;
import javafx.stage.WindowEvent;

public class StartController {
    //private String cookie;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button login_button;

    @FXML
    private TextField login_input;

    @FXML
    private PasswordField password_input;

    @FXML
    private Label error_t;

    @FXML
    private StackPane root;

    @FXML
    private VBox bx;


    @FXML
    void initialize() {
        login_button.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });
        // При нажатии на кнопку "Вход"
        login_button.setOnAction(event -> {
            Login_Ais(); // Вызов функции авторизации
        });
        // При нажатии на Enter
        login_input.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                Login_Ais(); // Вызов функции авторизации
            }
        });
        password_input.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                Login_Ais(); // Вызов функции авторизации
            }
        });
    }

    public void Login_Ais(){
            // Запуск прогресса индикации
            ProgressIndicator pi = new ProgressIndicator();
            VBox box = new VBox(pi);
            box.setAlignment(Pos.CENTER);
            // Grey Background
            bx.setDisable(true);
            root.getChildren().add(box);
            //System.out.println("Test");

            String username_text = login_input.getText(); // Считывание логина
            String password_text = password_input.getText(); // Считывание пароля
            // Инициализация потока с авторизацией
            Task LoginTask = new LoginController.LoginTask(username_text,password_text );

            // После выполнения потока
            LoginTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    // Закрытие прогресса индикации
                    box.setDisable(true);
                    pi.setVisible(false);
                    bx.setDisable(false);
                    System.out.println(LoginTask.getValue());
                    String cookie="";
                    cookie=LoginTask.getValue().toString();

                    // Если авторизация прошла успешно
                    if (!cookie.equals("Login or password is incorrect!")){
                        login_button.getScene().getWindow().hide();// Скрываем окно авторизации
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/ais_plus/view/app.fxml"));
                        try {
                            loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Parent root = loader.getRoot();
                        // Вызываем контроллер основного окна программы
                        appController AppController = loader.getController();


                        try {
                            AppController.Show_data_uslug(cookie); // Вызов функции заполнения данных по услугам
                            AppController.Show_data_Departm(cookie); // Вызов функции заполнения данных по ведомствам
                            AppController.Show_data_Mfc(cookie); // Вызов функции заполнения данных по мфц
                            AppController.Show_accruals(cookie); // Вызов функции для получения начислений по услуге
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Запускаем основное окно программы АИС
                        Stage stage = new Stage();
                        // Подтверждение выхода из приложения
                        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            public void handle(WindowEvent we) {
                                System.out.println("Stage is closing");
                                ButtonType yes_del = new ButtonType("Да", ButtonBar.ButtonData.OK_DONE); // Создание кнопки подтвердить
                                ButtonType no_del = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE); // Создание кнопки отменить
                                Alert alert =new Alert(Alert.AlertType.CONFIRMATION , "Test", yes_del, no_del);
                                alert.setTitle("Выход из приложения"); // Название предупреждения
                                alert.setHeaderText("Подтвердите выход из приложения!"); // Текст предупреждения
                                alert.setContentText("Вы действительно хотите выйти из приложения?");
                                // Вызов подтверждения элемента
                                alert.showAndWait().ifPresent(rs -> {
                                    if (rs == yes_del){
                                        System.out.println("Exit!");
                                        Platform.exit();
                                        System.exit(0);
                                    } else if(rs ==no_del){
                                        we.consume();
                                    }
                                });
                            }
                        });
                        stage.setTitle("Ais Plus");
                        //stage.setResizable(false);
                        stage.setScene(new Scene(root));
                        stage.showAndWait();



                    } else {
                        // Иначе - если авторизация не удалась , то ошибка
                        error_t.setVisible(true);
                        System.out.println("Error!");
                    }
                }
            });

            // Запуск потока
            Thread loginThread = new Thread(LoginTask);
            loginThread.start();
    }

}



