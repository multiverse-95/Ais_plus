package ais_plus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import ais_plus.controller.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ais_plus.model.DataDepartm_Model;
import ais_plus.model.DataMfc_Model;
import ais_plus.model.DataUslug_Model;

public class appController {
    String cookie_out;
    //ArrayList<DataUslug_Model> dataUslug_models_arr =new ArrayList<DataUslug_Model>();
    //ArrayList<DataDepartm_Model> dataDepartm_models_arr =new ArrayList<DataDepartm_Model>();
    //ArrayList<DataMfc_Model> dataMfc_models_arr = new ArrayList<DataMfc_Model>();


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<DataUslug_Model> data_table;
    @FXML
    private TableColumn<DataUslug_Model, String> id_usl;

    @FXML
    private TableColumn<DataUslug_Model, String> name_usl;

    @FXML
    private Button show_usl_b;

    @FXML
    private Button add_accrual_b;

    @FXML
    private TextField count_usl_t;

    @FXML
    private TextField search_t;

    @FXML
    private Button download_usl_b;

    @FXML
    public CheckBox onlyActual_ch;

    @FXML
    public TextField count_uslAll_t;

    @FXML
    public TextField count_uslAct_t;

    @FXML
    private Tab departm_tab;

    @FXML
    private TableView<DataDepartm_Model> data_table_departm;

    @FXML
    private TableColumn<DataDepartm_Model, String> id_departm;

    @FXML
    private TableColumn<DataDepartm_Model, String> name_departm;

    @FXML
    private Button show_departm;

    @FXML
    private TextField search_departm_t;

    @FXML
    private Button download_departm_b;

    @FXML
    private TextField count_DepartmAll_t;

    @FXML
    private Tab mfc_tab;

    @FXML
    private CheckBox onlyWorkMFC_ch;

    @FXML
    private TableView<DataMfc_Model> data_table_mfc;

    @FXML
    private TableColumn<DataMfc_Model, String> id_mfc;

    @FXML
    private TableColumn<DataMfc_Model, String> name_mfc;

    @FXML
    private Button show_mfc_b;

    @FXML
    private TextField search_mfc_t;

    @FXML
    private Button download_mfc_b;

    @FXML
    private TextField count_mfcAll_t;


    @FXML
    void initialize() throws IOException {

    }
    public void Show_accruals(String cookie_value){
        add_accrual_b.setOnAction(event -> {
            if (data_table.getSelectionModel().getSelectedItem() == null){
                Alert alert =new Alert(Alert.AlertType.WARNING , "Test");
                alert.setTitle("Услуга не выбрана");
                alert.setHeaderText("Необходимо выбрать услугу!");
                alert.setContentText("Выберите услугу из списка и попробуйте снова.");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK){

                    }

                });
                System.out.println("NOT_SELECTED USLUGAAAA");
            } else {
                DataUslug_Model dataUslug_model = data_table.getSelectionModel().getSelectedItem();
                String eid_usl=dataUslug_model.getEidUslug();
                String lid_usl=dataUslug_model.getLidUslug();
                System.out.println("GET EID: "+eid_usl+" GET LID: "+lid_usl);
                String name_usl=dataUslug_model.getNameUslug();
                //System.out.println(cookie_value);

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/ais_plus/view/accrual_list.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = loader.getRoot();

                AccrualController accrualController = loader.getController();
                accrualController.Show_data_Accrual(cookie_value, eid_usl, lid_usl, name_usl, loader);

                Stage stage = new Stage();
                stage.setTitle("Начисления");
                stage.setResizable(false);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            }


        });
    }

    public void Show_uslugs(String cookie_value) {
        show_usl_b.setOnAction(event -> {
            System.out.println(cookie_value);
            try {
                Show_data_uslug(cookie_value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        search_t.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    try {
                        Show_data_uslug(cookie_value);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void Show_mfc(String cookie_value) {
        show_mfc_b.setOnAction(event -> {
            data_table_mfc.setPlaceholder(new Label("Ничего не найдено!"));
            MfcController mfcController = new MfcController();
            String result_json= null;
            String search= search_mfc_t.getText();
            boolean isCheckbox;
            if (onlyWorkMFC_ch.isSelected()){
                isCheckbox=true;
            } else {
                isCheckbox = false;
            }
            try {
                result_json = mfcController.Get_data_mfc(cookie_value,search,isCheckbox );

            } catch (IOException e) {
                e.printStackTrace();
            }

            MfcController.result_mfc parsed_result = mfcController.Parsing_json_mfc(result_json);
            count_mfcAll_t.setText(String.valueOf(parsed_result.getAllMfc()));
            ArrayList<DataMfc_Model> parsed_result_arr= parsed_result.getData_mfcArr();
            ObservableList<DataMfc_Model> dataMfc_models = FXCollections.observableArrayList(parsed_result_arr);

            //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
            id_mfc.setCellValueFactory(new PropertyValueFactory<>("IdMfc"));
            id_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());
            name_mfc.setCellValueFactory(new PropertyValueFactory<>("NameMfc"));
            name_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());

            //add your data to the table here.
            data_table_mfc.setItems(dataMfc_models);
            Download_mfc(parsed_result_arr);
        });

        search_mfc_t.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    data_table_mfc.setPlaceholder(new Label("Ничего не найдено!"));
                    MfcController mfcController = new MfcController();
                    String result_json= null;
                    String search= search_mfc_t.getText();
                    boolean isCheckbox;
                    if (onlyWorkMFC_ch.isSelected()){
                        isCheckbox=true;
                    } else {
                        isCheckbox = false;
                    }
                    try {
                        result_json = mfcController.Get_data_mfc(cookie_value, search, isCheckbox);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    MfcController.result_mfc parsed_result = mfcController.Parsing_json_mfc(result_json);
                    count_mfcAll_t.setText(String.valueOf(parsed_result.getAllMfc()));
                    ArrayList<DataMfc_Model> parsed_result_arr= parsed_result.getData_mfcArr();
                    ObservableList<DataMfc_Model> dataMfc_models = FXCollections.observableArrayList(parsed_result_arr);

                    //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
                    id_mfc.setCellValueFactory(new PropertyValueFactory<>("IdMfc"));
                    id_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());
                    name_mfc.setCellValueFactory(new PropertyValueFactory<>("NameMfc"));
                    name_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());

                    //add your data to the table here.
                    data_table_mfc.setItems(dataMfc_models);
                    Download_mfc(parsed_result_arr);
                }
            }
        });
    }

    public void Download_uslugs(ArrayList<DataUslug_Model> dataUslug_models_arr) {

        download_usl_b.setOnAction(event -> {
            String text_test="";

            for (int i=0; i<dataUslug_models_arr.size(); i++){
                text_test+=dataUslug_models_arr.get(i).getEidUslug() +"\t"+ dataUslug_models_arr.get(i).getNameUslug()+"\n";
            }
            //System.out.println(text_test);
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            fileChooser.setInitialFileName("service_report");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
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


            if(file != null){
                SaveFile(text_test, file);
            }

        });
    }

    public void Download_departm(ArrayList<DataDepartm_Model> dataDepartm_models_arr) {

        download_departm_b.setOnAction(event -> {
            String text_test="";

            for (int i=0; i<dataDepartm_models_arr.size(); i++){
                text_test+=dataDepartm_models_arr.get(i).getIdDepartm() +"\t"+ dataDepartm_models_arr.get(i).getNameDepartm()+"\n";
            }
            //System.out.println(text_test);
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            fileChooser.setInitialFileName("departm_report");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
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


            if(file != null){
                SaveFile(text_test, file);
            }

        });
    }

    public void Download_mfc(ArrayList<DataMfc_Model> dataMfc_model_arr) {

        download_mfc_b.setOnAction(event -> {
            String text_test="";

            for (int i=0; i<dataMfc_model_arr.size(); i++){
                text_test+=dataMfc_model_arr.get(i).getIdMfc() +"\t"+ dataMfc_model_arr.get(i).getNameMfc()+"\n";
            }
            //System.out.println(text_test);
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            fileChooser.setInitialFileName("mfc_report");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
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


            if(file != null){
                SaveFile(text_test, file);
            }

        });
    }

    private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ArrayList<DataUslug_Model> Show_data_uslug(String cookie_value) throws IOException {
        boolean isCheckBox_check;
        if (onlyActual_ch.isSelected()){
            isCheckBox_check=true;
        } else {
            isCheckBox_check=false;
        }

        ServiceController serviceController = new ServiceController();
        data_table.setPlaceholder(new Label("Ничего не найдено!"));
        String search_text =search_t.getText();
        System.out.println(search_text);
        int limit_usl=0;
        limit_usl=  serviceController.Total_usl_from_serv(serviceController.Get_data_uslug(cookie_value, search_text, 25));
        System.out.println("LIMIT_uslug " +limit_usl);
        String result_json= serviceController.Get_data_uslug(cookie_value, search_text, limit_usl);

        //System.out.println(result_json);

        ServiceController.result_service parsed_result= serviceController.Parsing_json_uslug(result_json, limit_usl, isCheckBox_check);

        count_uslAll_t.setText(String.valueOf(parsed_result.getAllUslug()));
        count_uslAct_t.setText(String.valueOf(parsed_result.getActualUslug()));

        ArrayList<DataUslug_Model> parsed_result_arr= parsed_result.getData_uslugArr();
        ObservableList<DataUslug_Model> dataUslug_models = FXCollections.observableArrayList(parsed_result_arr);

        //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
        id_usl.setCellValueFactory(new PropertyValueFactory<>("EidUslug"));
        id_usl.setCellFactory(TextFieldTableCell.<DataUslug_Model>forTableColumn());
        name_usl.setCellValueFactory(new PropertyValueFactory<>("NameUslug"));
        name_usl.setCellFactory(TextFieldTableCell.<DataUslug_Model>forTableColumn());

        //add your data to the table here.
        data_table.setItems(dataUslug_models);
        Show_uslugs(cookie_value);
        Download_uslugs(parsed_result_arr);

        //return parsed_result;
        return null;
    }

    public void Show_data_Departm(String cookie_value) throws IOException {

            departm_tab.setOnSelectionChanged(event -> {
                if (departm_tab.isSelected()){
                    DepartmController departmController = new DepartmController();
                    System.out.println("Count_departm: "+count_DepartmAll_t.getText());
                    if (count_DepartmAll_t.getText().isEmpty()){
                        data_table_departm.setPlaceholder(new Label("Ничего не найдено!"));
                        String search_text =search_t.getText();
                        //System.out.println(search_text);
                        String count_uslugs= count_usl_t.getText();
                        int limit_usl=Integer.parseInt(count_uslugs);
                        String result_json= null;
                        try {
                            result_json =  departmController.Get_data_departm(cookie_value);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        DepartmController.result_departm parsed_result= departmController.Parsing_json_depart(result_json, search_departm_t.getText());
                        ArrayList<DataDepartm_Model> parsed_result_arr=  parsed_result.getData_departmArr();
                        //ArrayList<DataDepartm_Model> parsed_result_arr=  Parsing_json_depart(result_json);
                        //System.out.println("Parsed data:\n"+ parsed_result.get(0).getNameUslug());
                        ObservableList<DataDepartm_Model> dataDepartm_models = FXCollections.observableArrayList(parsed_result_arr);

                        count_DepartmAll_t.setText(String.valueOf(parsed_result.getAllDepartm()));
                        //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
                        id_departm.setCellValueFactory(new PropertyValueFactory<>("IdDepartm"));
                        id_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());
                        name_departm.setCellValueFactory(new PropertyValueFactory<>("NameDepartm"));
                        name_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());

                        //add your data to the table here.
                        data_table_departm.setItems(dataDepartm_models);
                        Download_departm(parsed_result_arr);

                        ///////////////////// SEARCH
                        show_departm.setOnAction(event2 -> {
                            ArrayList<DataDepartm_Model> parsed_result_arr_find= departmController.Search_departm(parsed_result_arr,search_departm_t.getText());
                            System.out.println("TEST SEARCH 111 "+ search_departm_t.getText());
                            Download_departm(parsed_result_arr_find);
                            count_DepartmAll_t.setText(String.valueOf(parsed_result_arr_find.size()));

                            data_table_departm.setPlaceholder(new Label("Ничего не найдено!"));

                            ObservableList<DataDepartm_Model> dataDepartm_models_find = FXCollections.observableArrayList(parsed_result_arr_find);

                            //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
                            id_departm.setCellValueFactory(new PropertyValueFactory<>("IdDepartm"));
                            id_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());
                            name_departm.setCellValueFactory(new PropertyValueFactory<>("NameDepartm"));
                            name_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());

                            //add your data to the table here.
                            data_table_departm.setItems(dataDepartm_models_find);


                        });

                        search_departm_t.setOnKeyPressed(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent keyEvent) {
                                if (keyEvent.getCode() == KeyCode.ENTER)  {
                                    ArrayList<DataDepartm_Model> parsed_result_arr_find= departmController.Search_departm(parsed_result_arr,search_departm_t.getText());
                                    System.out.println("TEST SEARCH 111 "+ search_departm_t.getText());
                                    Download_departm(parsed_result_arr_find);
                                    count_DepartmAll_t.setText(String.valueOf(parsed_result_arr_find.size()));

                                    data_table_departm.setPlaceholder(new Label("Ничего не найдено!"));

                                    ObservableList<DataDepartm_Model> dataDepartm_models_find = FXCollections.observableArrayList(parsed_result_arr_find);

                                    //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
                                    id_departm.setCellValueFactory(new PropertyValueFactory<>("IdDepartm"));
                                    id_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());
                                    name_departm.setCellValueFactory(new PropertyValueFactory<>("NameDepartm"));
                                    name_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());

                                    //add your data to the table here.
                                    data_table_departm.setItems(dataDepartm_models_find);
                                }
                            }
                        });
                    }
                }
            });
    }

    public void Show_data_Mfc(String cookie_value) throws IOException {

        mfc_tab.setOnSelectionChanged(event -> {
            if (mfc_tab.isSelected()){
                MfcController mfcController = new MfcController();
                System.out.println("Count_mfc_tab: "+count_mfcAll_t.getText());
                if (count_mfcAll_t.getText().isEmpty()){
                    data_table_mfc.setPlaceholder(new Label("Ничего не найдено!"));

                    String result_json= null;
                    String search= search_mfc_t.getText();
                    boolean isCheckbox;
                    if (onlyWorkMFC_ch.isSelected()){
                        isCheckbox=true;
                    } else {
                        isCheckbox = false;
                    }
                    try {
                        result_json = mfcController.Get_data_mfc(cookie_value, search, isCheckbox);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MfcController.result_mfc parsed_result = mfcController.Parsing_json_mfc(result_json);
                    count_mfcAll_t.setText(String.valueOf(parsed_result.getAllMfc()));
                    ArrayList<DataMfc_Model> parsed_result_arr = parsed_result.getData_mfcArr();
                    ObservableList<DataMfc_Model> dataMfc_models = FXCollections.observableArrayList(parsed_result_arr);

                    //make sure the property value factory should be exactly same as the e.g getStudentId from your model class
                    id_mfc.setCellValueFactory(new PropertyValueFactory<>("IdMfc"));
                    id_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());
                    name_mfc.setCellValueFactory(new PropertyValueFactory<>("NameMfc"));
                    name_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());

                    //add your data to the table here.
                    data_table_mfc.setItems(dataMfc_models);

                    //set button for search
                    Download_mfc(parsed_result_arr);
                    Show_mfc(cookie_value);

                }
            }
        });
    }

}