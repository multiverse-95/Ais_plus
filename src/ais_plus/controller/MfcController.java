package ais_plus.controller;

import ais_plus.appController;
import ais_plus.model.DataUslug_Model;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import ais_plus.model.DataMfc_Model;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
// Контроллер для списка мфц
public class MfcController {
    // Функция получения данных по мфц
    public String Get_data_mfc(String cookie, String search, boolean isCheckBox_check) throws IOException {
        // Хранилище куки
        CookieStore httpCookieStore = new BasicCookieStore();
        //String search="";
        //search =search_mfc_t.getText();
        // Если чебкокс только рабочие мфц включен
        String onlyWork="";
        if (isCheckBox_check){
            onlyWork="false";
        } else {
            onlyWork="true";
        }
        // Сортировка мфц
        String sort = "[{\"property\":\"code\",\"direction\":\"ASC\"}]";
        // Строка с url, к которой будет выполняться get запрос
        String getUrl = "http://192.168.99.91/cpgu/action/getMfcs?_dc=1617272572092&showClosed="+onlyWork+"&searchString="+
                URLEncoder.encode(search, String.valueOf(StandardCharsets.UTF_8))+"&page=1&start=0&limit=500&sort="
                + URLEncoder.encode(sort, String.valueOf(StandardCharsets.UTF_8));
        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpGet get = new HttpGet(getUrl);
        get.setHeader("Content-type", "application/json");
        get.addHeader("Cookie","JSESSIONID="+cookie); // Добавление куки в Header
        HttpResponse response = httpClient.execute(get); // Выполнение get запроса
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
        //return gson.toJson(payload_departm);
    }
    // Функция для парсинга данных с мфц
    public result_mfc Parsing_json_mfc(String json){
        // Создания экземпляра парсинга
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json); // Получение главного элемента
        // Получение массива записей records
        JsonArray records= element.getAsJsonObject().get("records").getAsJsonArray();
        // Список в который будут записываться все мфц
        ArrayList<DataMfc_Model> dataMfc_modelArr = new ArrayList<DataMfc_Model>();
        // Получение количества мфц
        int count_MfcAll = element.getAsJsonObject().get("total").getAsInt();
        //count_mfcAll_t.setText(element.getAsJsonObject().get("total").getAsString());
        // Идем по циклу, парсим данные и записываем данные по мфц в список
        for (int i=0; i<records.size(); i++){
            dataMfc_modelArr.add(new DataMfc_Model(records.get(i).getAsJsonObject().get("id").getAsString(),
                    records.get(i).getAsJsonObject().get("name").getAsString()));
        }
        //Download_mfc(dataMfc_modelArr);
        //return dataMfc_modelArr;
        // Возвращаем список мфц и количество мфц
        return new result_mfc(dataMfc_modelArr,count_MfcAll);
    }
    // Функция для загрузки отчета по мфц
    public void Download_mfc(ArrayList<DataMfc_Model> dataMfc_model_arr) {


            String text_test=""; // Переменная в которой будет храниться текст отчета
            // Идем по циклу и добавляем в переменную данные о мфц
            for (int i=0; i<dataMfc_model_arr.size(); i++){
                text_test+=dataMfc_model_arr.get(i).getIdMfc() +"\t"+ dataMfc_model_arr.get(i).getNameMfc()+"\n";
            }
            // Создаем экземпляр класса FileChooser
            FileChooser fileChooser = new FileChooser();

            // Устанавливаем список расширений для файла
            fileChooser.setInitialFileName("mfc_report"); // Устанавливаем название для файла
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
                            SaveFileExcel(dataMfc_model_arr, file);
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
                            SaveFileExcelOldFormat(dataMfc_model_arr, file);
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
    public void SaveFileExcelOldFormat (ArrayList<DataMfc_Model> dataMfc_model_arr, File file) throws IOException {
        // Создание книги Excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        // Создание листа
        HSSFSheet sheet = workbook.createSheet("Mfc sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        // Установка стилей для книги
        HSSFCellStyle style = createStyleForTitleOld(workbook);

        row = sheet.createRow(rownum);

        // Создание столбца IdMfc
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("IdMfc");
        cell.setCellStyle(style);
        // Создание столбца названия мфц
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameMfc");
        cell.setCellStyle(style);


        // Перебор по данным
        for (DataMfc_Model mfc_model : dataMfc_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // Запись id мфц
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(mfc_model.getIdMfc());
            // Запись Названия мфц
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(mfc_model.getNameMfc());

        }
        // Создания потока сохранения файла
        FileOutputStream outFile = new FileOutputStream(file);
        // Запись файла
        workbook.write(outFile);
        // Закрытие потока записи
        outFile.close();
        System.out.println("Created file: " + file.getAbsolutePath());

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
    public void SaveFileExcel (ArrayList<DataMfc_Model> dataMfc_model_arr, File file) throws IOException {
        // Создание книги Excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Создание листа
        XSSFSheet sheet = workbook.createSheet("Mfc sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        // Установка стилей
        XSSFCellStyle style = createStyleForTitleNew(workbook);

        row = sheet.createRow(rownum);

        // Создание столбца IdMfc
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("IdMfc");
        cell.setCellStyle(style);
        // Создание столбца названия мфц
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameMfc");
        cell.setCellStyle(style);


        // Перебор по данным
        for (DataMfc_Model mfc_model : dataMfc_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // Запись id мфц
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(mfc_model.getIdMfc());
            // Запись Названия мфц
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(mfc_model.getNameMfc());

        }
        // Создания потока сохранения файла
        FileOutputStream outFile = new FileOutputStream(file);
        // Запись файла
        workbook.write(outFile);
        // Закрытие потока записи
        outFile.close();
        System.out.println("Created file: " + file.getAbsolutePath());

    }
    // Класс для возвращения списка мфц, кол-во всех мфц
    public class result_mfc {

        private ArrayList<DataMfc_Model> data_mfcArr;
        private int count_MfcAll;

        public result_mfc(ArrayList<DataMfc_Model> data_mfcArr, int count_MfcAll) {

            this.data_mfcArr = data_mfcArr;
            this.count_MfcAll = count_MfcAll;

        }

        // getters and setters
        public ArrayList<DataMfc_Model> getData_mfcArr() {
            return data_mfcArr;
        }
        public int getAllMfc () {
            return count_MfcAll;
        }
    }
}


