package ais_plus.controller;

import ais_plus.appController;
import ais_plus.model.DataMfc_Model;
import ais_plus.model.DataUslug_Model;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DepartmController {

    public String Get_data_departm(String cookie) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore();
        Gson gson = new Gson();

        Payload_departm payload_departm =new Payload_departm();
        Data_departm data_departm = new Data_departm();
        Sort_departm sort_departm = new Sort_departm();

        ArrayList<String> null_numb = new ArrayList<String>();
        ArrayList<Data_departm> data_departm_arr = new ArrayList<Data_departm>();
        ArrayList<Sort_departm> sort_departm_arr = new ArrayList<Sort_departm>();

        // Add value for sort_depart
        sort_departm.property="leaf";
        sort_departm.direction="ASC";
        sort_departm_arr.add(sort_departm);
        Sort_departm sort_departm2 = new Sort_departm();
        sort_departm2.property="title";
        sort_departm2.direction="ASC";
        sort_departm_arr.add(sort_departm2);


        // Add value for data_depart
        data_departm.node = "root";
        data_departm.sort = sort_departm_arr;
        data_departm.id = "root";
        data_departm_arr.add(data_departm);

        // Add value for payload_depart
        payload_departm.action ="departmentService";
        payload_departm.method ="getDepartments";
        payload_departm.data = data_departm_arr;
        payload_departm.type ="rpc";
        payload_departm.tid =10;

        String postUrl       = "http://192.168.99.91/cpgu/action/router";// put in your url

        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(payload_departm));//gson.tojson() converts your payload to json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        post.addHeader("Cookie","JSESSIONID="+cookie);
        HttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
        //return gson.toJson(payload_departm);
    }

    public result_departm Parsing_json_depart(String json, String search_text){

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray jsonArray =element.getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        JsonArray result= jsonObject.get("result").getAsJsonArray();
        ArrayList<DataDepartm_Model> dataDepartm_modelArr = new ArrayList<DataDepartm_Model>();
        String departm_info="";
        //count_DepartmAll_t.setText(String.valueOf(result.size()));
        int count_DepartmAll=result.size();
        for (int i=0; i<result.size(); i++){
            dataDepartm_modelArr.add(new DataDepartm_Model(result.get(i).getAsJsonObject().get("departmentId").getAsString(),
                    result.get(i).getAsJsonObject().get("title").getAsString()));
        }

        //ArrayList<DataDepartm_Model> dataDepartm_modelArr_find= Search_departm(dataDepartm_modelArr, search_text);
        //ArrayList<DataDepartm_Model> dataDepartm_modelArr_find= null;
        //return dataDepartm_modelArr;
        return new result_departm(dataDepartm_modelArr,count_DepartmAll);

    }

    public ArrayList<DataDepartm_Model> Search_departm(ArrayList<DataDepartm_Model> dataDepartm_modelArr, String search_text){


            Pattern pattern = Pattern.compile(".*" + search_text.toLowerCase() + ".*");

            ArrayList<DataDepartm_Model> dataDepartmFind_modelArr = new ArrayList<DataDepartm_Model>();
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
            return dataDepartmFind_modelArr;
    }

    public void Download_departm(ArrayList<DataDepartm_Model> dataDepartm_models_arr) {

            String text_test="";

            for (int i=0; i<dataDepartm_models_arr.size(); i++){
                text_test+=dataDepartm_models_arr.get(i).getIdDepartm() +"\t"+ dataDepartm_models_arr.get(i).getNameDepartm()+"\n";
            }
            //System.out.println(text_test);
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            fileChooser.setInitialFileName("departm_report");
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
                        SaveFileExcel(dataDepartm_models_arr, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else if (fileChooser.getSelectedExtensionFilter().getExtensions().toString().equals("[*.xls]")){
                System.out.println("SELECTED XLS");
                if(file != null){
                    try {
                        SaveFileExcelOldFormat(dataDepartm_models_arr, file);
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

    public void SaveFileExcelOldFormat (ArrayList<DataDepartm_Model> dataDepartm_model_arr, File file) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Departm sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitleOld(workbook);

        row = sheet.createRow(rownum);

        // EmpNo
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("IdDepartm");
        cell.setCellStyle(style);
        // EmpName
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameDepartm");
        cell.setCellStyle(style);


        // Data
        for (DataDepartm_Model departm_model : dataDepartm_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // IdMfc (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(departm_model.getIdDepartm());
            // NameMFc (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(departm_model.getNameDepartm());

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

    public void SaveFileExcel (ArrayList<DataDepartm_Model> dataDepartm_model_arr, File file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Departm sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        //
        XSSFCellStyle style = createStyleForTitleNew(workbook);

        row = sheet.createRow(rownum);

        // EmpNo
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("IdDepartm");
        cell.setCellStyle(style);
        // EmpName
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameDepartm");
        cell.setCellStyle(style);


        // Data
        for (DataDepartm_Model departm_model : dataDepartm_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // IdMfc (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(departm_model.getIdDepartm());
            // NameMFc (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(departm_model.getNameDepartm());

        }

        FileOutputStream outFile = new FileOutputStream(file);

        workbook.write(outFile);
        outFile.close();
        System.out.println("Created file: " + file.getAbsolutePath());

    }




    public class result_departm {

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

    class  Payload_departm
    {
        public String action;
        public String method;
        public ArrayList<Data_departm> data;
        public String type;
        public int tid;
    }
    class Data_departm
    {
        public String node;
        public ArrayList<Sort_departm> sort;
        public String id;
    }
    class Sort_departm
    {
        public String property;
        public String direction;
    }
}
