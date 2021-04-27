package ais_plus.controller;

import com.google.gson.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AccrualFormController {

    @FXML
    private Label accr_label;

    @FXML
    private TextField idPayment_t;

    @FXML
    private TextField displayName_t;

    @FXML
    private TextArea hint_t;

    @FXML
    private CheckBox phys_per_ch;

    @FXML
    private CheckBox indiv_enterp_ch;

    @FXML
    private CheckBox legal_pers_ch;

    @FXML
    private TextField name_t;

    @FXML
    private TextField bankName_t;

    @FXML
    private TextField personalAccount_t;

    @FXML
    private TextField correspAccount_t;

    @FXML
    private TextField bic_t;

    @FXML
    private TextField kpp_t;

    @FXML
    private CheckBox oktmo_eq_mfc_ch;

    @FXML
    private TextField oktmo_t;

    @FXML
    private TextField cbc_t;

    @FXML
    private TextField urn_t;

    @FXML
    private TextField cbcSection_t;

    @FXML
    private CheckBox preCheckRequired_ch;

    @FXML
    private Button add_accul_b;

    @FXML
    private Button cancel_accul_b;

    @FXML
    private TextArea purpose_t;

    @FXML
    private TextField inn_t;

    @FXML
    private RadioButton regType_BUD_rb;

    @FXML
    private RadioButton regType_NOTBUD_rb;

    @FXML
    void initialize() throws IOException {
        regType_BUD_rb.setOnAction(event -> {
            if (regType_BUD_rb.isSelected()) {
                regType_NOTBUD_rb.setSelected(false);
                oktmo_eq_mfc_ch.setDisable(false);
                oktmo_t.setDisable(false);
                cbc_t.setDisable(false);
                urn_t.setDisable(false);
                cbcSection_t.setDisable(false);
            }
        });
        regType_NOTBUD_rb.setOnAction(event -> {
            if (regType_NOTBUD_rb.isSelected()) {
                regType_BUD_rb.setSelected(false);
                oktmo_eq_mfc_ch.setSelected(false);
                oktmo_eq_mfc_ch.setDisable(true);
                oktmo_t.setText(null);
                oktmo_t.setDisable(true);
                cbc_t.setText(null);
                cbc_t.setDisable(true);
                urn_t.setText(null);
                urn_t.setDisable(true);
                cbcSection_t.setText(null);
                cbcSection_t.setDisable(true);
            }
        });

        oktmo_eq_mfc_ch.setOnAction(event -> {
            if (oktmo_eq_mfc_ch.isSelected()){
                oktmo_t.setDisable(true);
            } else {
                oktmo_t.setDisable(false);
            }
        });

    }

    public void Add_Accrual(String cookie, String id_accr, String eid, String lid, String name_usl, FXMLLoader loader, String use) throws IOException {
        add_accul_b.setOnAction(event -> {
            String idPayment = idPayment_t.getText();
            String displayName = displayName_t.getText();
            String person="false";
            if (phys_per_ch.isSelected()) person="true";
            String soloProprietor="false";
            if (indiv_enterp_ch.isSelected()) soloProprietor="true";
            String legal="false";
            if (legal_pers_ch.isSelected()) legal="true";
            String hint = hint_t.getText();
            String name = name_t.getText();
            String bankName = bankName_t.getText();
            String personalAccount = personalAccount_t.getText();
            String correspAccount = correspAccount_t.getText();
            String bic = bic_t.getText();
            String kpp = kpp_t.getText();
            String inn = inn_t.getText();
            String regType ="";
            if (regType_BUD_rb.isSelected()) {
                regType="BUD403";
            } else {
                regType="NBUD407";
            }
            String mfcsOKTMO="";
            if (oktmo_eq_mfc_ch.isSelected()){
                mfcsOKTMO="on";
            } else {
                mfcsOKTMO=null;
            }
            String oktmo = oktmo_t.getText();
            String cbc = cbc_t.getText();
            String urn = urn_t.getText();
            String cbcSection = cbcSection_t.getText();
            String preCheckRequired = "";
            if (preCheckRequired_ch.isSelected()){preCheckRequired = "true";} else {preCheckRequired = "false";}
            String purpose = purpose_t.getText();
            boolean isFieldCorrect= Check_fields(personalAccount,correspAccount,bic,kpp,inn,oktmo,cbc, urn,cbcSection);
            if(isFieldCorrect)
            {
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
                params.add(new BasicNameValuePair("use", use));
                params.add(new BasicNameValuePair("id", id_accr));
                params.add(new BasicNameValuePair("lid", lid));
                params.add(new BasicNameValuePair("eid", eid));
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
                post.addHeader("Cookie","JSESSIONID="+cookie);
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

                //AccrualController accrualController = new AccrualController();
                //accrualController.Show_data_Accrual(cookie, lid, name_usl);
                add_accul_b.getScene().getWindow().hide();
                AccrualController accrualController = loader.getController();
                accrualController.Update_data_Accrual(cookie, eid, lid, null);
            } else {
                WarningMessage();
            }


        });

        cancel_accul_b.setOnAction(event -> {
            cancel_accul_b.getScene().getWindow().hide();
        });

    }

    public String Edit_Accrual(String cookie, String id_accr, String eid, String lid, String name_usl, FXMLLoader loader) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore();
        Gson gson = new Gson();

        Payload_edit payload_edit =new Payload_edit();
        Data_edit data_edit = new Data_edit();

        ArrayList<Data_edit> data_editArr = new ArrayList<Data_edit>();

        // Add value for data_depart
        data_edit.paymentId = id_accr;
        data_editArr.add(data_edit);

        // Add value for payload_depart
        payload_edit.action ="paymentController";
        payload_edit.method ="getPayment";
        payload_edit.data = data_editArr;
        payload_edit.type ="rpc";
        payload_edit.tid =22;

        String postUrl       = "http://192.168.99.91/cpgu/action/router";// put in your url

        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(payload_edit));//gson.tojson() converts your payload to json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        post.addHeader("Cookie","JSESSIONID="+cookie);
        HttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        String json_result= EntityUtils.toString(entity);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json_result);
        JsonArray accrual_edit=element.getAsJsonArray();
        JsonObject result = accrual_edit.get(0).getAsJsonObject().get("result").getAsJsonObject();
        JsonObject data= result.getAsJsonObject().get("data").getAsJsonObject();
        String oktmo="";
        String cbc="";
        String urn="";
        String cbcSection="";

        String idPayment = data.get("idPayment").getAsString();
        String displayName = data.get("displayName").getAsString();
        String person = data.get("person").getAsString();
        String soloProprietor= data.get("soloProprietor").getAsString();
        String legal=data.get("legal").getAsString();
        String hint = data.get("hint").getAsString();
        String name = data.get("name").getAsString();
        String bankName = data.get("bankName").getAsString();
        String personalAccount = data.get("personalAccount").getAsString();
        String correspAccount = data.get("correspAccount").getAsString();
        String bic = data.get("bic").getAsString();
        String kpp = data.get("kpp").getAsString();
        String inn = data.get("inn").getAsString();
        String regType =data.get("regType").getAsString();
        String mfcsOKTMO=data.get("mfcsOKTMO").getAsString();
        if (regType.equals("NBUD407")){
            oktmo=null;
            cbc=null;
            urn=null;
            cbcSection=null;
        } else {
            oktmo=data.get("oktmo").getAsString();
            cbc=data.get("cbc").getAsString();
            urn=data.get("urn").getAsString();
            cbcSection=data.get("cbcSection").getAsString();
        }

        String preCheckRequired = data.get("preCheckRequired").getAsString();
        String purpose = data.get("purpose").getAsString();

        accr_label.setText("Редактирование начисления");
        add_accul_b.setText("Сохранить");


        idPayment_t.setText(idPayment);
        displayName_t.setText(displayName);
        if (person.equals("true")) {phys_per_ch.setSelected(true);} else {phys_per_ch.setSelected(false);}
        if (soloProprietor.equals("true")) {indiv_enterp_ch.setSelected(true);} else {indiv_enterp_ch.setSelected(false);}
        if (legal.equals("true")) {legal_pers_ch.setSelected(true);} else {legal_pers_ch.setSelected(false);}
        hint_t.setText(hint);
        name_t.setText(name);
        bankName_t.setText(bankName);
        personalAccount_t.setText(personalAccount);
        correspAccount_t.setText(correspAccount);
        bic_t.setText(bic);
        kpp_t.setText(kpp);
        inn_t.setText(inn);
        regType_BUD_rb.setSelected(false);
        regType_NOTBUD_rb.setSelected(false);
        oktmo_eq_mfc_ch.setSelected(false);
        if (regType.equals("BUD403")){
            regType_BUD_rb.setSelected(true);
            if (mfcsOKTMO.equals("true")){
                oktmo_eq_mfc_ch.setSelected(true);
                oktmo_t.setDisable(true);
            } else {
                oktmo_eq_mfc_ch.setSelected(false);
                oktmo_t.setDisable(false);
            }
        }
            else if(regType.equals("NBUD407")){
            regType_NOTBUD_rb.setSelected(true);
            regType_BUD_rb.setSelected(false);
            oktmo_eq_mfc_ch.setSelected(false);
            oktmo_eq_mfc_ch.setDisable(true);
            oktmo_t.setText(null);
            oktmo_t.setDisable(true);
            cbc_t.setText(null);
            cbc_t.setDisable(true);
            urn_t.setText(null);
            urn_t.setDisable(true);
            cbcSection_t.setText(null);
            cbcSection_t.setDisable(true);
        }
        oktmo_t.setText(oktmo);
        cbc_t.setText(cbc);
        urn_t.setText(urn);
        cbcSection_t.setText(cbcSection);
        if (preCheckRequired.equals("true")){ preCheckRequired_ch.setSelected(true);} else {preCheckRequired_ch.setSelected(false);}
        purpose_t.setText(purpose);
        Add_Accrual(cookie, id_accr, eid, lid, name_usl, loader, "on");
        //return idPayment;
        return json_result;
    }

    public String Delete_Accrual(String cookie, String id_accr, String eid, String lid, String name_usl, FXMLLoader loader) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore();
        Gson gson = new Gson();

        Payload_delete payload_delete =new Payload_delete();

        ArrayList<String> data_deleteArr = new ArrayList<String>();

        // Add value for data_depart
        data_deleteArr.add(id_accr);

        // Add value for payload_depart
        payload_delete.action ="paymentController";
        payload_delete.method ="delete";
        payload_delete.data = data_deleteArr;
        payload_delete.type ="rpc";
        payload_delete.tid =22;

        String postUrl       = "http://192.168.99.91/cpgu/action/router";// put in your url

        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(payload_delete));//gson.tojson() converts your payload to json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        post.addHeader("Cookie","JSESSIONID="+cookie);
        HttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        String json_result= EntityUtils.toString(entity);
        AccrualController accrualController = loader.getController();
        accrualController.Update_data_Accrual(cookie, eid, lid, null);
        return json_result;
    }

    public boolean Check_fields(String personalAccount, String correspAccount, String bic, String kpp,
                                String inn, String oktmo, String cbc, String urn, String cbcSection)
    {
        if (oktmo==null || cbc==null || urn==null || cbcSection==null ){
            if (personalAccount.length()!=20 || correspAccount.length()!=20 ||  bic.length()!=9 || kpp.length()!=9){
                return false;
            } else {
                return true;
            }
        } else {
            if (personalAccount.length()!=20 || correspAccount.length()!=20 ||  bic.length()!=9 || kpp.length()!=9|| cbc.length()>20){
                return false;
            }
            else if (urn.length()!=0 || cbcSection.length()!=0)
            {
                if( urn.length()>8 || cbcSection.length()>3) { return false; } else { return true; }
            } else { return true; }
        }

    }

    public void WarningMessage(){
        Alert alert =new Alert(Alert.AlertType.WARNING , "Test");
        alert.setTitle("Проверьте правильность данных");
        alert.setHeaderText("Введены неправильные данные!");
        alert.setContentText("Проверьте данные!");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK){

            }

        });
    }


    class  Payload_edit
    {
        public String action;
        public String method;
        public ArrayList<Data_edit> data;
        public String type;
        public int tid;
    }
    class Data_edit
    {
        public String paymentId;
    }

    class  Payload_delete
    {
        public String action;
        public String method;
        public ArrayList<String> data;
        public String type;
        public int tid;
    }


}
