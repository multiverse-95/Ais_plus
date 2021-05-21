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
// Контроллер для формы начислений
public class AccrualFormController {
    // Графические элементы
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
    // Инициализация
    @FXML
    void initialize() throws IOException {
        // Если выбран бюджетный платеж
        if (regType_BUD_rb.isSelected()) {
            CheckFieldsOnChangeTextBUD(); // Проверять поля по бюджетному платежу
        }
        // Ивент на радиоб. бюджетный платеж
        regType_BUD_rb.setOnAction(event -> {
            if (regType_BUD_rb.isSelected()) {
                // Если выбран бюджетный платеж, то проверить поля, сбросить флажок на небюджетном, и сбросить поля
                CheckFieldsOnChangeTextBUD();
                regType_NOTBUD_rb.setSelected(false);
                oktmo_eq_mfc_ch.setDisable(false);
                oktmo_t.setDisable(false);
                oktmo_t.setText("");
                cbc_t.setDisable(false);
                cbc_t.setText("");
                urn_t.setDisable(false);
                urn_t.setText("");
                cbcSection_t.setDisable(false);
                cbcSection_t.setText("");
            }
        });
        // Если выбран небюджетный платеж, проверять поля по небюджетному платежу, сбросить поля
        regType_NOTBUD_rb.setOnAction(event -> {
            if (regType_NOTBUD_rb.isSelected()) {
                CheckFieldsOnChangeTextNOTBUD();
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
        // Событие на октмо соотв. мфц
        oktmo_eq_mfc_ch.setOnAction(event -> {
            // Если октмо соотв. мфц, поле октмо заблокировать, иначе разблокировать
            if (oktmo_eq_mfc_ch.isSelected()){
                oktmo_t.setDisable(true);
            } else {
                oktmo_t.setDisable(false);
            }
        });

    }
    // Функция добавления начислений
    public void Add_Accrual(String cookie, String id_accr, String eid, String lid, String name_usl, FXMLLoader loader, String use) throws IOException {
        // Событие на добавление начислений
        add_accul_b.setOnAction(event -> {
            String idPayment = idPayment_t.getText(); // Считать поле код платежа
            String displayName = displayName_t.getText(); // Считать поле имя начисления
            String person="false";
            // Считать флажок физ. лицо. Если активирован, то перем. person = true
            if (phys_per_ch.isSelected()) person="true";
            String soloProprietor="false";
            // Считать флажок индив. предприниматель. Если активирован, то перем. soloProprietor = true
            if (indiv_enterp_ch.isSelected()) soloProprietor="true";
            String legal="false";
            // Считать флажок юридическое лицо. Если активирован, то перем. legal = true
            if (legal_pers_ch.isSelected()) legal="true";
            String hint = hint_t.getText(); // Считать поле подсказка
            String name = name_t.getText(); // Считать поле имя получателя платежа
            String bankName = bankName_t.getText(); // Считать поле наименование банка получателя
            String personalAccount = personalAccount_t.getText(); // Считать поле счет получателя
            String correspAccount = correspAccount_t.getText(); // Считать поле корресп. счет получателя
            String bic = bic_t.getText(); // Считать поле бик
            String kpp = kpp_t.getText(); // Считать поле кпп
            String inn = inn_t.getText(); // Считать поле инн
            String regType ="";
            // Если флажок бюджетный платеж выбран то перем. regType=BUD403, иначе regType=NBUD407
            if (regType_BUD_rb.isSelected()) {
                regType="BUD403";
            } else {
                regType="NBUD407";
            }
            String mfcsOKTMO="";
            // Если выбран флажок мфц соотв. октмо, то mfcsOKTMO="on", иначе mfcsOKTMO=null
            if (oktmo_eq_mfc_ch.isSelected()){
                mfcsOKTMO="on";
            } else {
                mfcsOKTMO=null;
            }
            String oktmo = oktmo_t.getText(); // Считать поле октмо
            String cbc = cbc_t.getText(); // Считать поле кбк
            String urn = urn_t.getText(); // Считать поле урн
            String cbcSection = cbcSection_t.getText(); // Считать поле код кбк
            String preCheckRequired = "";
            // Считать флажок пречек обязателен. Если выбран тоpreCheckRequired = "true", иначе preCheckRequired = "false";
            if (preCheckRequired_ch.isSelected()){preCheckRequired = "true";} else {preCheckRequired = "false";}
            String purpose = purpose_t.getText(); // Считать поле назначение платежа
            // Проверка полей на корректность ввода
            boolean isFieldCorrect= Check_fields(idPayment,displayName, name, bankName,personalAccount,correspAccount,bic,kpp,inn,oktmo,cbc, urn,cbcSection, purpose);
            // Если поля введены правильно
            if(isFieldCorrect)
            {
                CookieStore httpCookieStore = new BasicCookieStore(); // Хранилище куки
                Gson gson = new Gson();
                HttpClient httpClient = null;
                HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
                httpClient = builder.build();
                String postUrl       = "http://192.168.99.91/cpgu/action/router";// Отправляем запрос на этот url
                HttpPost post = new HttpPost(postUrl);
                // Создаем параметры для запроса
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
                post.addHeader("Cookie","JSESSIONID="+cookie); // Добавление куки в header
                HttpResponse response = null;
                try {
                    response = httpClient.execute(post); // Выполнение post запроса
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
                add_accul_b.getScene().getWindow().hide(); // Скрыть окно "добавить начисление"
                AccrualController accrualController = loader.getController(); // Вызов контроллера начислений
                accrualController.Update_data_Accrual(cookie, eid, lid, null); // Обновить список начислений
            } else {
                // Если поля неправельно введены
                WarningMessage(); // Вывести предупреждение
            }


        });
        // Событие на кнопку "Отмена"
        cancel_accul_b.setOnAction(event -> {
            cancel_accul_b.getScene().getWindow().hide();
        });

    }
    // Функция для редактирования начислений
    public String Edit_Accrual(String cookie, String id_accr, String eid, String lid, String name_usl, FXMLLoader loader) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore(); // Хранилище куки
        Gson gson = new Gson();
        // Payload для редактирования
        Payload_edit payload_edit =new Payload_edit();
        // Данные для редактирования
        Data_edit data_edit = new Data_edit();
        // Список для данных редактирования
        ArrayList<Data_edit> data_editArr = new ArrayList<Data_edit>();

        // Добавление данных редактирования
        data_edit.paymentId = id_accr;
        data_editArr.add(data_edit);

        // Добавления значений для payload_depart
        payload_edit.action ="paymentController";
        payload_edit.method ="getPayment";
        payload_edit.data = data_editArr;
        payload_edit.type ="rpc";
        payload_edit.tid =22;

        String postUrl       = "http://192.168.99.91/cpgu/action/router";// url на который будет отправляться post запрос

        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(payload_edit)); //Конвертирует данные в gson
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        post.addHeader("Cookie","JSESSIONID="+cookie); // Добавление куки в header
        HttpResponse response = httpClient.execute(post); // Выполнение post запроса
        HttpEntity entity = response.getEntity();
        String json_result= EntityUtils.toString(entity); // Получение json с ответа на запрос
        // Создание экземпляра парсера
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json_result); // Получение главного элемента
        JsonArray accrual_edit=element.getAsJsonArray(); // Получение массива элементов
        JsonObject result = accrual_edit.get(0).getAsJsonObject().get("result").getAsJsonObject(); // Получение элемента result
        JsonObject data= result.getAsJsonObject().get("data").getAsJsonObject(); // Получение элемента data
        String oktmo=""; // октмо
        String cbc=""; // кбк
        String urn=""; // урн
        String cbcSection=""; // Код кбк

        String idPayment = data.get("idPayment").getAsString(); // Считать данные код начисления
        String displayName = data.get("displayName").getAsString(); // Считать данные имя начисления
        String person = data.get("person").getAsString(); // Считать данные физическое ли лицо
        String soloProprietor= data.get("soloProprietor").getAsString(); // Считать данные индив. предпр. ли
        String legal=data.get("legal").getAsString(); // Считать данные юридическ. лицо ли
        String hint = data.get("hint").getAsString(); // Считать данные подзказка
        String name = data.get("name").getAsString(); // Считать данные имя получателя платежа
        String bankName = data.get("bankName").getAsString(); // Считать данные наименов. банка получателя
        String personalAccount = data.get("personalAccount").getAsString(); // Считать номер счета получателя
        String correspAccount = data.get("correspAccount").getAsString(); // Считать данные корресп. счет получателя
        String bic = data.get("bic").getAsString(); // Считать данные бик
        String kpp = data.get("kpp").getAsString(); // Считать данные кпп
        String inn = data.get("inn").getAsString(); // Считать данные инн
        String regType =data.get("regType").getAsString(); // Считать данные тип платежа
        String mfcsOKTMO=data.get("mfcsOKTMO").getAsString(); // Считать данные мфц соотв. октмо
        // Если тип платежа небюджетный
        if (regType.equals("NBUD407")){
            oktmo=null; // октмо пустое
            cbc=null; // кбк пустое
            urn=null; // урн пустое
            cbcSection=null; // код кбк пустое
        } else {
            // Иначе получить данные
            oktmo=data.get("oktmo").getAsString(); // Считать данные октмо
            cbc=data.get("cbc").getAsString(); // Считать данные кбк
            urn=data.get("urn").getAsString(); // Считать данные урн
            cbcSection=data.get("cbcSection").getAsString(); // Считать данные код кбк
        }

        String preCheckRequired = data.get("preCheckRequired").getAsString(); // Считать данные пречек обязателен
        String purpose = data.get("purpose").getAsString(); // Считать данные назначение платежа

        accr_label.setText("Редактирование начисления"); // Название окошка
        add_accul_b.setText("Сохранить"); // Назание кнопки для сохранения

        // Устанавливаем в поля полученные данные
        idPayment_t.setText(idPayment); // Код платежа
        displayName_t.setText(displayName); // Имя платежа
        // Если выбран какой то тип заявителя, то поставить нужный флажок
        if (person.equals("true")) {phys_per_ch.setSelected(true);} else {phys_per_ch.setSelected(false);}
        if (soloProprietor.equals("true")) {indiv_enterp_ch.setSelected(true);} else {indiv_enterp_ch.setSelected(false);}
        if (legal.equals("true")) {legal_pers_ch.setSelected(true);} else {legal_pers_ch.setSelected(false);}
        hint_t.setText(hint); // Установить в поле значение подсказки
        name_t.setText(name); // Установить в поле значение имя получателя платежа
        bankName_t.setText(bankName); // Установить в поле наименование банка начисления
        personalAccount_t.setText(personalAccount); // Установить в поле счет начисления
        correspAccount_t.setText(correspAccount); // Установить в поле значение корр. счет
        bic_t.setText(bic); // Установить в поле значение бик
        kpp_t.setText(kpp); // Установить в поле значение кпп
        inn_t.setText(inn); // Установить в поле значение инн
        regType_BUD_rb.setSelected(false);
        regType_NOTBUD_rb.setSelected(false);
        oktmo_eq_mfc_ch.setSelected(false);
        // Если тип платежа бюджетный
        if (regType.equals("BUD403")){
            regType_BUD_rb.setSelected(true); // Установить флажок для бюджетного платежа
            // Если октмо соотв. мфц
            if (mfcsOKTMO.equals("true")){
                oktmo_eq_mfc_ch.setSelected(true); // Установить флажок октмо соотв. мфц
                oktmo_t.setDisable(true); // Заблокировать поле октмо
            } else {
                // Иначе
                oktmo_eq_mfc_ch.setSelected(false); // Не устанавливать флажок октмо соотв. мфц
                oktmo_t.setDisable(false); // Разблокировать поле октмо
            }
        } // Если небюджетный платеж
            else if(regType.equals("NBUD407")){
            regType_NOTBUD_rb.setSelected(true); // Установить флажок небюджетный платеж
            regType_BUD_rb.setSelected(false); // Не устанавливать флажок на бюджетный платеж
            oktmo_eq_mfc_ch.setSelected(false);
            oktmo_eq_mfc_ch.setDisable(true); // Заблокировать поле октмо соотв. мфц
            oktmo_t.setText(null); // Убрать текст с поля октмо
            oktmo_t.setDisable(true); // Заблокировать поле октмо
            cbc_t.setText(null); // Убрать текст с поля кбк
            cbc_t.setDisable(true); // Заблокировать поле кбк
            urn_t.setText(null); // Убрать текст с поля урн
            urn_t.setDisable(true); // Заблокировать поле урн
            cbcSection_t.setText(null); // Убрать текст с поля код кбк
            cbcSection_t.setDisable(true); // Заблокировать поле код кбк
        }
        oktmo_t.setText(oktmo); // Установить поле октмо
        cbc_t.setText(cbc); // Установить поле кбк
        urn_t.setText(urn); // Установить поле урн
        cbcSection_t.setText(cbcSection); // Установить поле код кбк
        // Если выбран чекбокс пречек обязателен
        if (preCheckRequired.equals("true")){ preCheckRequired_ch.setSelected(true);} else {preCheckRequired_ch.setSelected(false);}
        purpose_t.setText(purpose);
        // Вызвать функцию добавления начисления
        Add_Accrual(cookie, id_accr, eid, lid, name_usl, loader, "on");
        //return idPayment;
        return json_result; // Вернуть json результата
    }
    // Функция удаления начисления
    public String Delete_Accrual(String cookie, String id_accr, String eid, String lid, String name_usl, FXMLLoader loader) throws IOException {
        CookieStore httpCookieStore = new BasicCookieStore(); // Куки начисления
        Gson gson = new Gson();
        // Payload удаления
        Payload_delete payload_delete =new Payload_delete();
        // Список для данных удаления
        ArrayList<String> data_deleteArr = new ArrayList<String>();

        // Добавить в данные по удаление id начисления
        data_deleteArr.add(id_accr);

        // Добавить данные для payload удаления
        payload_delete.action ="paymentController";
        payload_delete.method ="delete";
        payload_delete.data = data_deleteArr;
        payload_delete.type ="rpc";
        payload_delete.tid =22;

        String postUrl       = "http://192.168.99.91/cpgu/action/router"; // url на которой будет отправлен запрос

        HttpClient httpClient = null;
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();
        HttpPost post          = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(gson.toJson(payload_delete)); // Конвертирует payload в json
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        post.addHeader("Cookie","JSESSIONID="+cookie); // Добавление куки в header
        HttpResponse response = httpClient.execute(post); // Выполнение post запроса
        HttpEntity entity = response.getEntity();
        String json_result= EntityUtils.toString(entity);
        // Вызов контроллера начислений
        AccrualController accrualController = loader.getController();
        // Обновление списка начислений
        accrualController.Update_data_Accrual(cookie, eid, lid, null);
        return json_result;
    }
    // Функция проверки на корректность ввода полей
    public boolean Check_fields(String idPayment, String displayName, String name, String bankName, String personalAccount, String correspAccount, String bic, String kpp,
                                String inn, String oktmo, String cbc, String urn, String cbcSection, String purpose)
    {
        boolean isValid=false; // Булева переменная , которая укажет, правильно введены данные или нет
        ArrayList<Boolean> isValidArray=new ArrayList<Boolean>();
        // Если октмо пустой или кбк пустой или урн пустой или кбк код пустой
        if (oktmo==null || cbc==null || urn==null || cbcSection==null ){
            // Если инн равен 10 или 12, то инн введен правильно, иначе поле инн будет подсвечено красным
            switch (inn.length()) {
                case 10: inn_t.setStyle("");
                    break;
                case 12: inn_t.setStyle("");
                    break;
                default:
                    inn_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");
                    isValidArray.add(false);
                    break;
            }
            // Если код начисление или имя начисления, или имя получателя платежа, или имя банка платежа, или назначение платежа пустые, то проверить каждое поле
            // Если одно из полей пустое, подсветить красным
            if (idPayment.length()==0 || displayName.length()==0 || name.length()==0 || bankName.length()==0 || purpose.length()==0){
                if (idPayment.length()==0){ idPayment_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {idPayment_t.setStyle("");}
                if (displayName.length()==0){ displayName_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {displayName_t.setStyle("");}
                if (name.length()==0){ name_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {name_t.setStyle("");}
                if (bankName.length()==0){ bankName_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {bankName_t.setStyle("");}
                if (purpose.length()==0){ purpose_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {purpose_t.setStyle("");}
                isValidArray.add(false);
            }
            // Если счет платежа не равен 20, или корресп. счет не равен 20, или бик не равен 9, или кпп не равен 9
            if (personalAccount.length()!=20 || correspAccount.length()!=20 ||  bic.length()!=9 || kpp.length()!=9){
                // Проверить каждое поле и подсветить красным
                if (personalAccount.length()!=20){ personalAccount_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {personalAccount_t.setStyle("");}
                if (correspAccount.length()!=20){ correspAccount_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {correspAccount_t.setStyle("");}
                if (bic.length()!=9){ bic_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {bic_t.setStyle("");}
                if (kpp.length()!=9){ kpp_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {kpp_t.setStyle("");}
                isValidArray.add(false);
            } else {
                // В массив корректности добавить true
                isValidArray.add(true);
                //return true;
            }
        } else { // Если октмо не пустой или кбк не пустой, или урн не пустой, или кбк код  не пустой
            // если длина инн равно 10 или 12, то все норм, иначе не норм
            switch (inn.length()) {
                case 10: inn_t.setStyle("");
                    break;
                case 12: inn_t.setStyle("");
                    break;
                default:
                    inn_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");
                    isValidArray.add(false);
                    break;
            }
            // Если длина октмо равно 8 или 11, то все норм, иначе не норм
            switch (oktmo.length()) {
                case 8: oktmo_t.setStyle("");
                    break;
                case 11: oktmo_t.setStyle("");
                    break;
                default:
                    oktmo_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");
                    isValidArray.add(false);
                    break;
            }
            // Если код начисление или имя начисления, или имя получателя платежа, или имя банка платежа, или назначение платежа пустые, то проверить каждое поле
            // Если одно из полей пустое, подсветить красным
            if (idPayment.length()==0 || displayName.length()==0 || name.length()==0 || bankName.length()==0 || purpose.length()==0){
                if (idPayment.length()==0){ idPayment_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {idPayment_t.setStyle("");}
                if (displayName.length()==0){ displayName_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {displayName_t.setStyle("");}
                if (name.length()==0){ name_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {name_t.setStyle("");}
                if (bankName.length()==0){ bankName_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {bankName_t.setStyle("");}
                if (purpose.length()==0){ purpose_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {purpose_t.setStyle("");}
                isValidArray.add(false);
            }
            // Если счет платежа не равен 20, или корресп. счет не равен 20, или бик не равен 9, или кпп не равен 9, или кбк больше 20 или кбк пустой
            if (personalAccount.length()!=20 || correspAccount.length()!=20 ||  bic.length()!=9 || kpp.length()!=9 || cbc.length()>20 || cbc.length()==0){
                // Проверить каждое поле, и если есть ошибка подсветить красным
                if (personalAccount.length()!=20){ personalAccount_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {personalAccount_t.setStyle("");}
                if (correspAccount.length()!=20){ correspAccount_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {correspAccount_t.setStyle("");}
                if (bic.length()!=9){ bic_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {bic_t.setStyle("");}
                if (kpp.length()!=9){ kpp_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {kpp_t.setStyle("");}
                if (cbc.length()==0){ cbc_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");}
                else if (cbc.length()>20){ cbc_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {cbc_t.setStyle("");}
                isValidArray.add(false);
                //return false;
            }
            // Если урн не пустой или код кбк не пустой
            else if (urn.length()!=0 || cbcSection.length()!=0)
            {
                // Если длина урн больше 8 или код кбк больше 3
                if( urn.length()>8 || cbcSection.length()>3) {
                    // Если есть ошибка , то подсветить красным
                    if (urn.length()>8){ urn_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {urn_t.setStyle("");}
                    if (cbcSection.length()>3){ cbcSection_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {cbcSection_t.setStyle("");}
                    isValidArray.add(false);
                    //return false;
                } else {
                    isValidArray.add(true);
                    //return true;
                }
            } else {
                isValidArray.add(true);
                //return true;
            }
        }
        // Если есть хоть в одном поле ошибка, то перем. isValid=false, иначе isValid=true
        for (int i=0; i<isValidArray.size(); i++) {
            if (isValidArray.get(i)==false){
                isValid=false;
                break;
            } else {
                isValid=true;
            }

        }
        return  isValid; // Вернуть isValid
    }
    //  Функция проверки при вводе данных бюджетного платежа
    public void CheckFieldsOnChangeTextBUD(){
            // Слушатель на ввод данных в поле код начисления
            idPayment_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String idPayment=idPayment_t.getText(); // Считать код начиселения
                // Если код начисления пустой, то подсветить красным
                if (idPayment.length()==0){ idPayment_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {idPayment_t.setStyle("");}
            });
            // Слушатель на ввод данных в поле название начисления
            displayName_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String displayName=displayName_t.getText(); // Считать название платежа
                // Если название платежа пустое,  то подсветить красным
                if (displayName.length()==0){ displayName_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {displayName_t.setStyle("");}
            });
            // Слушатель на ввод данных в поле имени получателя платежа
            name_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String name=name_t.getText(); // Считать название платежа
                // Если имя платежа пустое, то подсветить красным
                if (name.length()==0){ name_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {name_t.setStyle("");}
            });
            // Слушатель на ввод данных в поле наименование банка получателя
            bankName_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String bankName=bankName_t.getText(); // Считать наименование банка получателя
                // Если наименование банка получателя пустое, то подсветить красным
                if (bankName.length()==0){ bankName_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {bankName_t.setStyle("");}
            });
            // Слушатель на ввод данных в поле назначение платежа
            purpose_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String purpose=purpose_t.getText(); // Считать назначение платежа
                // Если назначение платежа пустое, то подсветить красным
                if (purpose.length()==0){ purpose_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {purpose_t.setStyle("");}
            });
            // Слушатель на ввод данных в поле счет получателя
            personalAccount_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String personalAccount=personalAccount_t.getText(); // Считать счет получателя
                // Если счет получателя не равен 20, то подсветить красным
                if (personalAccount.length()!=20){ personalAccount_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {personalAccount_t.setStyle("");}
            });
            // Слушатель на ввод данных в поле корресп. счет
            correspAccount_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String correspAccount=correspAccount_t.getText(); // Считать корресп. счет
                // Если корресп. счет не равен 20, то подсветить красным
                if (correspAccount.length()!=20){ correspAccount_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {correspAccount_t.setStyle("");}
            });
            // Слушатель на ввод данных в поле бик
            bic_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String bic=bic_t.getText(); // Считать поле бик
                // Если поле бик не равно 9, то подсветить красным
                if (bic.length()!=9){ bic_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {bic_t.setStyle("");}
            });
            // Слушатель на ввод данных в кпп
            kpp_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String kpp=kpp_t.getText(); // Считать поле кпп
                // Если поле кпп не равно 9, то подсветить красным
                if (kpp.length()!=9){ kpp_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {kpp_t.setStyle("");}
            });
            // Слушатель на ввод данных в инн
            inn_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String inn=inn_t.getText(); // Считать поле инн
                // Если инн не равно 10 или 12, то подсветить красным
                switch (inn.length()) {
                    case 10: inn_t.setStyle("");
                        break;
                    case 12: inn_t.setStyle("");
                        break;
                    default:
                        inn_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");
                        break;
                }
            });
            // Слушатель на ввод данных в октмо
            oktmo_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String oktmo=oktmo_t.getText(); // Считать поле октмо
                // Если октмо не пустое
                if (oktmo!=null){
                    // Если октмо не равно 8 или не равно 11, то подсветить красным
                    switch (oktmo.length()) {
                        case 8: oktmo_t.setStyle("");
                            break;
                        case 11: oktmo_t.setStyle("");
                            break;
                        default:
                            oktmo_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");
                            break;
                    }
                }

            });
            // Слушатель на ввод данных в кбк
            cbc_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String cbc=cbc_t.getText(); // Считать данные в кбк
                // Если кбк не пустое, то подсветить красным. Если кбк больше 20 то подсветить красным
                if (cbc!=null){
                    if (cbc.length()==0){ cbc_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");}
                    else if (cbc.length()>20){ cbc_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {cbc_t.setStyle("");}
                }

            });
            // Слушатель на ввод данных в урн
            urn_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String urn=urn_t.getText(); // Считать поле урн
                // Если урн не пустое
                if(urn!=null){
                    if (urn.length()!=0){
                        // Если урн больше 8, то подсветить красным
                        if (urn.length()>8){ urn_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {urn_t.setStyle("");}
                    }
                }

            });
            // Слушатель на ввод данных в код кбк
            cbcSection_t.textProperty().addListener((observable, oldValue, newValue) -> {
                String cbcSection=cbcSection_t.getText(); // Считать код кбк
                // Если кбк не пустое
                if(cbcSection!=null){
                    if(cbcSection.length()!=0) {
                        // Если код кбк больше 3, то подсветить красным
                        if (cbcSection.length()>3){ cbcSection_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {cbcSection_t.setStyle("");}
                    }
                }

            });
    }
    //  Функция проверки при вводе данных небюджетного платежа
    public void CheckFieldsOnChangeTextNOTBUD(){
        // Слушатель на ввод данных в поле код начисления
        idPayment_t.textProperty().addListener((observable, oldValue, newValue) -> {
            String idPayment=idPayment_t.getText(); // Считать код начисления
            // Если код начисления пустой, то подсветить красным
            if (idPayment.length()==0){ idPayment_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {idPayment_t.setStyle("");}
        });
        // Слушатель на ввод данных в поле имя начисления
        displayName_t.textProperty().addListener((observable, oldValue, newValue) -> {
            String displayName=displayName_t.getText(); // Считать поле имя начисления
            // Если имя начисления пустое, то подсветить красным
            if (displayName.length()==0){ displayName_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {displayName_t.setStyle("");}
        });
        // Слушатель на ввод данных в поле имя получателя платежа
        name_t.textProperty().addListener((observable, oldValue, newValue) -> {
            String name=name_t.getText(); // Считать имя платежа
            // Если имя получателя платежа пустое, то подсветить красным
            if (name.length()==0){ name_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {name_t.setStyle("");}
        });
        // Слушатель на ввод данных в поле имя банка получателя
        bankName_t.textProperty().addListener((observable, oldValue, newValue) -> {
            String bankName=bankName_t.getText(); // Считать имя банка получателя
            // Если имя банка получателя пустое, то подсветить красным
            if (bankName.length()==0){ bankName_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {bankName_t.setStyle("");}
        });
        // Слушатель на ввод данных в поле назначение платежа
        purpose_t.textProperty().addListener((observable, oldValue, newValue) -> {
            String purpose=purpose_t.getText(); // Считать поле назначение платежа
            // Если назначение платежа пустое, то подсветить красным
            if (purpose.length()==0){ purpose_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {purpose_t.setStyle("");}
        });
        // Слушатель на ввод данных в поле счет получателя
        personalAccount_t.textProperty().addListener((observable, oldValue, newValue) -> {
            String personalAccount=personalAccount_t.getText(); // Считать счет получателя
            // Если счет получателя не равен 20, то подсветить красным
            if (personalAccount.length()!=20){ personalAccount_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {personalAccount_t.setStyle("");}
        });
        // Слушатель на ввод данных в поле корресп. счет
        correspAccount_t.textProperty().addListener((observable, oldValue, newValue) -> {
            String correspAccount=correspAccount_t.getText(); // Считать поле корресп. счет
            // Если корресп счет не равен 20, то подсветить красным
            if (correspAccount.length()!=20){ correspAccount_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {correspAccount_t.setStyle("");}
        });
        // Слушатель на ввод данных в поле бик
        bic_t.textProperty().addListener((observable, oldValue, newValue) -> {
            String bic=bic_t.getText(); // Считать поле бик
            // Если бик не равен 9, то подсветить красным
            if (bic.length()!=9){ bic_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {bic_t.setStyle("");}
        });
        // Слушатель на ввод данных в поле кпп
        kpp_t.textProperty().addListener((observable, oldValue, newValue) -> {
            String kpp=kpp_t.getText(); // Считать поле кпп
            // Если кпп не равен 9, то подсветить красным
            if (kpp.length()!=9){ kpp_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");} else {kpp_t.setStyle("");}
        });
        // Слушатель на ввод данных в поле инн
        inn_t.textProperty().addListener((observable, oldValue, newValue) -> {
            String inn=inn_t.getText(); // Считать поле инн
            // Если инн не равен 10 или 12, то подсветить красным
            switch (inn.length()) {
                case 10: inn_t.setStyle("");
                    break;
                case 12: inn_t.setStyle("");
                    break;
                default:
                    inn_t.setStyle("-fx-background-color: #FFF0F0;-fx-border-color: #DBB1B1;");
                    break;
            }
        });
        // На поля октмо, кбк, урн, код кбк не нужно действие при вводе, т.к.  они заблокированы
        oktmo_t.textProperty().addListener((observable, oldValue, newValue) -> {

        });
        cbc_t.textProperty().addListener((observable, oldValue, newValue) -> {

        });
        urn_t.textProperty().addListener((observable, oldValue, newValue) -> {

        });
        cbcSection_t.textProperty().addListener((observable, oldValue, newValue) -> {

        });
    }
    // Функция, которое выводит предупреждение о неправильном вводе данных
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

    // Payload для редактирования начислений
    class  Payload_edit
    {
        public String action;
        public String method;
        public ArrayList<Data_edit> data;
        public String type;
        public int tid;
    }
    // Данные для редактирования начислений
    class Data_edit
    {
        public String paymentId; // id начисления
    }
    // Payload для удаления начислений
    class  Payload_delete
    {
        public String action;
        public String method;
        public ArrayList<String> data;
        public String type;
        public int tid;
    }


}
