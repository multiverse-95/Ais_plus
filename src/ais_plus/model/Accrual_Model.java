package ais_plus.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
// Модель для данных по начислениям
public class Accrual_Model {
    private SimpleStringProperty IdAccr; // id начисления
    private SimpleStringProperty IdPayment; // Код платежа
    private SimpleStringProperty DisplayName; // Имя начисления
    private SimpleStringProperty Person; // Физическое лицо
    private SimpleStringProperty SoloProprietor; // Индивидуальный предприниматель
    private SimpleStringProperty Legal; // Юридическое лицо
    private SimpleStringProperty Hint; // Подсказка
    private SimpleStringProperty Name; // Имя платежа
    private SimpleStringProperty BankName; // Имя банка получателя
    private SimpleStringProperty PersonalAccount; // Счет получателя
    private SimpleStringProperty CorrespAccount; // Корресп. счет получателя
    private SimpleStringProperty Bic; // бик
    private SimpleStringProperty Kpp; // кпп
    private SimpleStringProperty Inn; // инн
    private SimpleStringProperty RegType; // Тип платежа
    private SimpleStringProperty MfcsOKTMO; // МФЦ соотв. октмо
    private SimpleStringProperty Oktmo; // ОКТМО
    private SimpleStringProperty Cbc; // кбк
    private SimpleStringProperty Urn; // Урн
    private SimpleStringProperty CbcSection; // Код кбк
    private SimpleStringProperty PreCheckRequired; // Пречерк
    private SimpleStringProperty Purpose; // Назначение платежа


    public Accrual_Model(String IdAccr, String IdPayment, String DisplayName, String Person, String SoloProprietor, String Legal, String Hint, String Name, String BankName, String PersonalAccount,
                         String CorrespAccount, String Bic, String Kpp, String Inn, String RegType, String MfcsOKTMO, String Oktmo, String Cbc, String Urn,
                         String CbcSection, String PreCheckRequired, String Purpose) {
        this.IdAccr = new SimpleStringProperty(IdAccr);
        this.IdPayment = new SimpleStringProperty(IdPayment);
        this.DisplayName = new SimpleStringProperty(DisplayName);
        this.Person = new SimpleStringProperty(Person);
        this.SoloProprietor = new SimpleStringProperty(SoloProprietor);
        this.Legal = new SimpleStringProperty(Legal);
        this.Hint = new SimpleStringProperty(Hint);
        this.Name = new SimpleStringProperty(Name);
        this.BankName = new SimpleStringProperty(BankName);
        this.PersonalAccount = new SimpleStringProperty(PersonalAccount);
        this.CorrespAccount = new SimpleStringProperty(CorrespAccount);
        this.Bic = new SimpleStringProperty(Bic);
        this.Kpp = new SimpleStringProperty(Kpp);
        this.Inn = new SimpleStringProperty(Inn);
        this.RegType = new SimpleStringProperty(RegType);
        this.MfcsOKTMO = new SimpleStringProperty(MfcsOKTMO);
        this.Oktmo = new SimpleStringProperty(Oktmo);
        this.Cbc = new SimpleStringProperty(Cbc);
        this.Urn = new SimpleStringProperty(Urn);
        this.CbcSection = new SimpleStringProperty(CbcSection);
        this.PreCheckRequired = new SimpleStringProperty(PreCheckRequired);
        this.Purpose = new SimpleStringProperty(Purpose);


    }
    // Сеттеры и геттеры для полей
    public String getIdAccr() {
        return IdAccr.get();
    }
    public void setIdAccr(String IdAccr) {
        this.IdAccr = new SimpleStringProperty(IdAccr);
    }
    public String getIdPayment() {
        return IdPayment.get();
    }
    public void setIdPayment(String IdPayment) {
        this.IdPayment = new SimpleStringProperty(IdPayment);
    }
    public String getDisplayName() {
        return DisplayName.get();
    }
    public void setDisplayName(String DisplayName) {
        this.DisplayName = new SimpleStringProperty(DisplayName);
    }
    public String getPerson() {
        return Person.get();
    }
    public void setPerson(String Person) {
        this.Person = new SimpleStringProperty(Person);
    }
    public String getSoloProprietor() {
        return SoloProprietor.get();
    }
    public void setSoloProprietor(String SoloProprietor) { this.SoloProprietor = new SimpleStringProperty(SoloProprietor); }
    public String getLegal() {
        return Legal.get();
    }
    public void setLegal(String Legal) {
        this.Legal = new SimpleStringProperty(Legal);
    }
    public String getHint() {
        return Hint.get();
    }
    public void setHint(String Hint) {
        this.Hint = new SimpleStringProperty(Hint);
    }
    public String getName() {
        return Name.get();
    }
    public void setName(String Name) {
        this.Name = new SimpleStringProperty(Name);
    }
    public String getBankName() {
        return BankName.get();
    }
    public void setBankName(String BankName) {
        this.BankName = new SimpleStringProperty(BankName);
    }
    public String getPersonalAccount() {
        return PersonalAccount.get();
    }
    public void setPersonalAccount(String PersonalAccount) { this.PersonalAccount = new SimpleStringProperty(PersonalAccount); }
    public String getCorrespAccount() {
        return CorrespAccount.get();
    }
    public void setCorrespAccount(String CorrespAccount) { this.CorrespAccount = new SimpleStringProperty(CorrespAccount); }
    public String getBic() {
        return Bic.get();
    }
    public void setBic(String Bic) {
        this.Bic = new SimpleStringProperty(Bic);
    }
    public String getKpp() {
        return Kpp.get();
    }
    public void setKpp(String Kpp) {
        this.Kpp = new SimpleStringProperty(Kpp);
    }
    public String getInn() {
        return Inn.get();
    }
    public void setInn(String Inn) {
        this.Inn = new SimpleStringProperty(Inn);
    }
    public String getRegType() {
        return RegType.get();
    }
    public void setRegType(String RegType) {
        this.RegType = new SimpleStringProperty(RegType);
    }
    public String getMfcsOKTMO() {
        return MfcsOKTMO.get();
    }
    public void setMfcsOKTMO(String MfcsOKTMO) {
        this.MfcsOKTMO = new SimpleStringProperty(MfcsOKTMO);
    }
    public String getOktmo() {
        return Oktmo.get();
    }
    public void setOktmo(String Oktmo) {
        this.Oktmo = new SimpleStringProperty(Oktmo);
    }
    public String getCbc() {
        return Cbc.get();
    }
    public void setCbc(String Cbc) {
        this.Cbc = new SimpleStringProperty(Cbc);
    }
    public String getUrn() {
        return Urn.get();
    }
    public void setUrn(String Urn) {
        this.Urn = new SimpleStringProperty(Urn);
    }
    public String getCbcSection() {
        return CbcSection.get();
    }
    public void setCbcSection(String CbcSection) {
        this.CbcSection = new SimpleStringProperty(CbcSection);
    }
    public String getPreCheckRequired() {
        return PreCheckRequired.get();
    }
    public void setPreCheckRequired(String PreCheckRequired) { this.PreCheckRequired = new SimpleStringProperty(PreCheckRequired); }
    public String getPurpose() {
        return Purpose.get();
    }
    public void setPurpose(String Purpose) {
        this.Purpose = new SimpleStringProperty(Purpose);
    }

}

