package ais_plus.model;

import javafx.beans.property.SimpleStringProperty;

public class DataMfc_Model {

    private SimpleStringProperty IdMfc; // id мфц
    private SimpleStringProperty NameMfc; // Название мфц


    public DataMfc_Model(String IdMfc, String NameMfc) {
        this.IdMfc = new SimpleStringProperty(IdMfc);
        this.NameMfc = new SimpleStringProperty(NameMfc);

    }
    // Геттеры и сеттеры
    public String getIdMfc() {
        return IdMfc.get();
    }

    public void setIdMfc(String IdMfc) {
        this.IdMfc = new SimpleStringProperty(IdMfc);
    }

    public String getNameMfc() {
        return NameMfc.get();
    }

    public void setNameMfc(String NameMfc) {
        this.NameMfc = new SimpleStringProperty(NameMfc);
    }
}
