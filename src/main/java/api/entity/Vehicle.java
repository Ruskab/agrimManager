package api.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String registrationPlate;

    private String brand;

    @JoinColumn
    @ManyToOne
    private Client client;

    private String KMS;
    private String BodyOnFrame; //bastidor
    private LocalDate lastRevisionDate;
    private LocalDate ItvDate;
    private LocalDate nextItvDate;
    private String airFilterReference;
    private String oilFilterReference;
    private String fuelFilter;
    private String motorOil;

    public Vehicle() {
        //JPA
    }

    public Vehicle(String registrationPlate, String brand, Client client, String KMS, String bodyOnFrame, LocalDate lastRevisionDate, LocalDate itvDate, LocalDate nextItvDate, String airFilterReference, String oilFilterReference, String fuelFilter, String motorOil) {
        this.registrationPlate = registrationPlate;
        this.brand = brand;
        this.client = client;
        this.KMS = KMS;
        BodyOnFrame = bodyOnFrame;
        this.lastRevisionDate = lastRevisionDate;
        ItvDate = itvDate;
        this.nextItvDate = nextItvDate;
        this.airFilterReference = airFilterReference;
        this.oilFilterReference = oilFilterReference;
        this.fuelFilter = fuelFilter;
        this.motorOil = motorOil;
    }

    public int getId() {
        return id;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getKMS() {
        return KMS;
    }

    public void setKMS(String KMS) {
        this.KMS = KMS;
    }

    public String getBodyOnFrame() {
        return BodyOnFrame;
    }

    public void setBodyOnFrame(String bodyOnFrame) {
        BodyOnFrame = bodyOnFrame;
    }

    public LocalDate getLastRevisionDate() {
        return lastRevisionDate;
    }

    public void setLastRevisionDate(LocalDate lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
    }

    public LocalDate getItvDate() {
        return ItvDate;
    }

    public void setItvDate(LocalDate itvDate) {
        ItvDate = itvDate;
    }

    public LocalDate getNextItvDate() {
        return nextItvDate;
    }

    public void setNextItvDate(LocalDate nextItvDate) {
        this.nextItvDate = nextItvDate;
    }

    public String getAirFilterReference() {
        return airFilterReference;
    }

    public void setAirFilterReference(String airFilterReference) {
        this.airFilterReference = airFilterReference;
    }

    public String getOilFilterReference() {
        return oilFilterReference;
    }

    public void setOilFilterReference(String oilFilterReference) {
        this.oilFilterReference = oilFilterReference;
    }

    public String getFuelFilter() {
        return fuelFilter;
    }

    public void setFuelFilter(String fuelFilter) {
        this.fuelFilter = fuelFilter;
    }

    public String getMotorOil() {
        return motorOil;
    }

    public void setMotorOil(String motorOil) {
        this.motorOil = motorOil;
    }
}
