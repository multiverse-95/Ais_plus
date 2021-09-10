package ais_plus;

import java.awt.*;
//import java.awt.TextArea;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import ais_plus.controller.*;
import ais_plus.model.Settings_Model;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ais_plus.model.DataDepartm_Model;
import ais_plus.model.DataMfc_Model;
import ais_plus.model.DataUslug_Model;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    private AnchorPane main_anchor;

    @FXML
    private MenuBar app_menuBar;
    @FXML
    private MenuItem menu_item_rep_service;

    @FXML
    private MenuItem menu_item_rep_departm;

    @FXML
    private MenuItem menu_item_rep_mfc;

    @FXML
    private MenuItem menu_item_change_user;

    @FXML
    private MenuItem menu_item_help;

    @FXML
    private MenuItem menu_item_about;

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

   /* @FXML
    private TableView<DataUslug_Model> data_table;
    @FXML
    private TableColumn<DataUslug_Model, String> id_usl;

    @FXML
    private TableColumn<DataUslug_Model, String> name_usl;*/


    @FXML
    private ListView<DataUslug_Model> service_list;
    @FXML
    private Button show_usl_b;

    @FXML
    private Button add_accrual_b;

    @FXML
    private Button correct_with_site_b;

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
        service_list.prefWidthProperty().bind(main_anchor.widthProperty());
        show_usl_b.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });

        correct_with_site_b.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });

        download_usl_b.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });

        show_departm.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });

        download_departm_b.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });

        show_mfc_b.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });

        download_mfc_b.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });

        add_accrual_b.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });
    }


    private final Image IMAGE_SERVICE  = new Image("ais_plus/images/doc_checked.png");

    private Image[] listOfImages = {IMAGE_SERVICE};
    private class CustomListCell extends ListCell<DataUslug_Model> {
        private HBox content;
        private ImageView imageView = new ImageView();
        private Label titleService;
        private Text eidService;

        public CustomListCell() {
            super();
            eidService = new Text();
            titleService = new Label();
            VBox vBox = new VBox(titleService,eidService );
            vBox.prefWidthProperty().bind(main_anchor.widthProperty());
            vBox.setSpacing(10);
            vBox.setPrefHeight(50);
            titleService.prefWidthProperty().bind(vBox.widthProperty());
            titleService.prefHeightProperty().bind(vBox.heightProperty());
            content = new HBox(imageView, vBox);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(DataUslug_Model item, boolean empty) {

            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {

                imageView.setImage(listOfImages[0]);
                double imageWidth = 70.0;
                imageView.setFitHeight(imageWidth);
                imageView.setFitWidth(imageWidth);

                titleService.setText(item.getNameUslug());
                titleService.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
                titleService.setTextAlignment(TextAlignment.JUSTIFY);
                titleService.setWrapText(true);
                eidService.setText("eid: "+item.getEidUslug());
                eidService.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
                setGraphic(content);
            }

        }
    }

    // Функция для получения начислений по услуге
    public void Show_accruals(String cookie_value){
        // Устанавлием событие на кнопку "Начисления"
        add_accrual_b.setOnAction(event -> {
            // Если услуга не выбрана, показать предупреждение
            if (service_list.getSelectionModel().getSelectedItem() == null){
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
                DataUslug_Model dataUslug_model = service_list.getSelectionModel().getSelectedItem();
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
            // если нет результатов
            data_table_mfc.setItems(null);
            Label label_empty=new Label();
            label_empty.setText("Ничего не найдено!");
            label_empty.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
            data_table_mfc.setPlaceholder(label_empty);
            MfcController mfcController = new MfcController();
            String result_json= null;
            String search= search_mfc_t.getText(); // Считать с текстс поля поиска
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
            autoResizeColumnsMfc(data_table_mfc);
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
                    data_table_mfc.setItems(null);
                    Label label_empty=new Label();
                    label_empty.setText("Ничего не найдено!");
                    label_empty.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
                    data_table_mfc.setPlaceholder(label_empty);
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
        String search_text =search_t.getText(); // Получение значения с текстового поля поиска
        System.out.println(search_text);

        // Установка размера для столбцов таблицы
        /*id_usl.prefWidthProperty().bind(data_table.widthProperty().multiply(0.12));
        name_usl.prefWidthProperty().bind(data_table.widthProperty().multiply(0.88));*/

        //id_usl.setResizable(false);
        //name_usl.setResizable(false);
        menu_item_rep_service.setDisable(true);
        service_list.setItems(null);
        //data_table.setItems(null);
        // Запуск ПрогрессИндикатора
        ProgressIndicator pi = new ProgressIndicator();
        VBox box = new VBox(pi);
        box.setAlignment(Pos.CENTER);
        // Grey Background
        vbox_usl_main.setDisable(true);
        service_list.setDisable(true);
        /*data_table.setDisable(true);
        Label label_load=new Label();
        label_load.setText("Загрузка данных...");
        label_load.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        data_table.setPlaceholder(label_load);*/
        root_usl.getChildren().add(box);
        // Инициализация потока с получением услуг с сервера
        Task ServiceTask = new ServiceController.ServiceTask(cookie_value,search_text , 25, isCheckBox_check);


        // После выполнения потока
        ServiceTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                // Получение результа после выполнения потока
                ServiceController.result_service parsed_result= (ServiceController.result_service) ServiceTask.getValue();
                // Закрытие прогресс индикатора
                box.setDisable(true);
                pi.setVisible(false);
                vbox_usl_main.setDisable(false);
                service_list.setDisable(false);
                //data_table.setDisable(false);
                menu_item_rep_service.setDisable(false);
                count_uslAll_t.setText(String.valueOf(parsed_result.getAllUslug())); // Установка количества всех услуг для поля Все услуги
                count_uslAct_t.setText(String.valueOf(parsed_result.getActualUslug())); // Установка количества актуальных услуг для поля Актуальные услуги
                // Если ничего не найдено
                /*if (parsed_result.getActualUslug()==0){
                    Label label_empty=new Label();
                    label_empty.setText("Ничего не найдено!");
                    label_empty.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
                    data_table.setPlaceholder(label_empty);
                }*/

                // Создание списка с данными по услугам
                ArrayList<DataUslug_Model> parsed_result_arr= parsed_result.getData_uslugArr();
               // ObservableList<DataUslug_Model> dataUslug_models = FXCollections.observableArrayList(parsed_result_arr);
                ObservableList<DataUslug_Model> dataUslug_models = FXCollections.observableArrayList();
                dataUslug_models.addAll(parsed_result_arr);

                service_list.setCellFactory(listView -> new CustomListCell());

                service_list.setItems(dataUslug_models);

                /*
                // Заполнение таблицы данными по услугам
                id_usl.setCellValueFactory(new PropertyValueFactory<>("EidUslug"));
                id_usl.setCellFactory(TextFieldTableCell.<DataUslug_Model>forTableColumn());
                name_usl.setCellValueFactory(new PropertyValueFactory<>("NameUslug"));
                name_usl.setCellFactory(TextFieldTableCell.<DataUslug_Model>forTableColumn());

                data_table.setItems(dataUslug_models);*/

                // Вызов функции показать услуги
                Show_uslugs(cookie_value);
                // Создание события для кнопки скачать отчет по услугам в АИС
                download_usl_b.setOnAction(event2 -> {
                    serviceController.Download_uslugs(parsed_result_arr);
                });

                // When user click on the Exit item.
                menu_item_rep_service.setOnAction(event_mi_rep_serv -> serviceController.Download_uslugs(parsed_result_arr));
                correct_with_site_b.setOnAction(corr_w_s->{
                    try {
                        openCompareReport(parsed_result_arr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        });

        ServiceTask.setOnCancelled(event_cancel -> {
            System.out.println("Cancel Task!!!");
        });

        // Запуск потока
        Thread serviceThread = new Thread(ServiceTask);
        serviceThread.setDaemon(true);
        serviceThread.start();

        // При нажатии на элемент меню "Сменить пользователя"
        menu_item_change_user.setOnAction(event_change_user -> {
            ServiceTask.cancel();
            // Считать данные с файла
            File fileJson = new File("C:\\ais_plus\\settingsAIS.json");

            JsonParser parser = new JsonParser();
            JsonElement jsontree = null;
            try {
                jsontree = parser.parse(new BufferedReader(new InputStreamReader(new FileInputStream("C:\\ais_plus\\settingsAIS.json"), StandardCharsets.UTF_8)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Сохранить путь для сохранения отчёта, остальное удалить
            JsonObject jsonObject = jsontree.getAsJsonObject();
            String lastPathToFile = jsonObject.get("lastPathToFile").getAsString();
            Settings_Model settingsModel = new Settings_Model("", "", "", lastPathToFile, false);
            settingsModel.setLogin("");
            settingsModel.setPassword("");
            settingsModel.setCookie("");
            settingsModel.setLastPathToFile(lastPathToFile);
            settingsModel.setCheckBoxSel(false);

            Gson gson = new Gson();
            String content = gson.toJson(settingsModel);
            try {
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileJson), StandardCharsets.UTF_8));
                out.write(content);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            app_menuBar.getScene().getWindow().hide();// Скрываем окно программы
            // Запускаем окно авторизации
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ais_plus/view/login.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Ais plus");
            stage.setResizable(false);
            stage.setScene(new Scene(root, 400, 250));
            stage.show();

        });
        // Показывает окно помощи
        menu_item_help.setOnAction(event_help -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ais_plus/view/help.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Помощь");
            stage.setResizable(false);
            stage.setScene(new Scene(root, 600, 400));
            stage.showAndWait();
        });
        // Показывает окно "О программе"
        menu_item_about.setOnAction(event_about -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ais_plus/view/about.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("О приложении");
            stage.setResizable(false);
            stage.setScene(new Scene(root, 545, 163));
            stage.showAndWait();
        });



        //return parsed_result;
        return null;
    }
    // Функция для отображение ведомств в АИС
    public void Show_data_Departm(String cookie_value) throws IOException {


        // When user click on the Exit item.
        menu_item_rep_departm.setOnAction(event_dep_rep -> {
            DepartmController departmController = new DepartmController();
            String search_text_depart=search_departm_t.getText(); // Получить значение с текстового поля
            Task DepartmTask = new DepartmController.DepartmTask(cookie_value,search_text_depart);
            //  После выполнения потока
            DepartmTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    DepartmController.result_departm parsed_result= (DepartmController.result_departm) DepartmTask.getValue();
                    ArrayList<DataDepartm_Model> parsed_result_arr=  parsed_result.getData_departmArr();
                    departmController.Download_departm(parsed_result_arr);
                }
                });
            // Запуск потока
            Thread departmThread = new Thread(DepartmTask);
            departmThread.setDaemon(true);
            departmThread.start();
        });
        // Если открыта вкладка Ведомства в АИС
            departm_tab.setOnSelectionChanged(event -> {
                if (departm_tab.isSelected()){
                    // Установка ширины столбцов для таблицы
                    //id_departm.prefWidthProperty().bind(data_table_departm.widthProperty().multiply(0.2));
                    //name_departm.prefWidthProperty().bind(data_table_departm.widthProperty().multiply(0.8));

                    // Вызвать контроллер класс по ведомствам
                    DepartmController departmController = new DepartmController();
                    System.out.println("Count_departm: "+count_DepartmAll_t.getText());
                    // Если вкладка с ведомствами открылась в первый раз
                    if (count_DepartmAll_t.getText().isEmpty()){
                        // Если ведомства не найдены
                        Label label_empty=new Label();
                        label_empty.setText("Ничего не найдено!");
                        label_empty.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
                        data_table_departm.setPlaceholder(label_empty);
                        //String search_text =search_t.getText(); // Получить значение с текстового поля
                        //System.out.println(search_text);

                        String search_text_depart=search_departm_t.getText(); // Получить значение с текстового поля

                        data_table_departm.setItems(null);
                        ProgressIndicator pi = new ProgressIndicator(); // Запуск прогресс индикатора
                        VBox box = new VBox(pi);
                        box.setAlignment(Pos.CENTER);
                        data_table_departm.setDisable(true);
                        Label label_load=new Label();
                        label_load.setText("Загрузка данных...");
                        label_load.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
                        data_table_departm.setPlaceholder(label_load);

                        vbox_departm_main.setDisable(true);
                        root_depart.getChildren().add(box);
                        // Инициализация потока для ведомств
                        Task DepartmTask = new DepartmController.DepartmTask(cookie_value,search_text_depart);

                        //  После выполнения потока
                        DepartmTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent event) {
                                // Получение распарсенных данных по ведомствам МфЦ
                                DepartmController.result_departm parsed_result= (DepartmController.result_departm) DepartmTask.getValue();
                                // Закрытие прогресс индикатора
                                box.setDisable(true);
                                pi.setVisible(false);
                                vbox_departm_main.setDisable(false);
                                data_table_departm.setDisable(false);
                                // Получение данных с распарсенного поля
                                ArrayList<DataDepartm_Model> parsed_result_arr=  parsed_result.getData_departmArr();
                                //ArrayList<DataDepartm_Model> parsed_result_arr=  Parsing_json_depart(result_json);
                                //System.out.println("Parsed data:\n"+ parsed_result.get(0).getNameUslug());
                                ObservableList<DataDepartm_Model> dataDepartm_models = FXCollections.observableArrayList(parsed_result_arr);
                                // Получение списка всех ведомств и заполнение в текстовое поле
                                count_DepartmAll_t.setText(String.valueOf(parsed_result.getAllDepartm()));
                                // Заполнение данными таблицы
                                id_departm.setCellValueFactory(new PropertyValueFactory<>("IdDepartm"));
                                id_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());
                                name_departm.setCellValueFactory(new PropertyValueFactory<>("NameDepartm"));
                                name_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());

                                data_table_departm.setItems(dataDepartm_models);
                                autoResizeColumnsDepartm(data_table_departm);
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
                                    Label label_empty=new Label();
                                    label_empty.setText("Ничего не найдено!");
                                    label_empty.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
                                    data_table_departm.setPlaceholder(label_empty);

                                    ObservableList<DataDepartm_Model> dataDepartm_models_find = FXCollections.observableArrayList(parsed_result_arr_find);

                                    // Заполнение данными по таблице ведомств
                                    id_departm.setCellValueFactory(new PropertyValueFactory<>("IdDepartm"));
                                    id_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());
                                    name_departm.setCellValueFactory(new PropertyValueFactory<>("NameDepartm"));
                                    name_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());


                                    data_table_departm.setItems(dataDepartm_models_find);
                                    autoResizeColumnsDepartm(data_table_departm);


                                });
                                // Событие на кнопку Enter
                                search_departm_t.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                    @Override
                                    public void handle(KeyEvent keyEvent) {
                                        if (keyEvent.getCode() == KeyCode.ENTER)  {
                                            data_table_departm.setItems(null);
                                            ArrayList<DataDepartm_Model> parsed_result_arr_find= departmController.Search_departm(parsed_result_arr,search_departm_t.getText());
                                            System.out.println("TEST SEARCH 111 "+ search_departm_t.getText());
                                            download_departm_b.setOnAction(event5 -> {
                                                departmController.Download_departm(parsed_result_arr_find);
                                            });
                                            count_DepartmAll_t.setText(String.valueOf(parsed_result_arr_find.size()));
                                            Label label_empty=new Label();
                                            label_empty.setText("Ничего не найдено!");
                                            label_empty.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
                                            data_table_departm.setPlaceholder(label_empty);

                                            ObservableList<DataDepartm_Model> dataDepartm_models_find = FXCollections.observableArrayList(parsed_result_arr_find);

                                            // Заполнение данными таблицы по ведомствам
                                            id_departm.setCellValueFactory(new PropertyValueFactory<>("IdDepartm"));
                                            id_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());
                                            name_departm.setCellValueFactory(new PropertyValueFactory<>("NameDepartm"));
                                            name_departm.setCellFactory(TextFieldTableCell.<DataDepartm_Model>forTableColumn());


                                            data_table_departm.setItems(dataDepartm_models_find);
                                            //autoResizeColumnsDepartm(data_table_departm);
                                        }
                                    }
                                });

                            }
                        });

                        // Запуск потока
                        Thread departmThread = new Thread(DepartmTask);
                        departmThread.setDaemon(true);
                        departmThread.start();


                    }
                }
            });
    }
    // Функция для заполнения данными по МФЦ
    public void Show_data_Mfc(String cookie_value) throws IOException {
        // При выборе в меню скачать отчёт по мфц
        menu_item_rep_mfc.setOnAction(event_dep_rep -> {
            MfcController mfcController = new MfcController();
            String search= search_mfc_t.getText(); // Получение данных с текстового поля
            boolean isCheckbox;
            // Если чекбокс выбран
            if (onlyWorkMFC_ch.isSelected()){
                isCheckbox=true;
            } else {
                isCheckbox = false;
            }
            // Поток по мфц
            Task MfcTask = new MfcController.MfcTask(cookie_value, search, isCheckbox);
            //  После выполнения потока
            MfcTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    MfcController.result_mfc parsed_result= (MfcController.result_mfc) MfcTask.getValue();
                    ArrayList<DataMfc_Model> parsed_result_arr = parsed_result.getData_mfcArr();
                    mfcController.Download_mfc(parsed_result_arr);
                }
            });
            // Запуск потока
            Thread mfcThread = new Thread(MfcTask);
            mfcThread.setDaemon(true);
            mfcThread.start();
        });

        // Если выбрана вкладка по МФЦ
        mfc_tab.setOnSelectionChanged(event -> {
            if (mfc_tab.isSelected()){
                // Установка ширины таблицы
                //id_mfc.prefWidthProperty().bind(data_table_mfc.widthProperty().multiply(0.12));
                //name_mfc.prefWidthProperty().bind(data_table_mfc.widthProperty().multiply(0.88));
                // Создание Экземпляра класса по МФЦ
                MfcController mfcController = new MfcController();
                System.out.println("Count_mfc_tab: "+count_mfcAll_t.getText());
                // Если список мфц не пуст
                if (count_mfcAll_t.getText().isEmpty()){
                    // Если ничего не найдено
                    //data_table_mfc.setPlaceholder(new Label("Ничего не найдено!"));

                    String result_json= null;
                    String search= search_mfc_t.getText(); // Получение данных с текстового поля
                    boolean isCheckbox;
                    // Если чекбокс выбран
                    if (onlyWorkMFC_ch.isSelected()){
                        isCheckbox=true;
                    } else {
                        isCheckbox = false;
                    }

                    data_table_mfc.setItems(null);
                    // Запуск прогресс индикатора
                    ProgressIndicator pi = new ProgressIndicator();
                    VBox box = new VBox(pi);
                    box.setAlignment(Pos.CENTER);
                    data_table_mfc.setDisable(true);
                    Label label_load=new Label();
                    label_load.setText("Загрузка данных...");
                    label_load.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
                    data_table_mfc.setPlaceholder(label_load);
                    // Grey Background
                    vbox_mfc_main.setDisable(true);
                    root_mfc.getChildren().add(box);
                    // Инициализация потока с получением данных по МФЦ
                    Task MfcTask = new MfcController.MfcTask(cookie_value, search, isCheckbox);

                    // После выполнения потока
                    MfcTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            // Получение распарсенных данных по мфц
                            MfcController.result_mfc parsed_result= (MfcController.result_mfc) MfcTask.getValue();
                            // Закрытие прогресса индикации
                            box.setDisable(true);
                            pi.setVisible(false);
                            vbox_mfc_main.setDisable(false);
                            data_table_mfc.setDisable(false);
                            count_mfcAll_t.setText(String.valueOf(parsed_result.getAllMfc())); // Установка количества
                            // Если МФЦ не нашлись
                            if (parsed_result.getAllMfc()==0){
                                Label label_empty=new Label();
                                label_empty.setText("Ничего не найдено!");
                                label_empty.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
                                data_table_mfc.setPlaceholder(label_empty);
                            }
                            ArrayList<DataMfc_Model> parsed_result_arr = parsed_result.getData_mfcArr();
                            ObservableList<DataMfc_Model> dataMfc_models = FXCollections.observableArrayList(parsed_result_arr);

                            // Заполнение данными таблицы
                            id_mfc.setCellValueFactory(new PropertyValueFactory<>("IdMfc"));
                            id_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());
                            name_mfc.setCellValueFactory(new PropertyValueFactory<>("NameMfc"));
                            name_mfc.setCellFactory(TextFieldTableCell.<DataMfc_Model>forTableColumn());


                            data_table_mfc.setItems(dataMfc_models);
                            autoResizeColumnsMfc(data_table_mfc);

                            // Установка ивента на кнпопку поиска по МФЦ
                            download_mfc_b.setOnAction(event2 -> {
                                mfcController.Download_mfc(parsed_result_arr);
                            });
                            Show_mfc(cookie_value);

                        }
                    });

                    // Запуск потока
                    Thread mfcThread = new Thread(MfcTask);
                    mfcThread.setDaemon(true);
                    mfcThread.start();



                }
            }
        });
    }

    public void openCompareReport(ArrayList<DataUslug_Model> parsed_result_arr) throws IOException {
        ArrayList<String> listFromExport=new ArrayList<String>();
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
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        SaveLastPathController saveLastPathController=new SaveLastPathController();
        String lastPathDirectory= saveLastPathController.getLastDirectory();
        if (!lastPathDirectory.equals("")){
            fileChooser.setInitialDirectory(new File(lastPathDirectory));
        }
        fileChooser.setTitle("Выберите файл exportMfcService");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Excel file (*.xlsx)", "*.xlsx");//Расширение
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);//Указываем текущую сцену CodeNote.mainStage
        if (file != null) {
            //Open
            System.out.println("Процесс открытия файла");


            // Read XSL file
            FileInputStream inputStream = new FileInputStream(file);

            // Get the workbook instance for XLS file
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                // Get iterator to all cells of current row
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    // Change to getCellType() if using POI 4.x
                    CellType cellType = cell.getCellTypeEnum();

                    switch (cellType) {
                        case _NONE:
                            //System.out.print("");
                            //System.out.print("\t");
                            break;
                        case BOOLEAN:
                            //System.out.print(cell.getBooleanCellValue());
                            //System.out.print("\t");
                            break;
                        case BLANK:
                            //System.out.print("");
                            //System.out.print("\t");
                            break;
                        case FORMULA:
                            // Formula
                            //System.out.print(cell.getCellFormula());
                            //System.out.print("\t");

                            //FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                            // Print out value evaluated by formula
                            //System.out.print(evaluator.evaluate(cell).getNumberValue());
                            break;
                        case NUMERIC:
                            //System.out.print(cell.getNumericCellValue());
                            //System.out.print("\t");
                            break;
                        case STRING:
                            //System.out.print(cell.getStringCellValue());
                            listFromExport.add(cell.getStringCellValue());
                            //System.out.print("\t");
                            break;
                        case ERROR:
                            //System.out.print("!");
                            //System.out.print("\t");
                            break;
                    }
                    break;
                }
            }

            ArrayList<String> ActualList = new ArrayList<String>();
            ArrayList<String> completeListDeleted = new ArrayList<String>();
            for (int i = 0; i < listFromExport.size(); i++) {
                String IdServiceFromServer = listFromExport.get(i).toLowerCase();
                for (int j = 0; j < parsed_result_arr.size(); j++) {
                    String IdService = parsed_result_arr.get(j).getEidUslug() + parsed_result_arr.get(j).getLidUslug();
                    if (IdServiceFromServer.toLowerCase().equals(IdService)) {
                        ActualList.add(IdService);
                    }
                }

            }


            for (int i = 0; i < ActualList.size(); i++) {
                System.out.println("Eid actual: " + ActualList.get(i));
            }


            for (int i = 0; i < ActualList.size(); i++) {
                String IdServiceActual = ActualList.get(i);
                for (int j = 0; j < listFromExport.size(); j++) {
                    String IdServiceFromExport = listFromExport.get(j);
                    if (IdServiceFromExport.equals(IdServiceActual)) {
                        listFromExport.remove(j);
                        break;
                    }
                }
            }
            completeListDeleted = listFromExport;
            completeListDeleted.remove(0);
            //completeListDeleted.remove(0);
            ArrayList<DataUslug_Model> dataService_model_arr = new ArrayList<DataUslug_Model>();
            System.out.println("DELETED LIST ID: ");
            for (int i = 0; i < completeListDeleted.size(); i++) {
                if (completeListDeleted.get(i).isEmpty()) {
                    continue;
                }
                System.out.println("Eid deleted: " + completeListDeleted.get(i));
                dataService_model_arr.add(new DataUslug_Model(completeListDeleted.get(i), "", ""));
            }

            final String[] absolutePathToFile = {""};
            ButtonType saveResult = new ButtonType("Сохранить в Excel", ButtonBar.ButtonData.OK_DONE); // Создание кнопки "Открыть отчёт"
            ButtonType cancelBut = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE); // Создание кнопки "Открыть папку с отчётом"
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Test", saveResult, cancelBut);
            alert.setTitle("Информация!"); // Название предупреждения
            alert.setHeaderText("Удаленные услуги найдены!"); // Текст предупреждения
            alert.setContentText("Выберите действие, что вы хотите сделать с результатом...");
            // Вызов подтверждения элемента
            alert.showAndWait().ifPresent(rs -> {
                if (rs == saveResult) { // Если выбрали открыть отчёт
                    absolutePathToFile[0] = Download_Deleted_Services(dataService_model_arr);
                    try {
                        Desktop.getDesktop().open(new File(absolutePathToFile[0]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (rs == cancelBut) { // Если выбрали открыт папку

                }
            });

        }
    }

    public String Download_Deleted_Services(ArrayList<DataUslug_Model> dataService_models_arr) {

        String absolutePathToFile="";
        //System.out.println(text_test);
        // Создание экземпляр класса FileChooser
        FileChooser fileChooser = new FileChooser();
        SaveLastPathController saveLastPathController=new SaveLastPathController();
        String lastPathDirectory= saveLastPathController.getLastDirectory();
        if (!lastPathDirectory.equals("")){
            fileChooser.setInitialDirectory(new File(lastPathDirectory));
        }

        // Устанавливаем список расширений для файла
        fileChooser.setInitialFileName("services_DELETED"); // Устанавливаем название для файла
        // Список расширений для Excel
        FileChooser.ExtensionFilter extFilterExcel = new FileChooser.ExtensionFilter("Excel file (*.xlsx)", "*.xlsx");

        // Добавляем список расширений
        fileChooser.getExtensionFilters().add(extFilterExcel);

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
                        absolutePathToFile= SaveFileExcel(dataService_models_arr, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return absolutePathToFile;


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
    public String SaveFileExcel (ArrayList<DataUslug_Model> dataService_model_arr, File file) throws IOException {
        // Создание книги Excel
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Создание листа
        XSSFSheet sheet = workbook.createSheet("Service DELETED");


        int rownum = 0;
        Cell cell;
        Row row;
        // Установка стилей
        XSSFCellStyle style = createStyleForTitleNew(workbook);

        row = sheet.createRow(rownum);

        // Создание столбца EidUslug
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("ID Услуги");
        cell.setCellStyle(style);


        // Перебор по данным
        for (DataUslug_Model service_model : dataService_model_arr) {
            //System.out.println(mfc_model.getIdMfc() +"\t" +mfc_model.getNameMfc());
            rownum++;
            row = sheet.createRow(rownum);

            // Запись Eid Услуги
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(service_model.getEidUslug());

        }

        autoSizeColumns(workbook);
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

    // Функция для автоматического выравнивания столбцов по ширине содержимого
    public static void autoSizeColumns(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets(); // Получаем кол-во листов
        for (int i = 0; i < numberOfSheets; i++) { // Идём по кол-ву листов
            Sheet sheet = workbook.getSheetAt(i); // Получаем лист
            if (sheet.getPhysicalNumberOfRows() > 0) { // Если столбцов больше 0
                Row row = sheet.getRow(sheet.getFirstRowNum()); // Получить первую строку
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) { // Идём по строкам и выравниваем по ширине
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    sheet.autoSizeColumn(columnIndex);
                }
            }
        }
    }

    // Функция для выравнивания колонок в таблице по ширине текста
    public static void autoResizeColumnsMfc( TableView<DataMfc_Model> table )
    {
        table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach( (column) ->
        {
            // Получение минимальной ширины
            Text t = new Text( column.getText() );
            double max = t.getLayoutBounds().getWidth();
            for ( int i = 0; i < table.getItems().size(); i++ )
            {
                // Столбцы не должны быть пустыми
                if ( column.getCellData( i ) != null )
                {
                    t = new Text( column.getCellData( i ).toString() ); // Получить текст со столбца
                    double calcwidth = t.getLayoutBounds().getWidth(); // Получить ширину текста
                    // Запомнить новую макс ширину
                    if ( calcwidth > max )
                    {
                        max = calcwidth;
                    }
                }
            }
            // Добавить к максимальной ширине немного пространства
            column.setPrefWidth( max + 12.0d );
        } );
    }

    // Функция для выравнивания колонок в таблице по ширине текста
    public static void autoResizeColumnsDepartm( TableView<DataDepartm_Model> table )
    {
        table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().stream().forEach( (column) ->
        {
            // Получение минимальной ширины
            Text t = new Text( column.getText() );
            double max = t.getLayoutBounds().getWidth();
            for ( int i = 0; i < table.getItems().size(); i++ )
            {
                // Столбцы не должны быть пустыми
                if ( column.getCellData( i ) != null )
                {
                    t = new Text( column.getCellData( i ).toString() ); // Получить текст со столбца
                    double calcwidth = t.getLayoutBounds().getWidth(); // Получить ширину текста
                    // Запомнить новую макс ширину
                    if ( calcwidth > max )
                    {
                        max = calcwidth;
                    }
                }
            }
            // Добавить к максимальной ширине немного пространства
            column.setPrefWidth( max + 12.0d );
        } );
    }


}