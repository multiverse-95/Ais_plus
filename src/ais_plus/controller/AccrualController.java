package ais_plus.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import ais_plus.model.Accrual_Model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AccrualController {

    @FXML
    private TableView<Accrual_Model> accrual_table;

    @FXML
    private TableColumn<Accrual_Model, String> id_accrual;

    @FXML
    private TableColumn<Accrual_Model, String> name_accrual;

    @FXML
    private Button add_accrual_b;

    @FXML
    private Button dupl_accrual_b;

    @FXML
    private Button edit_accrual_b;

    @FXML
    private Button del_accrual_b;

    @FXML
    private TextField name_of_serv_t;

    @FXML
    void initialize() throws IOException {

    }

    public void Show_data_Accrual(String cookie_value, String eid_usl, String lid_usl, String name_usl, FXMLLoader loader_list) {
        name_of_serv_t.setText(name_usl);
        accrual_table.setPlaceholder(new Label("Ничего не найдено!"));
        String result_json = null;
            try {
                result_json = Get_Accruals(cookie_value, eid_usl, lid_usl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("JSON Accrual: "+result_json);
            ArrayList<Accrual_Model> parsed_result_arr = Parsing_Accruals(result_json);
            ObservableList<Accrual_Model> dataMfc_models = FXCollections.observableArrayList(parsed_result_arr);

            //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
        id_accrual.setCellValueFactory(new PropertyValueFactory<>("IdAccr"));
        id_accrual.setCellFactory(TextFieldTableCell.<Accrual_Model>forTableColumn());
        name_accrual.setCellValueFactory(new PropertyValueFactory<>("DisplayName"));
        name_accrual.setCellFactory(TextFieldTableCell.<Accrual_Model>forTableColumn());

            //add your data to the table here.
        accrual_table.setItems(dataMfc_models);

        add_accrual_b.setOnAction(event -> {
            String id_accr="";
            System.out.println("ID ACCR: "+id_accr+"\t"+"cookie: "+cookie_value);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ais_plus/view/accrual_form.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();

            AccrualFormController accrualFormController = loader.getController();
            try {
                accrualFormController.Add_Accrual(cookie_value, id_accr, eid_usl, lid_usl, name_usl, loader_list, null);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage = new Stage();
            stage.setTitle("Добавить начисление");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        });

        edit_accrual_b.setOnAction(event -> {
            Accrual_Model accrual_model = accrual_table.getSelectionModel().getSelectedItem();
            if (accrual_model == null){
                Alert alert =new Alert(Alert.AlertType.WARNING , "Test");
                alert.setTitle("Начисление не выбрано");
                alert.setHeaderText("Необходимо выбрать начисление!");
                alert.setContentText("Выберите начисление из списка и попробуйте снова.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK){

                    }

                });
                System.out.println("NOT_SELECTED ACCRUAL");
            } else {
                String id_accr=accrual_model.getIdAccr();
                System.out.println("ID ACCR: "+id_accr+"\t"+"cookie: "+cookie_value);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/ais_plus/view/accrual_form.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = loader.getRoot();

                AccrualFormController accrualFormController = loader.getController();
                try {
                    System.out.println("EDIT json= "+accrualFormController.Edit_Accrual(cookie_value, id_accr, eid_usl, lid_usl, name_usl, loader_list));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = new Stage();
                stage.setTitle("Редактировать начисление");
                stage.setResizable(false);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            }


        });

        del_accrual_b.setOnAction(event -> {
            Accrual_Model accrual_model = accrual_table.getSelectionModel().getSelectedItem();
            if (accrual_model == null){
                Alert alert =new Alert(Alert.AlertType.WARNING , "Test");
                alert.setTitle("Начисление не выбрано");
                alert.setHeaderText("Необходимо выбрать начисление!");
                alert.setContentText("Выберите начисление из списка и попробуйте снова.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK){

                    }

                });
                System.out.println("NOT_SELECTED ACCRUAL");
            } else {
                String id_accr=accrual_model.getIdAccr();
                String name_accr=accrual_model.getDisplayName();
                ButtonType yes_del = new ButtonType("Да", ButtonBar.ButtonData.OK_DONE);
                ButtonType no_del = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert =new Alert(Alert.AlertType.CONFIRMATION , "Test", yes_del, no_del);
                alert.setTitle("Удаление начисления");
                alert.setHeaderText("Вы уверены, что хотите удалить начисление?");
                alert.setContentText("Удалить начисление: "+name_accr+"?");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == yes_del){
                        System.out.println("ID ACCR: "+id_accr+"\t"+"cookie: "+cookie_value);
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/ais_plus/view/accrual_form.fxml"));
                        try {
                            loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        AccrualFormController accrualFormController = loader.getController();
                        try {
                            System.out.println("DELETE json= "+accrualFormController.Delete_Accrual(cookie_value, id_accr, eid_usl, lid_usl, name_usl, loader_list));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        int id_dupl=199518;
        int count_dupl=0;
        Duplicate_Accrual(cookie_value, eid_usl, lid_usl, id_dupl, count_dupl);
    }


    public void Update_data_Accrual(String cookie_value, String eid_usl, String lid_usl, String id_accr) {
        //name_of_serv_t.setText(name_usl);
        accrual_table.setPlaceholder(new Label("Ничего не найдено!"));
        String result_json = null;
        try {
            result_json = Get_Accruals(cookie_value, eid_usl, lid_usl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Accrual_Model> parsed_result_arr = Parsing_Accruals(result_json);
        ObservableList<Accrual_Model> dataMfc_models = FXCollections.observableArrayList(parsed_result_arr);

        //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
        id_accrual.setCellValueFactory(new PropertyValueFactory<>("IdAccr"));
        id_accrual.setCellFactory(TextFieldTableCell.<Accrual_Model>forTableColumn());
        name_accrual.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        name_accrual.setCellFactory(TextFieldTableCell.<Accrual_Model>forTableColumn());

        //add your data to the table here.
        accrual_table.setItems(dataMfc_models);
        if (id_accr!=null){
            for (int i=0; i<parsed_result_arr.size(); i++){
                if(id_accr.equals(parsed_result_arr.get(i).getIdAccr())){
                    accrual_table.getSelectionModel().select(parsed_result_arr.get(i));
                }
            }
        }

    }

    public String Get_Accruals(String cookie,  String eid_usl, String lid_usl) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore();
        //String sort = "[{\"property\":\"code\",\"direction\":\"ASC\"}]";
        String getUrl = "http://192.168.99.91/cpgu/action/getPaymentsForService?_dc=1617950599424&eid="+eid_usl+"&lid="+lid_usl+"&page=1&start=0&limit=500";
        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpGet get = new HttpGet(getUrl);
        //get.setHeader("Content-type", "application/json");
        get.addHeader("Cookie","JSESSIONID="+cookie);
        HttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
    }

    public ArrayList<Accrual_Model> Parsing_Accruals(String json){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray accrual_list =element.getAsJsonArray();
        ArrayList<Accrual_Model> accrual_model_arr = new ArrayList<Accrual_Model>();
        String oktmo="";
        String cbc="";
        String urn="";
        String cbcSection="";
        for (int i=0; i<accrual_list.size(); i++){
            if (accrual_list.get(i).getAsJsonObject().get("regType").getAsString().equals("NBUD407")){
                oktmo=null;
                cbc=null;
                urn=null;
                cbcSection=null;
            } else {
                oktmo=accrual_list.get(i).getAsJsonObject().get("oktmo").getAsString();
                cbc=accrual_list.get(i).getAsJsonObject().get("cbc").getAsString();
                urn=accrual_list.get(i).getAsJsonObject().get("urn").getAsString();
                cbcSection=accrual_list.get(i).getAsJsonObject().get("cbcSection").getAsString();
            }

            accrual_model_arr.add(new Accrual_Model(
                    accrual_list.get(i).getAsJsonObject().get("id").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("idPayment").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("displayName").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("person").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("soloProprietor").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("legal").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("hint").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("name").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("bankName").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("personalAccount").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("correspAccount").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("bic").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("kpp").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("inn").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("regType").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("mfcsOKTMO").getAsString(),
                    oktmo,
                    cbc,
                    urn,
                    cbcSection,
                    accrual_list.get(i).getAsJsonObject().get("preCheckRequired").getAsString(),
                    accrual_list.get(i).getAsJsonObject().get("purpose").getAsString()
                    ));
        }
        return accrual_model_arr;

    }

    public void Duplicate_Accrual(String cookie_value, String eid_usl, String lid_usl, int id_dupl, int count_dupl){
        dupl_accrual_b.setOnAction(event -> {
            Accrual_Model accrual_model = accrual_table.getSelectionModel().getSelectedItem();
            if (accrual_model == null){
                Alert alert =new Alert(Alert.AlertType.WARNING , "Test");
                alert.setTitle("Начисление не выбрано");
                alert.setHeaderText("Необходимо выбрать начисление!");
                alert.setContentText("Выберите начисление из списка и попробуйте снова.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK){

                    }

                });
                System.out.println("NOT_SELECTED ACCRUAL");
            } else {
                int id_dupl2=id_dupl; id_dupl2++;
                int count_dupl2=count_dupl; count_dupl2++;
                String result_json = null;
                try {
                    result_json = Get_Accruals(cookie_value, eid_usl, lid_usl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String idPayment=""; String displayName=""; String person=""; String soloProprietor=""; String legal=""; String hint=""; String name=""; String bankName=""; String personalAccount="";
                String correspAccount=""; String bic=""; String kpp=""; String inn=""; String regType =""; String mfcsOKTMO=""; String oktmo="";
                String cbc=""; String urn=""; String cbcSection=""; String preCheckRequired = ""; String purpose="";


                String id_accr=accrual_model.getIdAccr();
                System.out.println("JSON Accrual: "+result_json);
                ArrayList<Accrual_Model> parsed_result_arr = Parsing_Accruals(result_json);
                //String str_reg = "Тест Дубликат";
                //str_reg=str_reg.toLowerCase();
                //System.out.println("FIND DUPL "+str_reg+" " +str_reg.matches("дубликат"));
                for (int i=0; i<parsed_result_arr.size(); i++){
                    if (id_accr.equals(parsed_result_arr.get(i).getIdAccr())){
                        idPayment= String.valueOf(id_dupl2);
                        displayName= parsed_result_arr.get(i).getDisplayName()+" Дубликат";
                        person = parsed_result_arr.get(i).getPerson();
                        soloProprietor = parsed_result_arr.get(i).getSoloProprietor();
                        legal = parsed_result_arr.get(i).getLegal();
                        hint= parsed_result_arr.get(i).getHint();
                        name= parsed_result_arr.get(i).getName();
                        bankName= parsed_result_arr.get(i).getBankName();
                        personalAccount= parsed_result_arr.get(i).getPersonalAccount();
                        correspAccount= parsed_result_arr.get(i).getCorrespAccount();
                        bic= parsed_result_arr.get(i).getBic();
                        kpp= parsed_result_arr.get(i).getKpp();
                        inn= parsed_result_arr.get(i).getInn();
                        regType= parsed_result_arr.get(i).getRegType();
                        mfcsOKTMO= parsed_result_arr.get(i).getMfcsOKTMO();
                        oktmo= parsed_result_arr.get(i).getOktmo();
                        cbc= parsed_result_arr.get(i).getCbc();
                        urn= parsed_result_arr.get(i).getUrn();
                        cbcSection= parsed_result_arr.get(i).getCbcSection();
                        preCheckRequired= parsed_result_arr.get(i).getPreCheckRequired();
                        purpose= parsed_result_arr.get(i).getPurpose();
                    }
                }
                System.out.println("paym: "+idPayment);
                System.out.println("displayName: "+displayName);
                if (mfcsOKTMO.equals("true")){mfcsOKTMO="on";} else {mfcsOKTMO=null;}
                CookieStore httpCookieStore = new BasicCookieStore();
                Gson gson = new Gson();
                HttpClient httpClient = null;
                HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
                httpClient = builder.build();
                String postUrl       = "http://192.168.99.91/cpgu/action/router";// put in your url
                HttpPost post = new HttpPost(postUrl);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("extTID", "14"));
                params.add(new BasicNameValuePair("extAction", "paymentController"));
                params.add(new BasicNameValuePair("extMethod", "save"));
                params.add(new BasicNameValuePair("extType", "rpc"));
                params.add(new BasicNameValuePair("extUpload", "false"));
                params.add(new BasicNameValuePair("id", ""));
                params.add(new BasicNameValuePair("lid", lid_usl));
                params.add(new BasicNameValuePair("eid", eid_usl));
                params.add(new BasicNameValuePair("idPayment", idPayment));
                params.add(new BasicNameValuePair("displayName", displayName));
                params.add(new BasicNameValuePair("person", person));
                params.add(new BasicNameValuePair("soloProprietor", soloProprietor));
                params.add(new BasicNameValuePair("legal", legal));
                params.add(new BasicNameValuePair("hint", hint));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("bankName", bankName));
                params.add(new BasicNameValuePair("personalAccount", personalAccount));
                params.add(new BasicNameValuePair("correspAccount", correspAccount));
                params.add(new BasicNameValuePair("bic", bic));
                params.add(new BasicNameValuePair("codeCBC", ""));
                params.add(new BasicNameValuePair("kpp", kpp));
                params.add(new BasicNameValuePair("inn", inn));
                params.add(new BasicNameValuePair("regType", regType));
                params.add(new BasicNameValuePair("mfcsOKTMO", mfcsOKTMO));
                params.add(new BasicNameValuePair("oktmo", oktmo));
                params.add(new BasicNameValuePair("cbc", cbc));
                params.add(new BasicNameValuePair("urn", urn));
                params.add(new BasicNameValuePair("cbcSection", cbcSection));
                params.add(new BasicNameValuePair("preCheckRequired", preCheckRequired));
                params.add(new BasicNameValuePair("purpose", purpose));
                post.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
                //post.setHeader("Content-type", "application/json");
                post.addHeader("Cookie","JSESSIONID="+cookie_value);
                HttpResponse response = null;
                try {
                    response = httpClient.execute(post);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HttpEntity entity = response.getEntity();
                try {
                    String result_add_paym=EntityUtils.toString(entity);
                    System.out.println("Add PAYMENT: "+result_add_paym);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Update_data_Accrual(cookie_value, eid_usl, lid_usl, id_accr);

                Duplicate_Accrual(cookie_value, eid_usl,lid_usl, id_dupl2, count_dupl2);
            }
        });
    }

}
