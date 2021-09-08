package ais_plus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
        primaryStage.setTitle("Ais plus"); // Название приложения
        //JMetro jMetro=new JMetro(root, Style.LIGHT);
        primaryStage.setScene(new Scene(root, 400, 200)); // Изначальный размер окна
        primaryStage.show(); // Запустить окно приложения
        primaryStage.setResizable(false); // Сделать размер окна неизменяемым
    }


    public static void main(String[] args) {
        launch(args);
    }
}
