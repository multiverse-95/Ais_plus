package ais_plus.model;
import javafx.beans.property.SimpleStringProperty;

public class DataDepartm_Model {

    private SimpleStringProperty IdDepartm;
    private SimpleStringProperty NameDepartm;


    public DataDepartm_Model(String IdDepartm, String NameDepartm) {
        this.IdDepartm = new SimpleStringProperty(IdDepartm);
        this.NameDepartm = new SimpleStringProperty(NameDepartm);

    }

    public String getIdDepartm() {
        return IdDepartm.get();
    }

    public void setIdDepartm(String IdDepartm) {
        this.IdDepartm = new SimpleStringProperty(IdDepartm);
    }

    public String getNameDepartm() {
        return NameDepartm.get();
    }

    public void setNameDepartm(String NameDepartm) {
        this.NameDepartm = new SimpleStringProperty(NameDepartm);
    }
}
