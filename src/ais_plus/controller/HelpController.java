package ais_plus.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea help_text;

    @FXML
    void initialize() {
        help_text.setWrapText(true);
        help_text.setText("Для загрузки отчётов выберите нужную вкладку и нажмите скачать отчёт. Также вы можете скачать отчёт в верхнем меню.\n\n" +
                "Для поиска нужной информации можно использовать фильтры. Также для услуг и МФЦ есть галочка - Только актуальные. При выборе этой галочки будут показываться" +
                "только актуальные данные.\n\n" +
                "Для смены пользователя выберите в меню Аккаунт-сменить пользователя.\n\n" +
                "Для добавления начисления, выберите услугу. Далее появится окно с начислениями по услуге." +
                "После этого вы можете добавить, редактировать, удалить или дублировать начисление. При добавлении начисление появится окно с полями. Их нужно заполнить и нажать Сохранить." +
                "При дублировании начисления нужно выбрать начисление и нажать дублировать. Информация из начисления скопируется в копию.");

    }


}
