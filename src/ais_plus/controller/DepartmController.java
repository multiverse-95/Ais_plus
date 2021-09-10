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
import ais_plus.model.DataDepartm_Model;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// Контроллер для ведомств
public class DepartmController {
    public static class DepartmTask extends Task<result_departm> {
        private final String cookies;
        private final String search_text;


        public DepartmTask(String cookies, String search_text) {
            this.cookies = cookies;
            this.search_text=search_text;

        }
        @Override
        protected result_departm call() throws Exception {
            String result_json= Get_data_departm(cookies);

            result_departm parsed_result= Parsing_json_depart(result_json, search_text);

            return parsed_result;
        }
    }

    // Функция получения данных по ведомствам
    public static String Get_data_departm(String cookie) throws IOException {
        // Хранилище куки
        CookieStore httpCookieStore = new BasicCookieStore();
        Gson gson = new Gson();
        // Создание экземпляра класса для payload ведомств
        Payload_departm payload_departm =new Payload_departm();
        // Создание экземпляра класса для data ведомств
        Data_departm data_departm = new Data_departm();
        // Создание экземпляра класс для сортировки ведомств
        Sort_departm sort_departm = new Sort_departm();

        ArrayList<String> null_numb = new ArrayList<String>();
        ArrayList<Data_departm> data_departm_arr = new ArrayList<Data_departm>(); // Список для данных ведомств
        ArrayList<Sort_departm> sort_departm_arr = new ArrayList<Sort_departm>(); // Список для сортировки ведомств

        // Добавление данных для сортировки ведомств
        sort_departm.property="leaf";
        sort_departm.direction="ASC";
        sort_departm_arr.add(sort_departm);
        Sort_departm sort_departm2 = new Sort_departm();
        sort_departm2.property="title";
        sort_departm2.direction="ASC";
        sort_departm_arr.add(sort_departm2);


        // Добавление данных для data ведомств
        data_departm.node = "root";
        data_departm.sort = sort_departm_arr;
        data_departm.id = "root";
        data_departm_arr.add(data_departm);

        // Добавление данных для payload ведомств
        payload_departm.action ="departmentService";
        payload_departm.method ="getDepartments";
        payload_departm.data = data_departm_arr;
        payload_departm.type ="rpc";
        payload_departm.tid =10;

        String postUrl       = "http://192.168.99.91/cpgu/action/router"; // Адрес запроса

        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(payload_departm)); // Конвертирование данных в json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        post.addHeader("Cookie","JSESSIONID="+cookie); // Добавление куки в Header
        HttpResponse response = httpClient.execute(post); // Выполнение post запроса
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
        //return gson.toJson(payload_departm);
    }
    // Функция для парсинга данных с ведомствами
    public static result_departm Parsing_json_depart(String json, String search_text){
        // Создания экземпляра парсинга
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json); // Получение главного элемента
        JsonArray jsonArray =element.getAsJsonArray(); // Получение массива элеменнтов
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject(); // Получение первого элемента из массива
        JsonArray result= jsonObject.get("result").getAsJsonArray(); // Получение элемента result
        ArrayList<DataDepartm_Model> dataDepartm_modelArr = new ArrayList<DataDepartm_Model>(); // Список в который будут записываться все ведомства
        String departm_info="";
        //count_DepartmAll_t.setText(String.valueOf(result.size()));
        int count_DepartmAll=result.size();
        // Идем по циклу, парсим данные и записываем данные по ведомствам в список
        for (int i=0; i<result.size(); i++){
            dataDepartm_modelArr.add(new DataDepartm_Model(result.get(i).getAsJsonObject().get("departmentId").getAsString(),
                    result.get(i).getAsJsonObject().get("title").getAsString()));
        }

        //ArrayList<DataDepartm_Model> dataDepartm_modelArr_find= Search_departm(dataDepartm_modelArr, search_text);
        //ArrayList<DataDepartm_Model> dataDepartm_modelArr_find= null;
        //return dataDepartm_modelArr;
        // Возвращаем список ведомств и количество ведомств
        return new result_departm(dataDepartm_modelArr,count_DepartmAll);

    }
    // Функция для поски по ведомствам
    public ArrayList<DataDepartm_Model> Search_departm(ArrayList<DataDepartm_Model> dataDepartm_modelArr, String search_text){

            // Регулярные выражения для поиска в строке
            Pattern pattern = Pattern.compile(".*" + search_text.toLowerCase() + ".*");
            // Список найденных ведомств
            ArrayList<DataDepartm_Model> dataDepartmFind_modelArr = new ArrayList<DataDepartm_Model>();
            // Идем по циклу ведомства и если есть совпадение с регулярными выражениями, то записываем в список найденных ведомств
            for (int i = 0; i < dataDepartm_modelArr.size(); i++) {
                Matcher matcher = pattern.matcher(dataDepartm_modelArr.get(i).getNameDepartm().toLowerCase());
                if (matcher.find()) {
                    //System.out.println("Search Success!");
                    dataDepartmFind_modelArr.add((new DataDepartm_Model(dataDepartm_modelArr.get(i).getIdDepartm(),
                            dataDepartm_modelArr.get(i).getNameDepartm())));
                } else {
                    //System.out.println("Search Failed!");
                }
            }
            // Возвращаем список найденных ведомств
            return dataDepartmFind_modelArr;
    }
    // Функция для загрузки отчета по ведомствам
    public void Download_departm(ArrayList<DataDepartm_Model> dataDepartm_models_arr) {
            String absolutePathToFile="";
            String text_test=""; // Переменная в которой будет храниться текст отчета
            // Идем по циклу и добавляем в переменную данные о ведомствах
            for (int i=0; i<dataDepartm_models_arr.size(); i++){
                text_test+=dataDepartm_models_arr.get(i).getIdDepartm() +"\t"+ dataDepartm_models_arr.get(i).getNameDepartm()+"\n";
            }
            //System.out.println(text_test);
            // Создаем экземпляр класса FileChooser
            FileChooser fileChooser = new FileChooser();
            SaveLastPathController saveLastPathController=new SaveLastPathController();
            String lastPathDirectory= saveLastPathController.getLastDirectory();
            if (!lastPathDirectory.equals("")){
                fileChooser.setInitialDirectory(new File(lastPathDirectory));
            }
            // Устанавливаем список расширений для файла
            fileChooser.setInitialFileName("departm_report");// Устанавливаем название для файла
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
                            absolutePathToFile=SaveFileExcel(dataDepartm_models_arr, file);
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
                            absolutePathToFile=SaveFileExcelOldFormat(dataDepartm_models_arr, file);
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
    public String SaveFileExcelOldFormat (ArrayList<DataDepartm_Model> dataDepartm_model_arr, File file) throws IOException {
        // Создание книги Excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        // Создание листа
        HSSFSheet sheet = workbook.createSheet("Departm sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        // Установка стилей для книги
        HSSFCellStyle style = createStyleForTitleOld(workbook);

        row = sheet.createRow(rownum);

        // Создание столбца IdDepartm
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("IdDepartm");
        cell.setCellStyle(style);
        // Создание столбца названия ведомства
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameDepartm");
        cell.setCellStyle(style);


        // Перебор по данным
        for (DataDepartm_Model departm_model : dataDepartm_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // Запись id ведомства
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(departm_model.getIdDepartm());
            // Запись Названия ведомства
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(departm_model.getNameDepartm());

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
    public String SaveFileExcel (ArrayList<DataDepartm_Model> dataDepartm_model_arr, File file) throws IOException {
        // Создание книги Excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Создание листа
        XSSFSheet sheet = workbook.createSheet("Departm sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        // Установка стилей
        XSSFCellStyle style = createStyleForTitleNew(workbook);

        row = sheet.createRow(rownum);

        // Создание столбца IdDepartm
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("IdDepartm");
        cell.setCellStyle(style);
        // Создание столбца названия ведомства
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameDepartm");
        cell.setCellStyle(style);


        // Перебор по данным
        for (DataDepartm_Model departm_model : dataDepartm_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // Запись id Ведомства
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(departm_model.getIdDepartm());
            // Запись Названия Ведомства
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(departm_model.getNameDepartm());

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



    // Класс для возвращения списка ведомств, кол-во всех ведомств
    public static class result_departm {

        private ArrayList<DataDepartm_Model> data_departmArr;
        private int count_DepartmAll;

        public result_departm(ArrayList<DataDepartm_Model> data_departmArr, int count_DepartmAll) {

            this.data_departmArr = data_departmArr;
            this.count_DepartmAll = count_DepartmAll;

        }

        // getters and setters
        public ArrayList<DataDepartm_Model> getData_departmArr() {
            return data_departmArr;
        }
        public int getAllDepartm () {
            return count_DepartmAll;
        }
    }
    // Класс для payload ведомств (Чтобы отправить json в post запросе)
    static class  Payload_departm
    {
        public String action;
        public String method;
        public ArrayList<Data_departm> data;
        public String type;
        public int tid;
    }
    // Класс для данных ведомств
    static class Data_departm
    {
        public String node;
        public ArrayList<Sort_departm> sort;
        public String id;
    }
    // Класс для сортировки ведомств
    static class Sort_departm
    {
        public String property;
        public String direction;
    }
}
