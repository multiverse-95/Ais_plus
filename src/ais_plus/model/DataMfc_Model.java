package ais_plus.model;

import javafx.beans.property.SimpleStringProperty;

public class DataMfc_Model {

    private SimpleStringProperty IdMfc;
    private SimpleStringProperty NameMfc;


    public DataMfc_Model(String IdMfc, String NameMfc) {
        this.IdMfc = new SimpleStringProperty(IdMfc);
        this.NameMfc = new SimpleStringProperty(NameMfc);

    }

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
