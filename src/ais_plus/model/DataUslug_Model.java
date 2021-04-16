package ais_plus.model;

import javafx.beans.property.SimpleStringProperty;

public class DataUslug_Model {

    private SimpleStringProperty EidUslug;
    private SimpleStringProperty LidUslug;
    private SimpleStringProperty NameUslug;


    public DataUslug_Model(String EidUslug, String LidUslug, String NameUslug) {
        this.EidUslug = new SimpleStringProperty(EidUslug);
        this.LidUslug = new SimpleStringProperty(LidUslug);
        this.NameUslug = new SimpleStringProperty(NameUslug);

    }

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
