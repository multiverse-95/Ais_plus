package ais_plus;

import java.io.File;
import java.io.FileOutputStream;
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
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ais_plus.model.DataDepartm_Model;
import ais_plus.model.DataMfc_Model;
import ais_plus.model.DataUslug_Model;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

// Контроллер для основного окна приложения
public class appController {
    String cookie_out;
    //ArrayList<DataUslug_Model> dataUslug_models_arr =new ArrayList<DataUslug_Model>();
    //ArrayList<DataDepartm_Model> dataDepartm_models_arr =new ArrayList<DataDepartm_Model>();
    //ArrayList<DataMfc_Model> dataMfc_models_arr = new ArrayList<DataMfc_Model>();

// Графические элементы
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private StackPane root_usl;

    @FXML
    private VBox vbox_usl_main;

    @FXML
    private StackPane root_depart;

    @FXML
    private VBox vbox_departm_main;

    @FXML
    private StackPane root_mfc;

    @FXML
    private VBox vbox_mfc_main;

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


    // Функция для получения начислений по услуге
    public void Show_accruals(String cookie_value){
        // Устанавлием событие на кнопку "Начисления"
        add_accrual_b.setOnAction(event -> {
            // Если услуга не выбрана, показать предупреждение
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
                // Иначе Получить услугу, получить eid услуги
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
                // Вызов функции обработки начислений
                AccrualController accrualController = loader.getController();
                accrualController.Show_data_Accrual(cookie_value, eid_usl, lid_usl, name_usl, loader);
                // Открыть окно начислений
                Stage stage = new Stage();
                stage.setTitle("Начисления");
                stage.setResizable(false);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            }


        });
    }
    // Функция для отображения услуг
    public void Show_uslugs(String cookie_value) {
        // Событие на кнопку показать услуги
        show_usl_b.setOnAction(event -> {
            System.out.println(cookie_value);
            try {
                Show_data_uslug(cookie_value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Событие на строку поиска. Искать на нажатие кнопки Enter
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
    // Функция для отображение списка МФЦ
    public void Show_mfc(String cookie_value) {
        // Событие на кнопку показать мфц
        show_mfc_b.setOnAction(event -> {
            data_table_mfc.setPlaceholder(new Label("Ничего не найдено!")); // если нет результатов
            MfcController mfcController = new MfcController();
            String result_json= null;
            String search= search_mfc_t.getText(); // Считать с кнопки поиска
            boolean isCheckbox; // Если поставлен чекбокс с только работающим МФЦ
            if (onlyWorkMFC_ch.isSelected()){
                isCheckbox=true;
            } else {
                isCheckbox = false;
            }
            try {
                // Вызов функции из класса контроллера по получению списка МФЦ
                result_json = mfcController.Get_data_mfc(cookie_value,search,isCheckbox );

            } catch (IOException e) {
                e.printStackTrace();
            }
            // Получение распарсенных результатов с МФЦ
            MfcController.result_mfc parsed_result = mfcController.Parsing_json_mfc(result_json);
            count_mfcAll_t.setText(String.valueOf(parsed_result.getAllMfc())); // Установить количестов МФЦ в текстовом поле
            ArrayList<DataMfc_Model> parsed_result_arr= parsed_result.getData_mfcArr(); // Создание списка с распарсенным результатом
            ObservableList<DataMfc_Model> dataMfc_models = FXCollections.observableArrayList(parsed_result_arr);

            // Заполнение данными в таблицу МФЦ
            id_mfc.setCellValueFactory(new PropertyValueFactory<>("IdMfc"));
            id_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());
            name_mfc.setCellValueFactory(new PropertyValueFactory<>("NameMfc"));
            name_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());

            data_table_mfc.setItems(dataMfc_models);
            // Установка события на кнопку скачать отчет по мфц
            download_mfc_b.setOnAction(event2 -> {
                mfcController.Download_mfc(parsed_result_arr); // Вызов функции сохранения отчета по МФЦ
            });

        });
        // Установка события на кнопку enter для поиска по мфц
        search_mfc_t.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER)  {
                    data_table_mfc.setPlaceholder(new Label("Ничего не найдено!"));
                    MfcController mfcController = new MfcController();
                    String result_json= null;
                    String search= search_mfc_t.getText(); // Получение значения с текстового поля
                    boolean isCheckbox; // Проверка чекбокса на работающие МФЦ
                    if (onlyWorkMFC_ch.isSelected()){
                        isCheckbox=true;
                    } else {
                        isCheckbox = false;
                    }
                    try {
                        // Получить json с данными по мфц
                        result_json = mfcController.Get_data_mfc(cookie_value, search, isCheckbox);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Получение распарсенных результатов с МФЦ
                    MfcController.result_mfc parsed_result = mfcController.Parsing_json_mfc(result_json);
                    count_mfcAll_t.setText(String.valueOf(parsed_result.getAllMfc())); // Установить количестов МФЦ в текстовом поле
                    ArrayList<DataMfc_Model> parsed_result_arr= parsed_result.getData_mfcArr(); // Создание списка с распарсенным результатом
                    ObservableList<DataMfc_Model> dataMfc_models = FXCollections.observableArrayList(parsed_result_arr);

                    // Заполнение данными в таблицу МФЦ
                    id_mfc.setCellValueFactory(new PropertyValueFactory<>("IdMfc"));
                    id_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());
                    name_mfc.setCellValueFactory(new PropertyValueFactory<>("NameMfc"));
                    name_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());


                    data_table_mfc.setItems(dataMfc_models);
                    // Установка события на кнопку скачивания отчета по мфц
                    download_mfc_b.setOnAction(event3 -> {
                        mfcController.Download_mfc(parsed_result_arr);
                    });

                }
            }
        });
    }
    // Функция для отображения услуг
    public ArrayList<DataUslug_Model> Show_data_uslug(String cookie_value) throws IOException {
        boolean isCheckBox_check; // переменная для хранения значения, актуальные услуги или нет
        // Если чекбокс выбран, то установить значение переменной isCheckBox_check true, иначе false
        if (onlyActual_ch.isSelected()){
            isCheckBox_check=true;
        } else {
            isCheckBox_check=false;
        }
        // Вызов экземпляра класса контроллера услуг
        ServiceController serviceController = new ServiceController();
        // Если услуги не найдены
        data_table.setPlaceholder(new Label("Ничего не найдено!"));
        String search_text =search_t.getText(); // Получение значени с текстового поля поиска
        System.out.println(search_text);
        int limit_usl=0; // Лимит услуг
        // Получение количество всех услуг на сервере


        ProgressIndicator pi = new ProgressIndicator();
        VBox box = new VBox(pi);
        box.setAlignment(Pos.CENTER);
        // Grey Background
        vbox_usl_main.setDisable(true);
        root_usl.getChildren().add(box);

        Task ServiceTask = new ServiceController.ServiceTask(cookie_value,search_text , 25, isCheckBox_check);

/*
        limit_usl=  serviceController.Total_usl_from_serv(serviceController.Get_data_uslug(cookie_value, search_text, 25));
        System.out.println("LIMIT_uslug " +limit_usl);
        // Получение json услуг
        String result_json= serviceController.Get_data_uslug(cookie_value, search_text, limit_usl);

        //System.out.println(result_json);
        // Получение распарсенных данных по услугам
        ServiceController.result_service parsed_result= serviceController.Parsing_json_uslug(result_json, limit_usl, isCheckBox_check);
*/

        //SetOnSucceeded methode
        ServiceTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                ServiceController.result_service parsed_result= (ServiceController.result_service) ServiceTask.getValue();
                box.setDisable(true);
                pi.setVisible(false);
                vbox_usl_main.setDisable(false);
                count_uslAll_t.setText(String.valueOf(parsed_result.getAllUslug())); // Установка количества всех услуг для поля Все услуги
                count_uslAct_t.setText(String.valueOf(parsed_result.getActualUslug())); // Установка количества актуальных услуг для поля Актуальные услуги

                // Создание списка с данными по услугам
                ArrayList<DataUslug_Model> parsed_result_arr= parsed_result.getData_uslugArr();
                ObservableList<DataUslug_Model> dataUslug_models = FXCollections.observableArrayList(parsed_result_arr);

                // Заполнение таблицы данными по услугам
                id_usl.setCellValueFactory(new PropertyValueFactory<>("EidUslug"));
                id_usl.setCellFactory(TextFieldTableCell.<DataUslug_Model>forTableColumn());
                name_usl.setCellValueFactory(new PropertyValueFactory<>("NameUslug"));
                name_usl.setCellFactory(TextFieldTableCell.<DataUslug_Model>forTableColumn());

                data_table.setItems(dataUslug_models);

                // Вызов функции показать услуги
                Show_uslugs(cookie_value);
                // Создание события для кнопки скачать отчет по услугам в АИС
                download_usl_b.setOnAction(event2 -> {
                    serviceController.Download_uslugs(parsed_result_arr);
                });
            }
        });

        //bind progress bar to both task progress property

        Thread serviceThread = new Thread(ServiceTask);
        serviceThread.start();

        //return parsed_result;
        return null;
    }
    // Функция для отображение ведомств в АИС
    public void Show_data_Departm(String cookie_value) throws IOException {
            // Если открыта вкладка Ведомства в АИС
            departm_tab.setOnSelectionChanged(event -> {
                if (departm_tab.isSelected()){
                    // Вызвать контроллер класс по ведомствам
                    DepartmController departmController = new DepartmController();
                    System.out.println("Count_departm: "+count_DepartmAll_t.getText());
                    // Если вкладка с ведомствами открылась в первый раз
                    if (count_DepartmAll_t.getText().isEmpty()){
                        // Если ведомства не найдены
                        data_table_departm.setPlaceholder(new Label("Ничего не найдено!"));
                        String search_text =search_t.getText(); // Получить значение с текстового поля
                        //System.out.println(search_text);
                        String count_uslugs= count_usl_t.getText(); // Установка количества услуг для текстового поля
                        String search_text_depart=search_departm_t.getText();

                        int limit_usl=Integer.parseInt(count_uslugs);


                        String result_json= null;

                        ProgressIndicator pi = new ProgressIndicator();
                        VBox box = new VBox(pi);
                        box.setAlignment(Pos.CENTER);
                        // Grey Background
                        vbox_departm_main.setDisable(true);
                        root_depart.getChildren().add(box);
                        Task DepartmTask = new DepartmController.DepartmTask(cookie_value,search_text_depart);

                        //SetOnSucceeded methode
                        DepartmTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent event) {
                                // Получение распарсенных данных по ведомствам МфЦ
                                DepartmController.result_departm parsed_result= (DepartmController.result_departm) DepartmTask.getValue();

                                box.setDisable(true);
                                pi.setVisible(false);
                                vbox_departm_main.setDisable(false);
                                ArrayList<DataDepartm_Model> parsed_result_arr=  parsed_result.getData_departmArr();
                                //ArrayList<DataDepartm_Model> parsed_result_arr=  Parsing_json_depart(result_json);
                                //System.out.println("Parsed data:\n"+ parsed_result.get(0).getNameUslug());
                                ObservableList<DataDepartm_Model> dataDepartm_models = FXCollections.observableArrayList(parsed_result_arr);
                                // Получение списка всех ведомств и заполнение в текстовое поле
                                count_DepartmAll_t.setText(String.valueOf(parsed_result.getAllDepartm()));
                                // Заполение данными таблицы
                                id_departm.setCellValueFactory(new PropertyValueFactory<>("IdDepartm"));
                                id_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());
                                name_departm.setCellValueFactory(new PropertyValueFactory<>("NameDepartm"));
                                name_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());

                                data_table_departm.setItems(dataDepartm_models);
                                // Вызов события для кнопки скачивания отчета по ведомствам
                                download_departm_b.setOnAction(event2 -> {
                                    departmController.Download_departm(parsed_result_arr);
                                });


                                // Поиск по ведомствам
                                show_departm.setOnAction(event3 -> {
                                    // Получение списка найденных ведомств
                                    ArrayList<DataDepartm_Model> parsed_result_arr_find= departmController.Search_departm(parsed_result_arr,search_departm_t.getText());
                                    System.out.println("TEST SEARCH 111 "+ search_departm_t.getText());
                                    // Событие на кнопку скачивания ведомств
                                    download_departm_b.setOnAction(event4 -> {
                                        departmController.Download_departm(parsed_result_arr_find);
                                    });
                                    // Получение количества ведомств
                                    count_DepartmAll_t.setText(String.valueOf(parsed_result_arr_find.size()));

                                    data_table_departm.setPlaceholder(new Label("Ничего не найдено!"));

                                    ObservableList<DataDepartm_Model> dataDepartm_models_find = FXCollections.observableArrayList(parsed_result_arr_find);

                                    // Заполнение данными по таблице ведомств
                                    id_departm.setCellValueFactory(new PropertyValueFactory<>("IdDepartm"));
                                    id_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());
                                    name_departm.setCellValueFactory(new PropertyValueFactory<>("NameDepartm"));
                                    name_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());


                                    data_table_departm.setItems(dataDepartm_models_find);


                                });
                                // Событие на кнопку Enter
                                search_departm_t.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                    @Override
                                    public void handle(KeyEvent keyEvent) {
                                        if (keyEvent.getCode() == KeyCode.ENTER)  {
                                            ArrayList<DataDepartm_Model> parsed_result_arr_find= departmController.Search_departm(parsed_result_arr,search_departm_t.getText());
                                            System.out.println("TEST SEARCH 111 "+ search_departm_t.getText());
                                            download_departm_b.setOnAction(event5 -> {
                                                departmController.Download_departm(parsed_result_arr_find);
                                            });
                                            count_DepartmAll_t.setText(String.valueOf(parsed_result_arr_find.size()));

                                            data_table_departm.setPlaceholder(new Label("Ничего не найдено!"));

                                            ObservableList<DataDepartm_Model> dataDepartm_models_find = FXCollections.observableArrayList(parsed_result_arr_find);

                                            // Заполнение данными таблицы по ведомствам
                                            id_departm.setCellValueFactory(new PropertyValueFactory<>("IdDepartm"));
                                            id_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());
                                            name_departm.setCellValueFactory(new PropertyValueFactory<>("NameDepartm"));
                                            name_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());


                                            data_table_departm.setItems(dataDepartm_models_find);
                                        }
                                    }
                                });

                            }
                        });

                        //bind progress bar to both task progress property

                        Thread serviceThread = new Thread(DepartmTask);
                        serviceThread.start();


                    }
                }
            });
    }
    // Функция для заполнения данными по МФЦ
    public void Show_data_Mfc(String cookie_value) throws IOException {
        // Если выбрана вкладка по МФЦ
        mfc_tab.setOnSelectionChanged(event -> {
            if (mfc_tab.isSelected()){
                // Создание Экземпляра класса по МФЦ
                MfcController mfcController = new MfcController();
                System.out.println("Count_mfc_tab: "+count_mfcAll_t.getText());
                // Если список мфц не пуст
                if (count_mfcAll_t.getText().isEmpty()){
                    // Если ничего не найдено
                    data_table_mfc.setPlaceholder(new Label("Ничего не найдено!"));

                    String result_json= null;
                    String search= search_mfc_t.getText(); // Получение данных с текстового поля
                    boolean isCheckbox;
                    // Если чекбокс выбран
                    if (onlyWorkMFC_ch.isSelected()){
                        isCheckbox=true;
                    } else {
                        isCheckbox = false;
                    }

                    ProgressIndicator pi = new ProgressIndicator();
                    VBox box = new VBox(pi);
                    box.setAlignment(Pos.CENTER);
                    // Grey Background
                    vbox_mfc_main.setDisable(true);
                    root_mfc.getChildren().add(box);

                    Task MfcTask = new MfcController.MfcTask(cookie_value, search, isCheckbox);

                    //SetOnSucceeded methode
                    MfcTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            // Получение распарсенных данных по мфц
                            MfcController.result_mfc parsed_result= (MfcController.result_mfc) MfcTask.getValue();

                            box.setDisable(true);
                            pi.setVisible(false);
                            vbox_mfc_main.setDisable(false);
                            count_mfcAll_t.setText(String.valueOf(parsed_result.getAllMfc())); // Установка количества
                            ArrayList<DataMfc_Model> parsed_result_arr = parsed_result.getData_mfcArr();
                            ObservableList<DataMfc_Model> dataMfc_models = FXCollections.observableArrayList(parsed_result_arr);

                            // Заполнение данными таблицы
                            id_mfc.setCellValueFactory(new PropertyValueFactory<>("IdMfc"));
                            id_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());
                            name_mfc.setCellValueFactory(new PropertyValueFactory<>("NameMfc"));
                            name_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());


                            data_table_mfc.setItems(dataMfc_models);

                            // Установка ивента на кнпопку поиска по МФЦ
                            download_mfc_b.setOnAction(event2 -> {
                                mfcController.Download_mfc(parsed_result_arr);
                            });
                            Show_mfc(cookie_value);

                        }
                    });

                    //bind progress bar to both task progress property

                    Thread serviceThread = new Thread(MfcTask);
                    serviceThread.start();



                }
            }
        });
    }

}