package ais_plus.model;

import javafx.beans.property.SimpleStringProperty;

public class DataUslug_Model {

    private SimpleStringProperty EidUslug; // eid услуги
    private SimpleStringProperty LidUslug; // lid услуги
    private SimpleStringProperty NameUslug; // Название услуги


    public DataUslug_Model(String EidUslug, String LidUslug, String NameUslug) {
        this.EidUslug = new SimpleStringProperty(EidUslug);
        this.LidUslug = new SimpleStringProperty(LidUslug);
        this.NameUslug = new SimpleStringProperty(NameUslug);

    }
    // Геттеры и сеттеры
    public String getEidUslug() {
        return EidUslug.get();
    }

    public void setEidUslug(String EidUslug) {
        this.EidUslug = new SimpleStringProperty(EidUslug);
    }

    public String getLidUslug() {
        return LidUslug.get();
    }

    public void setLidUslug(String LidUslug) {
        this.LidUslug = new SimpleStringProperty(LidUslug);
    }

    public String getNameUslug() {
        return NameUslug.get();
    }

    public void setNameUslug(String NameUslug) {
        this.NameUslug = new SimpleStringProperty(NameUslug);
    }

}
