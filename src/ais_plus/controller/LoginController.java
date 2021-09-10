package ais_plus.controller;

import ais_plus.appController;
import ais_plus.model.Settings_Model;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ais_plus.model.Login_Model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// Контроллер для авторизации в АИС
public class LoginController {

     public static class LoginTask extends Task<String> {
        private final String username;
        private final String password;
        private final boolean isCheckBoxSel; // Флаг чекбокса

        public LoginTask(String username, String password, boolean isCheckBoxSel) {
            this.username = username;
            this.password =password;
            this.isCheckBoxSel=isCheckBoxSel;
        }
        @Override
        protected String call() throws Exception {
            String cookie=Autoriz1(username,password);
            if (!cookie.equals("Login or password is incorrect!")){
                System.out.println("YES!");
                Autoriz2(username,password, cookie);
                Get_admin_role(cookie);
                SaveAutoriz(username, password, cookie, isCheckBoxSel);

            } else {
                System.out.println("NO!");
            }
            return cookie;
        }
    }

    // Функция для начальной авторизации
    public static String Autoriz1(String username, String password) throws IOException {
        Login_Model login_model =new Login_Model(); // Получаем модель авторизации
        CookieStore httpCookieStore = new BasicCookieStore(); // Для куки
        login_model.username =username; // Логин
        login_model.password =password; // Пароль
        String postUrl       = "http://192.168.99.91/cpgu/action/user/login"; // Адрес, к котрому будет отправляться POST-запрос
        Gson gson          = new Gson();  // Вызов экземпляра Gson
        HttpClient httpClient = null; // Создание httpClient
        // Установка cookie по-умолчанию
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl); // Создание экземпляра httpPost
        StringEntity postingString = new StringEntity(gson.toJson(login_model)); // Конвертируют payload с данными авторизации в json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json"); // Установка Header
        HttpResponse response = httpClient.execute(post); // Выполнение post-запросы
        List<Cookie> cookies = httpCookieStore.getCookies(); // Получение cookie
        //System.out.println("Login s incorrect!");
        String error="";
        // Если куки нет
        if (cookies.size()==0){
            error="Login or password is incorrect!";
            return error;
        } else {
            // Иначе взять первое значение куки
            return cookies.get(0).getValue();
        }
        //return cookies.get(0).getValue();

    }
    // Вторая авторизация с указанием cookie в header
    public static String Autoriz2(String username, String password, String cookie) throws IOException {

        Login_Model login_model =new Login_Model(); // Получаем модель авторизации
        CookieStore httpCookieStore = new BasicCookieStore(); // Для куки
        login_model.username = username; // Логин
        login_model.password= password; // Пароль
        String postUrl       = "http://192.168.99.91/cpgu/action/user/login";// Адрес, к котрому будет отправляться POST-запрос
        Gson gson          = new Gson(); // Вызов экземпляра Gson
        HttpClient httpClient = null; // Создание httpClient
        // Установка cookie по-умолчанию
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl); // Создание экземпляра httpPost
        StringEntity postingString = new StringEntity(gson.toJson(login_model));// Конвертируют payload с данными авторизации в json
        post.setEntity(postingString); // Установка json в post запрос
        post.setHeader("Content-type", "application/json"); // Установка Header
        post.addHeader("Cookie","JSESSIONID="+cookie); // Добавление cookie в header
        HttpResponse response = httpClient.execute(post); // Выполнение post запроса
        //String payl= gson.toJson(payload);
        return response.getStatusLine().toString(); // Возвращает статус запроса
        //return payl;

    }
    // Функция для получения прав администратора
    public static String Get_admin_role(String cookie) throws IOException {
        // Установка cookie по-умолчанию
        CookieStore httpCookieStore = new BasicCookieStore();
        Gson gson = new Gson(); // Вызов экземпляра gson

        Login_Model login_model = new Login_Model(); // Вызов экземпляр класса модели авторизации
        ArrayList<Login_Model> loginModelArr=new ArrayList<Login_Model>(); // Создание списка с данным по авторизации
        // заполнение payload для получения прав админа
        Object[] data1 = new Object[0];
        login_model.action="authController";
        login_model.method="getCurrentUser";
        login_model.data=data1;
        login_model.type="rpc";
        login_model.tid=1;

        // Добавление в список данных по авторизации
        loginModelArr.add(login_model);

        // Заполнение payload для получения прав админа
        Object[] data2 = new Object[2];
        data2[0] = 35149;
        data2[1] = "LOGIN";

        Login_Model login_model2 = new Login_Model(); // Вызов экземпляра класса модели авторизации
        login_model2.action="changeWorkingAttributesService";
        login_model2.method="setWorkingMfcId";
        login_model2.data=data2;
        login_model2.type="rpc";
        login_model2.tid=2;

        // Добавление в список данных по авторизации
        loginModelArr.add(login_model2);

        Object[] data3 = new Object[2];
        data3[0] = "ROLE_ADMIN";
        data3[1] = false;

        Login_Model login_model3 = new Login_Model(); // Вызов экземпляр класса модели авторизации
        login_model3.action="authController";
        login_model3.method="singleAuthorityLogin";
        login_model3.data=data3;
        login_model3.type="rpc";
        login_model3.tid=3;
        // Добавление в список данных по авторизации
        loginModelArr.add(login_model3);

        Login_Model login_model4 = new Login_Model(); // Вызов экземпляр класса модели авторизации
        login_model4.action="cpguConfigurationService";
        login_model4.method="getConfiguration";
        login_model4.data=null;
        login_model4.type="rpc";
        login_model4.tid=4;
        // Добавление в список данных по авторизации
        loginModelArr.add(login_model4);

        String postUrl       = "http://192.168.99.91/cpgu/action/router";// Адрес для получения прав вдмина
        String result_of_req="";
        // Идем по циклу и отправляем post-запросы для получения прав админа
        for (int i=0; i<4; i++){
            HttpClient httpClient = null;
            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
            httpClient = builder.build();
            HttpPost post          = new HttpPost(postUrl);
            // Конвертируем из каждого элемента списка payload в json
            StringEntity postingString = new StringEntity(gson.toJson(loginModelArr.get(i)));
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            post.addHeader("Cookie","JSESSIONID="+cookie); // Добавляем в Header cookie
            HttpResponse response = httpClient.execute(post); // Отправляем post-запрос
            HttpEntity entity = response.getEntity();
            result_of_req = EntityUtils.toString(entity); // Результат выполнения запроса
        }


        return result_of_req; // Возвращаем последний результат выполнения запроса
        //return payload_admin_arr;
    }

    // Функция для сохранения данных по авторизации
    private static void SaveAutoriz(String login, String password, String cookie , boolean isCheckBoxSel){
        // Получаем логин, пароль, куки, флаг чекбокса
        Settings_Model settingsModel=new Settings_Model(login, password, cookie,"", isCheckBoxSel);
        settingsModel.setLogin(login);
        settingsModel.setPassword(password);
        settingsModel.setCookie(cookie);
        settingsModel.setCheckBoxSel(isCheckBoxSel);
        Gson gson = new Gson();
        //StringEntity postingString = new StringEntity(gson.toJson(settingsModel), StandardCharsets.UTF_8);//gson.tojson() converts your payload to json
        System.out.println("json settings: "+ gson.toJson(settingsModel));
        // Создаем файл с настройками
        File f = new File("C:\\ais_plus");
        try{
            if(f.mkdir()) {
                System.out.println("Directory Created");
                String content=gson.toJson(settingsModel);
                File file=new File("C:\\ais_plus\\settingsAIS.json");
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
                out.write(content);
                out.close();
            } else {
                System.out.println("Directory is not created");
                SaveSettings(login, password, cookie, isCheckBoxSel);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // Функция для сохранения настроек
    public static void SaveSettings(String login, String password, String cookie, boolean isCheckBoxSel) throws IOException {
        // Путь к файлу
        File fileJson = new File("C:\\ais_plus\\settingsAIS.json");
        JsonParser parser = new JsonParser();
        JsonElement jsontree = null;
        try {
            jsontree = parser.parse(new BufferedReader(new InputStreamReader(new FileInputStream("C:\\ais_plus\\settingsAIS.json"), StandardCharsets.UTF_8)));
            //jsontree = parser.parse(new FileReader("C:\\pkpvdplus\\settingsPVD.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Парсим данные
        JsonObject jsonObject = jsontree.getAsJsonObject();
        String lastPathToFile = jsonObject.get("lastPathToFile").getAsString();
        Settings_Model settingsModel = new Settings_Model(login, password, cookie, lastPathToFile, isCheckBoxSel);
        settingsModel.setLogin(login);
        settingsModel.setPassword(password);
        settingsModel.setCookie(cookie);
        settingsModel.setLastPathToFile(lastPathToFile);
        settingsModel.setCheckBoxSel(isCheckBoxSel);
        // Сохраняем настройки
        Gson gson = new Gson();
        String content = gson.toJson(settingsModel);
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileJson), StandardCharsets.UTF_8));
        out.write(content);
        out.close();

    }

}


