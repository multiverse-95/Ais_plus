package ais_plus.controller;

import ais_plus.appController;
import com.google.gson.*;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ais_plus.model.DataUslug_Model;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

// Контроллер для услуг
public class ServiceController {

    public static class ServiceTask extends Task<result_service> {
        private final String cookies;
        private final String search_text;
        private final int limit_usl;
        private boolean isCheckBox_check;

        public ServiceTask(String cookies, String search_text, int limit_usl, boolean isCheckBox_check) {
            this.cookies = cookies;
            this.search_text = search_text;
            this.limit_usl=limit_usl;
            this.isCheckBox_check= isCheckBox_check;
        }
        @Override
        protected result_service call() throws Exception {
            int limit_uslug =Total_usl_from_serv(Get_data_uslug(cookies, search_text, limit_usl));
            String result_json= Get_data_uslug(cookies, search_text, limit_uslug);

            result_service parsed_result= Parsing_json_uslug(result_json, limit_uslug, isCheckBox_check);

            return parsed_result;
        }
    }
    // Функция получения данных по услугам
    public static String Get_data_uslug(String cookie, String search_text, int limit_usl) throws IOException {

        // Хранилище куки
        CookieStore httpCookieStore = new BasicCookieStore();
        Gson gson = new Gson();
        // Создание экземпляра класса для payload услуг
        Payload_uslug payload_uslug =new Payload_uslug();
        // Создание экземпляра класса для data услуг
        Data_uslug data_uslugs = new Data_uslug();
        // Создание экземпляра класса для сортировки услуг
        Sort_uslug sort_uslug = new Sort_uslug();

        ArrayList<String> null_numb = new ArrayList<String>();
        ArrayList<Data_uslug> data_uslug = new ArrayList<Data_uslug>(); // Список для данных услуг
        ArrayList<Sort_uslug> sort_usl = new ArrayList<Sort_uslug>(); // Список для сортировки услуг

        // Данные для сортировки услуг
        sort_uslug.property="title";
        sort_uslug.direction="ASC";
        sort_usl.add(sort_uslug);

        // Основные данные для data услуг
        data_uslugs.serviceType = null;
        data_uslugs.requesterType = null;
        data_uslugs.departmentFilter = null;
        data_uslugs.searchString = search_text;
        data_uslugs.page = 1;
        data_uslugs.start = 0;
        data_uslugs.limit = limit_usl;
        data_uslugs.sort = sort_usl;
        data_uslug.add(data_uslugs);

        // Основные данные для payload услуг
        payload_uslug.action ="customServiceInfoService";
        payload_uslug.method ="getCustomServices";
        payload_uslug.data =data_uslug;
        payload_uslug.type ="rpc";
        payload_uslug.tid =11;


        String postUrl       = "http://192.168.99.91/cpgu/action/router";// Адрес запроса

        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(payload_uslug), "UTF-8"); //Конвертирование данных в json, в кодировке UTF-8
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json;charset=UTF-8");
        post.addHeader("Cookie","JSESSIONID="+cookie); // Добавление куки в Header
        HttpResponse response = httpClient.execute(post); // Выполнение post запроса
        HttpEntity entity = response.getEntity();
        System.out.println(gson.toJson(payload_uslug));
        //System.out.println(EntityUtils.toString(entity));
        return EntityUtils.toString(entity);
        //return gson.toJson(payload_uslug);
    }
    // Функция для получения количества всех услуг на сервере
    public static int Total_usl_from_serv(String json){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray jsonArray =element.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        JsonObject result = jsonObject.get("result").getAsJsonObject(); // Получение объекта result
        JsonArray records= result.get("records").getAsJsonArray(); // Получение массива records
        String title_usl="";
        int limit_usl_fromServer=jsonObject.get("result").getAsJsonObject().get("total").getAsInt(); // Получение количества услуг
        if (limit_usl_fromServer==0) limit_usl_fromServer=25;
        return limit_usl_fromServer;
    }
    // Функция для парсинга данных с услуг
    public static result_service Parsing_json_uslug(String json, int limit_usl, boolean isCheckBox_check) throws IOException {

        // Создания экземпляра парсинга
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json); // Получение главного элемента
        JsonArray jsonArray =element.getAsJsonArray(); // Получение массива элеменнтов
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject(); // Получение первого элемента из массива
        JsonObject result = jsonObject.get("result").getAsJsonObject(); // Получение элемента result
        JsonArray records= result.get("records").getAsJsonArray(); // Получение массива records
        String title_usl="";
        int limit_usl_fromServer=jsonObject.get("result").getAsJsonObject().get("total").getAsInt(); // Получение количества всех услуг
        System.out.println("Total "+limit_usl_fromServer);
        // Создание списка услуг
        ArrayList<DataUslug_Model> dataUslug_models_arr = new ArrayList<DataUslug_Model>();
        int SumAllUslug =limit_usl_fromServer; // Сумма всех услуг
        int SumActualUslug =0;

        // Если количество услуг больше, чем на сервере
        if (limit_usl>limit_usl_fromServer){
            limit_usl=limit_usl_fromServer; // Присвоить лимит услуг лимитом услуг с сервера
            System.out.println("LIMIT_USLUG_OUT "+limit_usl);
            // Проверка чекбокса
            if (isCheckBox_check){
                // Если выбран чекбокс только актуальные услуги
                System.out.println("CheckBox is selected! >");
                // Идем по циклу и добавляем в список услуг только актуальные услуги
                for (int i=0; i<limit_usl; i++){
                    if(!records.get(i).getAsJsonObject().get("notRender").getAsBoolean()){
                        SumActualUslug++;
                        dataUslug_models_arr.add(new DataUslug_Model(records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("eid").getAsString(),
                                records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("lid").getAsString(),
                                records.get(i).getAsJsonObject().get("title").getAsString()));
                    }
                }

            } else {
                // Если чекбокс не выбран то добавляем все услуги
                System.out.println("CheckBox is NOT selected! <");
                for (int i=0; i<limit_usl; i++){
                    if(!records.get(i).getAsJsonObject().get("notRender").getAsBoolean()){
                        SumActualUslug++;
                    }
                    dataUslug_models_arr.add(new DataUslug_Model(records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("eid").getAsString(),
                            records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("lid").getAsString(),
                            records.get(i).getAsJsonObject().get("title").getAsString()));

                }
            }

        } else { // Если количество услуг меньше, чем на сервере
            if (isCheckBox_check){
                // Если выбран чекбокс только актуальные услуги
                System.out.println("CheckBox is selected! <");
                for (int i=0; i<limit_usl; i++){
                    // Идем по циклу и добавляем в список услуг только актуальные услуги
                    if(!records.get(i).getAsJsonObject().get("notRender").getAsBoolean()){
                        SumActualUslug++;
                        dataUslug_models_arr.add(new DataUslug_Model(records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("eid").getAsString(),
                                records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("lid").getAsString(),
                                records.get(i).getAsJsonObject().get("title").getAsString()));
                    }

                }

            } else { // Если чекбокс не выбран то добавляем все услуги
                System.out.println("CheckBox is NOT selected! <");
                for (int i=0; i<limit_usl; i++){
                    if(!records.get(i).getAsJsonObject().get("notRender").getAsBoolean()){
                        SumActualUslug++;
                    }
                    dataUslug_models_arr.add(new DataUslug_Model(records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("eid").getAsString(),
                            records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("lid").getAsString(),
                            records.get(i).getAsJsonObject().get("title").getAsString()));
                }
            }
        }
        // Возвращаем список всех услуг, кол-во всех услуг, кол-во актуальных услуг
        return new result_service(dataUslug_models_arr, SumAllUslug, SumActualUslug);
    }
    // Функция для загрузки отчета по услугам
    public void Download_uslugs(ArrayList<DataUslug_Model> dataUslug_models_arr) {
            String absolutePathToFile="";
            String text_test=""; // Переменная в которой будет храниться текст отчета
            // Идем по циклу и добавляем в переменную данные об услугах
            for (int i=0; i<dataUslug_models_arr.size(); i++){
                text_test+=dataUslug_models_arr.get(i).getEidUslug() +"\t"+ dataUslug_models_arr.get(i).getNameUslug()+"\n";
            }
            //System.out.println(text_test);
            // Создание экземпляр класса FileChooser
            FileChooser fileChooser = new FileChooser();

            SaveLastPathController saveLastPathController=new SaveLastPathController();
            String lastPathDirectory= saveLastPathController.getLastDirectory();
            if (!lastPathDirectory.equals("")){
                fileChooser.setInitialDirectory(new File(lastPathDirectory));
            }

            // Устанавливаем список расширений для файла
            fileChooser.setInitialFileName("service_report"); // Устанавливаем название для файла
            // Список расширений для Excel
            FileChooser.ExtensionFilter extFilterExcel = new FileChooser.ExtensionFilter("Excel file (*.xlsx)", "*.xlsx");
            // Список расширений для Excel (старый формат)
            FileChooser.ExtensionFilter extFilterExcelOld = new FileChooser.ExtensionFilter("Excel file (old format) (*.xls)", "*.xls");
            // Список расширений для txt
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT file (*.txt)", "*.txt");
            // Добавляем список расширений
            fileChooser.getExtensionFilters().add(extFilterExcel);
            fileChooser.getExtensionFilters().add(extFilterExcelOld);
            fileChooser.getExtensionFilters().add(extFilter);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ais_plus/view/app.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            appController AppController = loader.getController();
            Stage stage = new Stage();
            // Показываем диалоговое окно для сохранения файла
            File file = fileChooser.showSaveDialog(stage);
            // Если не нажать кнопка "Отмена"
            if (fileChooser.getSelectedExtensionFilter()!=null){
                // Если выбрано расширение для Excel
                if (fileChooser.getSelectedExtensionFilter().getExtensions().toString().equals("[*.xlsx]")){
                    System.out.println("SELECTED XLSX");
                    // Если файл не пустой
                    if(file != null){
                        try {
                            // Сохраняем файл
                            absolutePathToFile=SaveFileExcel(dataUslug_models_arr, file);
                            try {
                                Desktop.getDesktop().open(new File(absolutePathToFile));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } // Иначе если выбрано расширение для старого формата Excel
                else if (fileChooser.getSelectedExtensionFilter().getExtensions().toString().equals("[*.xls]")){
                    System.out.println("SELECTED XLS");
                        // Если файл не пустой
                        if(file != null){
                            try {
                                // Сохраняем файл
                                absolutePathToFile=SaveFileExcelOldFormat(dataUslug_models_arr, file);
                                try {
                                    Desktop.getDesktop().open(new File(absolutePathToFile));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                } // Иначе если выбрано расширение для txt
                else if (fileChooser.getSelectedExtensionFilter().getExtensions().toString().equals("[*.txt]")){
                    System.out.println("SELECTED TXT");
                    // Если файл не пустой
                    if(file != null){
                        // Сохраняем файл
                        SaveFileTxt(text_test, file);
                    }

                }
            }


    }
    // Функция сохранения файла в txt
    private void SaveFileTxt(String content, File file){
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Функция для установки стилей Excel старый формат
    private static HSSFCellStyle createStyleForTitleOld(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
    // Функция сохранения файла в Excel старый формат
    public String SaveFileExcelOldFormat (ArrayList<DataUslug_Model> dataUslug_model_arr, File file) throws IOException {
        // Создание книги Excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        // Создание листа
        HSSFSheet sheet = workbook.createSheet("Service sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        // Установка стилей для книги
        HSSFCellStyle style = createStyleForTitleOld(workbook);

        row = sheet.createRow(rownum);

        // Создание столбца EidUslug
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("EidUslug");
        cell.setCellStyle(style);
        // Создание столбца названия услуги
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameUslug");
        cell.setCellStyle(style);


        // Перебор по данным
        for (DataUslug_Model uslug_model : dataUslug_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // Запись Eid Услуги
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(uslug_model.getEidUslug());
            // Запись Названия Услуги
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(uslug_model.getNameUslug());

        }
        // Создания потока сохранения файла
        FileOutputStream outFile = new FileOutputStream(file);
        // Запись файла
        workbook.write(outFile);
        // Закрытие потока записи
        outFile.close();
        System.out.println("Created file: " + file.getAbsolutePath());
        SaveLastPathController saveLastPathController=new SaveLastPathController();
        saveLastPathController.SaveLastPathInfo(file.getParent());
        return file.getAbsolutePath();

    }
    // Функция для установки стилей Excel
    private static XSSFCellStyle createStyleForTitleNew(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    // Функция сохранения файла в Excel
    public String SaveFileExcel (ArrayList<DataUslug_Model> dataUslug_model_arr, File file) throws IOException {
        // Создание книги Excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Создание листа
        XSSFSheet sheet = workbook.createSheet("Service sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        // Установка стилей
        XSSFCellStyle style = createStyleForTitleNew(workbook);

        row = sheet.createRow(rownum);

        // Создание столбца EidUslug
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("EidUslug");
        cell.setCellStyle(style);
        // Создание столбца названия услуги
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameUslug");
        cell.setCellStyle(style);


        // Перебор по данным
        for (DataUslug_Model uslug_model : dataUslug_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // Запись Eid Услуги
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(uslug_model.getEidUslug());
            // Запись Названия Услуги
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(uslug_model.getNameUslug());

        }
        // Создания потока сохранения файла
        FileOutputStream outFile = new FileOutputStream(file);
        // Запись файла
        workbook.write(outFile);
        // Закрытие потока записи
        outFile.close();
        System.out.println("Created file: " + file.getAbsolutePath());
        SaveLastPathController saveLastPathController=new SaveLastPathController();
        saveLastPathController.SaveLastPathInfo(file.getParent());
        return file.getAbsolutePath();

    }
    // Класс для возвращения списка услуг, кол-во всех услуг, и кол-во актуальных услуг
    public static class result_service {

        private ArrayList<DataUslug_Model> data_uslugArr;
        private int SumAllUslug;
        private int ActualUslug;

        public result_service(ArrayList<DataUslug_Model> data_uslugArr, int SumAllUslug, int ActualUslug) {

            this.data_uslugArr = data_uslugArr;
            this.SumAllUslug = SumAllUslug;
            this.ActualUslug = ActualUslug;
        }

        // getters and setters
        public ArrayList<DataUslug_Model> getData_uslugArr() {
            return data_uslugArr;
        }
        public int getAllUslug () {
            return SumAllUslug;
        }
        public int getActualUslug() {
            return ActualUslug;
        }


    }
    // Класс для payload услуг (Чтобы отправить json в post запросе)
    static class  Payload_uslug
    {
        public String action;
        public String method;
        public ArrayList<Data_uslug> data;
        public String type;
        public int tid;
    }
    // Класс для данных услуг
    static class Data_uslug
    {
        public ArrayList<String> serviceType;
        public ArrayList<String> requesterType;
        public ArrayList<String> departmentFilter;
        public String searchString;
        public int page;
        public int start;
        public int limit;
        public ArrayList<Sort_uslug> sort;
    }
    // Класс для сортировки услуг
    static class Sort_uslug
    {
        public String property;
        public String direction;
    }


}
