package ais_plus.controller;

import ais_plus.appController;
import ais_plus.model.DataMfc_Model;
import com.google.gson.*;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServiceController {

    public String Get_data_uslug(String cookie, String search_text, int limit_usl) throws IOException {


        CookieStore httpCookieStore = new BasicCookieStore();
        Gson gson = new Gson();

        Payload_uslug payload_uslug =new Payload_uslug();
        Data_uslug data_uslugs = new Data_uslug();
        Sort_uslug sort_uslug = new Sort_uslug();

        ArrayList<String> null_numb = new ArrayList<String>();
        ArrayList<Data_uslug> data_uslug = new ArrayList<Data_uslug>();
        ArrayList<Sort_uslug> sort_usl = new ArrayList<Sort_uslug>();

        // Add value for sort_uslug
        sort_uslug.property="title";
        sort_uslug.direction="ASC";
        sort_usl.add(sort_uslug);

        // Add value for data_uslug
        data_uslugs.serviceType = null;
        data_uslugs.requesterType = null;
        data_uslugs.departmentFilter = null;
        data_uslugs.searchString = search_text;
        data_uslugs.page = 1;
        data_uslugs.start = 0;
        data_uslugs.limit = limit_usl;
        data_uslugs.sort = sort_usl;
        data_uslug.add(data_uslugs);

        // Add value for payload_uslug
        payload_uslug.action ="customServiceInfoService";
        payload_uslug.method ="getCustomServices";
        payload_uslug.data =data_uslug;
        payload_uslug.type ="rpc";
        payload_uslug.tid =11;


        String postUrl       = "http://192.168.99.91/cpgu/action/router";// put in your url

        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(payload_uslug), "UTF-8");//gson.tojson() converts your payload to json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json;charset=UTF-8");
        post.addHeader("Cookie","JSESSIONID="+cookie);
        HttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        System.out.println(gson.toJson(payload_uslug));
        //System.out.println(EntityUtils.toString(entity));
        return EntityUtils.toString(entity);
        //return gson.toJson(payload_uslug);
    }

    public int Total_usl_from_serv(String json){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray jsonArray =element.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        JsonObject result = jsonObject.get("result").getAsJsonObject();
        JsonArray records= result.get("records").getAsJsonArray();
        String title_usl="";
        int limit_usl_fromServer=jsonObject.get("result").getAsJsonObject().get("total").getAsInt();
        if (limit_usl_fromServer==0) limit_usl_fromServer=25;
        return limit_usl_fromServer;
    }

    public result_service Parsing_json_uslug(String json, int limit_usl, boolean isCheckBox_check) throws IOException {


        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray jsonArray =element.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        JsonObject result = jsonObject.get("result").getAsJsonObject();
        JsonArray records= result.get("records").getAsJsonArray();
        String title_usl="";
        int limit_usl_fromServer=jsonObject.get("result").getAsJsonObject().get("total").getAsInt();
        System.out.println("Total "+limit_usl_fromServer);
        ArrayList<DataUslug_Model> dataUslug_models_arr = new ArrayList<DataUslug_Model>();
        int SumAllUslug =limit_usl_fromServer;
        int SumActualUslug =0;


        if (limit_usl>limit_usl_fromServer){
            limit_usl=limit_usl_fromServer;
            System.out.println("LIMIT_USLUG_OUT "+limit_usl);
            if (isCheckBox_check){
                System.out.println("CheckBox is selected! >");
                for (int i=0; i<limit_usl; i++){
                    if(!records.get(i).getAsJsonObject().get("notRender").getAsBoolean()){
                        SumActualUslug++;
                        dataUslug_models_arr.add(new DataUslug_Model(records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("eid").getAsString(),
                                records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("lid").getAsString(),
                                records.get(i).getAsJsonObject().get("title").getAsString()));
                    }
                }

            } else {
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

        } else {
            if (isCheckBox_check){
                System.out.println("CheckBox is selected! <");
                for (int i=0; i<limit_usl; i++){
                    if(!records.get(i).getAsJsonObject().get("notRender").getAsBoolean()){
                        SumActualUslug++;
                        dataUslug_models_arr.add(new DataUslug_Model(records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("eid").getAsString(),
                                records.get(i).getAsJsonObject().get("id").getAsJsonObject().get("lid").getAsString(),
                                records.get(i).getAsJsonObject().get("title").getAsString()));
                    }

                }

            } else {
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

        return new result_service(dataUslug_models_arr, SumAllUslug, SumActualUslug);
    }

    public void Download_uslugs(ArrayList<DataUslug_Model> dataUslug_models_arr) {

            String text_test="";

            for (int i=0; i<dataUslug_models_arr.size(); i++){
                text_test+=dataUslug_models_arr.get(i).getEidUslug() +"\t"+ dataUslug_models_arr.get(i).getNameUslug()+"\n";
            }
            //System.out.println(text_test);
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            fileChooser.setInitialFileName("service_report");
            FileChooser.ExtensionFilter extFilterExcel = new FileChooser.ExtensionFilter("Excel file (*.xlsx)", "*.xlsx");
            FileChooser.ExtensionFilter extFilterExcelOld = new FileChooser.ExtensionFilter("Excel file (old format) (*.xls)", "*.xls");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT file (*.txt)", "*.txt");
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
            //Show save file dialog
            File file = fileChooser.showSaveDialog(stage);
            if (fileChooser.getSelectedExtensionFilter()!=null){
                if (fileChooser.getSelectedExtensionFilter().getExtensions().toString().equals("[*.xlsx]")){
                    System.out.println("SELECTED XLSX");
                    if(file != null){
                        try {
                            SaveFileExcel(dataUslug_models_arr, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else if (fileChooser.getSelectedExtensionFilter().getExtensions().toString().equals("[*.xls]")){
                    System.out.println("SELECTED XLS");
                        if(file != null){
                            try {
                                SaveFileExcelOldFormat(dataUslug_models_arr, file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                } else if (fileChooser.getSelectedExtensionFilter().getExtensions().toString().equals("[*.txt]")){
                    System.out.println("SELECTED TXT");
                    if(file != null){
                        SaveFileTxt(text_test, file);
                    }

                }
            }


    }

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

    // Excel Save
    private static HSSFCellStyle createStyleForTitleOld(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    public void SaveFileExcelOldFormat (ArrayList<DataUslug_Model> dataUslug_model_arr, File file) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Service sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitleOld(workbook);

        row = sheet.createRow(rownum);

        // EmpNo
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("EidUslug");
        cell.setCellStyle(style);
        // EmpName
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameUslug");
        cell.setCellStyle(style);


        // Data
        for (DataUslug_Model uslug_model : dataUslug_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // IdMfc (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(uslug_model.getEidUslug());
            // NameMFc (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(uslug_model.getNameUslug());

        }

        FileOutputStream outFile = new FileOutputStream(file);

        workbook.write(outFile);
        outFile.close();
        System.out.println("Created file: " + file.getAbsolutePath());

    }

    private static XSSFCellStyle createStyleForTitleNew(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    public void SaveFileExcel (ArrayList<DataUslug_Model> dataUslug_model_arr, File file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Service sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        //
        XSSFCellStyle style = createStyleForTitleNew(workbook);

        row = sheet.createRow(rownum);

        // EmpNo
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("EidUslug");
        cell.setCellStyle(style);
        // EmpName
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameUslug");
        cell.setCellStyle(style);


        // Data
        for (DataUslug_Model uslug_model : dataUslug_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // IdMfc (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(uslug_model.getEidUslug());
            // NameMFc (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(uslug_model.getNameUslug());

        }

        FileOutputStream outFile = new FileOutputStream(file);

        workbook.write(outFile);
        outFile.close();
        System.out.println("Created file: " + file.getAbsolutePath());

    }

    public class result_service {

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

    class  Payload_uslug
    {
        public String action;
        public String method;
        public ArrayList<Data_uslug> data;
        public String type;
        public int tid;
    }
    class Data_uslug
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

    class Sort_uslug
    {
        public String property;
        public String direction;
    }


}
