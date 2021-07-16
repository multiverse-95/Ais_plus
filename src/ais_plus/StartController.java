package ais_plus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ais_plus.controller.LoginController;

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
        Login_Ais(); // Вызов функции авторизации
    }

    public void Login_Ais(){

        login_button.setOnAction(event -> {
            ProgressIndicator pi = new ProgressIndicator();
            VBox box = new VBox(pi);
            box.setAlignment(Pos.CENTER);
            // Grey Background
            bx.setDisable(true);
            root.getChildren().add(box);
            //System.out.println("Test");
            String username_text= login_input.getText(); // Считывание логина
            String password_text =password_input.getText(); // Считывание пароля
            Task task1 = new LoginController.Task1(username_text,password_text );

            //SetOnSucceeded methode
            task1.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    box.setDisable(true);
                    pi.setVisible(false);
                    bx.setDisable(false);
                    System.out.println(task1.getValue());
                    String cookie="";
                    cookie=task1.getValue().toString();

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
                            AppController.Show_accruals(cookie); // Вызов функции для получение начислений по услуге
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Запускаем основное окно программы АИС
                        Stage stage = new Stage();
                        stage.setTitle("Ais Plus");
                        stage.setResizable(false);
                        stage.setScene(new Scene(root));
                        stage.showAndWait();

                    } else {
                        // Иначе - если авторизация не удалась , то ошибка
                        error_t.setVisible(true);
                        System.out.println("Error!");
                    }
                }
            });

            //bind progress bar to both task progress property

            Thread loadingThread = new Thread(task1);
            loadingThread.start();


        });

       /* login_button.setOnAction(event -> {


            String username_text= login_input.getText(); // Считывание логина
            String password_text =password_input.getText(); // Считывание пароля
            String status_line ="";
            System.out.println("Login success! \nLogin: "+username_text+"\nPassword: "+ password_text);
            LoginController loginController = new LoginController(); // Вызов контроллера авторизации
            try {
                cookie = loginController.Autoriz1(username_text,password_text); // Получение cookies
                status_line= loginController.Autoriz2 (username_text,password_text, cookie);
                loginController.Get_admin_role(cookie); // Получение админских прав
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Cookie: "+ cookie);
            System.out.println("Status: "+ status_line);
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
                    AppController.Show_accruals(cookie); // Вызов функции для получение начислений по услуге
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Запускаем основное окно программы АИС
                Stage stage = new Stage();
                stage.setTitle("Ais Plus");
                stage.setResizable(false);
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } else {
                // Иначе - если авторизация не удалась , то ошибка
                error_t.setVisible(true);
                System.out.println("Error!");
            }
        });*/

    }

}



