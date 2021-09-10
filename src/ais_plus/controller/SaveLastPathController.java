package ais_plus.controller;

import ais_plus.model.Settings_Model;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SaveLastPathController {

    // Функция для сохранения последнего пути файла
    public static void SaveLastPathInfo(String lastPathToFile) throws IOException {
        // Путь к файлу
        File fileJson = new File("C:\\ais_plus\\settingsAIS.json");
        // Проверяем, существует ли файл
        if(!fileJson.exists())
        {
            System.out.println("No file!");
        } else {
            System.out.println("yes file!");
            // Читаем в кодировке UTF-8
            JsonParser parser = new JsonParser();
            JsonElement jsontree = null;
            try {
                jsontree = parser.parse(new BufferedReader(new InputStreamReader(new FileInputStream("C:\\ais_plus\\settingsAIS.json"), StandardCharsets.UTF_8)));
                //jsontree = parser.parse(new FileReader("C:\\pkpvdplus\\settingsPVD.json"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Парсим нужные данные
            JsonObject jsonObject = jsontree.getAsJsonObject();
            String login=jsonObject.get("login").getAsString();
            String password=jsonObject.get("password").getAsString();
            String cookie=jsonObject.get("cookie").getAsString();
            boolean isCheckBoxSel=jsonObject.get("isCheckBoxSel").getAsBoolean();
            Settings_Model settingsModel=new Settings_Model(login, password, cookie, lastPathToFile, isCheckBoxSel);
            settingsModel.setLogin(login);
            settingsModel.setPassword(password);
            settingsModel.setCookie(cookie);
            settingsModel.setLastPathToFile(lastPathToFile);
            settingsModel.setCheckBoxSel(isCheckBoxSel);
            // Сохраняем путь к файлу
            Gson gson = new Gson();
            File f = new File("C:\\ais_plus");
            try{
                if(f.mkdir()) {
                    System.out.println("Directory Created");
                } else {
                    System.out.println("Directory is not created");
                }
            } catch(Exception e){
                e.printStackTrace();
            }

            String content=gson.toJson(settingsModel);
            // Записываем в кодировке UTF-8
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileJson), StandardCharsets.UTF_8));
            out.write(content);
            out.close();
        }
    }

    // Функция для получения последней директории, где был сохранён отчёт
    public String getLastDirectory(){
        String lastPathToFile="";
        // Читаем файл
        File fileJson = new File("C:\\ais_plus\\settingsAIS.json");

        if(!fileJson.exists())
        {
            System.out.println("No file!");
        } else {
            System.out.println("yes file!");
            // Парсим нужные данные
            JsonParser parser = new JsonParser();
            JsonElement jsontree = null;
            try {
                jsontree = parser.parse(new BufferedReader(new InputStreamReader(new FileInputStream("C:\\ais_plus\\settingsAIS.json"), StandardCharsets.UTF_8)));
                //jsontree = parser.parse(new FileReader("C:\\pkpvdplus\\settingsPVD.json"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Получаем путь к файлу
            JsonObject jsonObject = jsontree.getAsJsonObject();
            lastPathToFile = jsonObject.get("lastPathToFile").getAsString();
        }
        return lastPathToFile;
    }
}
