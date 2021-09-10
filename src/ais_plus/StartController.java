package ais_plus;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ais_plus.controller.MfcController;
import ais_plus.model.DataMfc_Model;
import ais_plus.model.Login_Model;
import ais_plus.model.Settings_Model;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class StartController {
    //private String cookie;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox saveMe_ch;

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
        saveMe_ch.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });
        final String[] cookie = {""};
        setLoginPassword();

        // Запуск прогресса индикации
        ProgressIndicator pi = new ProgressIndicator();
        VBox box = new VBox(pi);
        box.setAlignment(Pos.CENTER);
        bx.setDisable(true);
        root.getChildren().add(box);
        // Проверяем действительность куки
        Task CookieValidTask = new CookieValidTask(cookie[0]);
        // После выполнения потока
        CookieValidTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                // Закрытие прогресса индикации
                box.setDisable(true);
                pi.setVisible(false);
                bx.setDisable(false);
                cookie[0] =CookieValidTask.getValue().toString(); // Получаем значение куки
                if(!cookie[0].isEmpty()){ // Если куки не пустое
                    if (saveMe_ch.isSelected()){ // Если чекбокс выбран
                        Enter_Ais(cookie[0]); // Входим в главное окно программы
                    } else {
                        // При нажатии на кнопку "Вход"
                        login_button.setOnAction(eventLogin -> {
                            // Если чекбокс активен, запустить авторизацию со флагом True, иначе запустить авторизацию со флагом False
                            if (saveMe_ch.isSelected()) {
                                Login_Ais(true); // Вызов функции авторизации с флагом true
                            } else {
                                Login_Ais(false); // Вызов функции авторизации с флагом false
                            }
                        });
                        // При нажатии на Enter
                        login_input.setOnKeyPressed(keyEvent -> {
                            if (keyEvent.getCode() == KeyCode.ENTER)  {
                                // Если чекбокс активен, запустить авторизацию со флагом True, иначе запустить авторизацию со флагом False
                                if (saveMe_ch.isSelected()) {
                                    Login_Ais(true); // Вызов функции авторизации с флагом true
                                } else {
                                    Login_Ais(false); // Вызов функции авторизации с флагом false
                                    //NotLogin();
                                }
                            }
                        });
                        password_input.setOnKeyPressed(keyEvent -> {
                            if (keyEvent.getCode() == KeyCode.ENTER)  {
                                // Если чекбокс активен, запустить авторизацию со флагом True, иначе запустить авторизацию со флагом False
                                if (saveMe_ch.isSelected()) {
                                    Login_Ais(true); // Вызов функции авторизации с флагом true
                                } else {
                                    Login_Ais(false); // Вызов функции авторизации с флагом false
                                }
                            }
                        });
                    }
                } else {
                    // Вызов функции автоматической авторизации
                    ArrayList<Settings_Model> dataLogin=AutoAutoriz();
                    if (dataLogin.isEmpty()){ // Если нет данных в конф. файле
                        System.out.println("LOGIN IS EMPTY!");
                    }
                    // Если логин или пароль пустые
                    else if (dataLogin.get(0).getLogin().isEmpty() || dataLogin.get(0).getPassword().isEmpty())
                    {
                        saveMe_ch.setSelected(true); // Установить чекбокс активным
                    } else if (saveMe_ch.isSelected()){ // Если данные не пустые и чекбокс активен
                        String username_text = dataLogin.get(0).getLogin(); // Считать логин с файла
                        String password_text =dataLogin.get(0).getPassword(); // Считать пароль с файла
                        login_input.setText(username_text); // Установить логин в поле
                        password_input.setText(password_text); // Установить пароль в поле
                        Login_Ais(true); // Запустить авторизацию с флагом true
                    }
                    // При нажатии на кнопку "Вход"
                    login_button.setOnAction(eventLogin -> {
                        // Если чекбокс активен, запустить авторизацию со флагом True, иначе запустить авторизацию со флагом False
                        if (saveMe_ch.isSelected()) {
                            Login_Ais(true); // Вызов функции авторизации с флагом true
                        } else {
                            Login_Ais(false); // Вызов функции авторизации с флагом false
                        }
                    });
                    // При нажатии на Enter
                    login_input.setOnKeyPressed(keyEvent -> {
                        if (keyEvent.getCode() == KeyCode.ENTER)  {
                            // Если чекбокс активен, запустить авторизацию со флагом True, иначе запустить авторизацию со флагом False
                            if (saveMe_ch.isSelected()) {
                                Login_Ais(true); // Вызов функции авторизации с флагом true
                            } else {
                                Login_Ais(false); // Вызов функции авторизации с флагом false
                                //NotLogin();
                            }
                        }
                    });
                    password_input.setOnKeyPressed(keyEvent -> {
                        if (keyEvent.getCode() == KeyCode.ENTER)  {
                            // Если чекбокс активен, запустить авторизацию со флагом True, иначе запустить авторизацию со флагом False
                            if (saveMe_ch.isSelected()) {
                                Login_Ais(true); // Вызов функции авторизации с флагом true
                            } else {
                                Login_Ais(false); // Вызов функции авторизации с флагом false
                            }
                        }
                    });
                }

            }
        });
        // Запускаем поток для проверки куки
        Thread CookieValidThread = new Thread(CookieValidTask);
        CookieValidThread.setDaemon(true);
        CookieValidThread.start();


    }

    // Функция для установки логина и пароля в поле ввода
    public void setLoginPassword(){
        File file = new File("C:\\ais_plus\\settingsAIS.json");
        // Если файл не существует, то ничего не делать
        if(!file.exists())
        {
            System.out.println("No file!");
        } else { // Иначе
            System.out.println("yes file!");
            // Считываем данные с файла json
            JsonParser parser = new JsonParser();
            JsonElement jsontree = null;

            try {
                jsontree = parser.parse(new BufferedReader(new InputStreamReader(new FileInputStream("C:\\ais_plus\\settingsAIS.json"), StandardCharsets.UTF_8)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Парсим логин, пароль и флаг для активации чекбокса
            JsonObject jsonObject = jsontree.getAsJsonObject();
            String login = jsonObject.get("login").getAsString();
            String password = jsonObject.get("password").getAsString();
            boolean isCheckBoxSel=jsonObject.get("isCheckBoxSel").getAsBoolean();
            // Если флаг для чекбокса активен, то ставим активным чекбокс
            if (isCheckBoxSel) {
                saveMe_ch.setSelected(true);
                login_input.setText(login);
                password_input.setText(password);
            } else {
                saveMe_ch.setSelected(false);
                login_input.setText("");
                password_input.setText("");
            }
        }
    }

    // Функция автоматической авторизации
    public ArrayList<Settings_Model> AutoAutoriz() {
        // Создание списка данных о пользователе
        ArrayList<Settings_Model> dataLogin=new ArrayList<Settings_Model>();
        // Путь к файлу
        File file = new File("C:\\ais_plus\\settingsAIS.json");
        // Если файл не существует, то ничего не делать
        if(!file.exists())
        {
            System.out.println("No file!");
        } else { // Иначе
            System.out.println("yes file!");
            // Считываем данные с файла json
            JsonParser parser = new JsonParser();
            JsonElement jsontree = null;

            try {
                jsontree = parser.parse(new BufferedReader(new InputStreamReader(new FileInputStream("C:\\ais_plus\\settingsAIS.json"), StandardCharsets.UTF_8)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Парсим логин, пароль и флаг для активации чекбокса
            JsonObject jsonObject = jsontree.getAsJsonObject();
            String login=jsonObject.get("login").getAsString();
            String password=jsonObject.get("password").getAsString();
            boolean isCheckBoxSel=jsonObject.get("isCheckBoxSel").getAsBoolean();
            // Если флаг для чекбокса активен, то ставим активным чекбокс
            if (isCheckBoxSel) {saveMe_ch.setSelected(true);} else {saveMe_ch.setSelected(false);}
            System.out.println(login +":and: "+password);
            // Добавляем нужные данные в список
            dataLogin.add(new Settings_Model(login, password,"","",false));
        }
        return dataLogin;
    }

    // Функция для проверки действительности куки
    public static String ifCookie_valid() throws IOException {

        String cookie="";
        // Путь к файлу
        File file = new File("C:\\ais_plus\\settingsAIS.json");
        // Если файл не существует, то ничего не делать
        if(!file.exists())
        {
            System.out.println("No file!");
        } else { // Иначе
            System.out.println("yes file!");
            // Считываем данные с файла json
            JsonParser parser = new JsonParser();
            JsonElement jsontree = null;

            try {
                jsontree = parser.parse(new BufferedReader(new InputStreamReader(new FileInputStream("C:\\ais_plus\\settingsAIS.json"), StandardCharsets.UTF_8)));
                //jsontree = parser.parse(new FileReader("C:\\pkpvdplus\\settingsPVD.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Парсим и получаем значение куки с файла
            JsonObject jsonObject = jsontree.getAsJsonObject();
            cookie=jsonObject.get("cookie").getAsString();
            System.out.println("cookie from file "+cookie);

            CookieStore httpCookieStore = new BasicCookieStore();
            HttpClient httpClient = null;
            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
            httpClient = builder.build();
            //String getUrl       = "http://10.42.200.207/api/rs/reports/list";// Сервер авторизации
            String getUrl       = "AIS_URL";// Сервер авторизации!!!!!
            HttpGet httpGet = new HttpGet(getUrl);
            httpGet.setHeader("Content-type", "application/json");
            httpGet.addHeader("Cookie","JSESSIONID="+cookie);
            HttpResponse response = httpClient.execute(httpGet); // Выполняем get запрос для проверки действительности куки

            HttpEntity entity = response.getEntity();
            String result_of_req = EntityUtils.toString(entity); // Получаем результат запроса

            int status_code= response.getStatusLine().getStatusCode(); // Получаем код ответа от сервера
            System.out.println("Status cookie autor: "+status_code);
            boolean CookieValid;
            // Если код ответа 200, значит куки действителен, если 401 или другой, то недействителен
            switch (status_code){
                case 200:
                    CookieValid=true;
                    break;
                case 401: // Если выбраны организации
                    CookieValid=false;
                    cookie="";
                    break;
                default:
                    CookieValid=false;
                    cookie="";
                    break;
            }
        }

        return cookie; // Возвращаем значение куки
    }

    public void Login_Ais(boolean isCheckBoxSel){
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
            Task LoginTask = new LoginController.LoginTask(username_text,password_text, isCheckBoxSel);

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
    // Функция для входа в программу через куки
    public void Enter_Ais(String cookie){
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
    }

    public static class CookieValidTask extends Task<String> {
        private final String cookie; // Логин


        public CookieValidTask(String cookie) {
            this.cookie = cookie;

        }
        @Override
        protected String call() throws Exception {
            String check_cookie= ifCookie_valid();
            return check_cookie;
        }
    }

}



