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

public class MfcController {

    public String Get_data_mfc(String cookie, String search, boolean isCheckBox_check) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore();
        //String search="";
        //search =search_mfc_t.getText();
        String onlyWork="";
        if (isCheckBox_check){
            onlyWork="false";
        } else {
            onlyWork="true";
        }
        String sort = "[{\"property\":\"code\",\"direction\":\"ASC\"}]";
        String getUrl = "http://192.168.99.91/cpgu/action/getMfcs?_dc=1617272572092&showClosed="+onlyWork+"&searchString="+
                URLEncoder.encode(search, String.valueOf(StandardCharsets.UTF_8))+"&page=1&start=0&limit=500&sort="
                + URLEncoder.encode(sort, String.valueOf(StandardCharsets.UTF_8));
        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpGet get = new HttpGet(getUrl);
        get.setHeader("Content-type", "application/json");
        get.addHeader("Cookie","JSESSIONID="+cookie);
        HttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
        //return gson.toJson(payload_departm);
    }

    public result_mfc Parsing_json_mfc(String json){

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray records= element.getAsJsonObject().get("records").getAsJsonArray();
        ArrayList<DataMfc_Model> dataMfc_modelArr = new ArrayList<DataMfc_Model>();
        int count_MfcAll = element.getAsJsonObject().get("total").getAsInt();
        //count_mfcAll_t.setText(element.getAsJsonObject().get("total").getAsString());

        for (int i=0; i<records.size(); i++){
            dataMfc_modelArr.add(new DataMfc_Model(records.get(i).getAsJsonObject().get("id").getAsString(),
                    records.get(i).getAsJsonObject().get("name").getAsString()));
        }
        //Download_mfc(dataMfc_modelArr);
        //return dataMfc_modelArr;
        return new result_mfc(dataMfc_modelArr,count_MfcAll);
    }

    public void Download_mfc(ArrayList<DataMfc_Model> dataMfc_model_arr) {


            String text_test="";

            for (int i=0; i<dataMfc_model_arr.size(); i++){
                text_test+=dataMfc_model_arr.get(i).getIdMfc() +"\t"+ dataMfc_model_arr.get(i).getNameMfc()+"\n";
            }
            //System.out.println(text_test);
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            fileChooser.setInitialFileName("mfc_report");
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
                        SaveFileExcel(dataMfc_model_arr, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else if (fileChooser.getSelectedExtensionFilter().getExtensions().toString().equals("[*.xls]")){
                System.out.println("SELECTED XLS");
                if(file != null){
                    try {
                        SaveFileExcelOldFormat(dataMfc_model_arr, file);
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

    public void SaveFileExcelOldFormat (ArrayList<DataMfc_Model> dataMfc_model_arr, File file) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Mfc sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        //
        HSSFCellStyle style = createStyleForTitleOld(workbook);

        row = sheet.createRow(rownum);

        // EmpNo
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("IdMfc");
        cell.setCellStyle(style);
        // EmpName
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameMfc");
        cell.setCellStyle(style);


        // Data
        for (DataMfc_Model mfc_model : dataMfc_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // IdMfc (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(mfc_model.getIdMfc());
            // NameMFc (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(mfc_model.getNameMfc());

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

    public void SaveFileExcel (ArrayList<DataMfc_Model> dataMfc_model_arr, File file) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Mfc sheet");

        //List<Employee> list = EmployeeDAO.listEmployees();

        int rownum = 0;
        Cell cell;
        Row row;
        //
        XSSFCellStyle style = createStyleForTitleNew(workbook);

        row = sheet.createRow(rownum);

        // EmpNo
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("IdMfc");
        cell.setCellStyle(style);
        // EmpName
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("NameMfc");
        cell.setCellStyle(style);


        // Data
        for (DataMfc_Model mfc_model : dataMfc_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // IdMfc (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(mfc_model.getIdMfc());
            // NameMFc (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(mfc_model.getNameMfc());

        }

        FileOutputStream outFile = new FileOutputStream(file);

        workbook.write(outFile);
        outFile.close();
        System.out.println("Created file: " + file.getAbsolutePath());

    }

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


