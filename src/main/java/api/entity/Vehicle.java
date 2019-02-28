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

    private String kms;
    private String bodyOnFrame; //bastidor
    private LocalDate lastRevisionDate;
    private LocalDate itvDate;
    private LocalDate nextItvDate;
    private String airFilterReference;
    private String oilFilterReference;
    private String fuelFilter;
    private String motorOil;

    public Vehicle() {
        //JPA
    }

    public Vehicle(String registrationPlate, String brand, Client client, String kms, String bodyOnFrame, LocalDate lastRevisionDate, LocalDate itvDate, LocalDate nextItvDate, String airFilterReference, String oilFilterReference, String fuelFilter, String motorOil) {
        this.registrationPlate = registrationPlate;
        this.brand = brand;
        this.client = client;
        this.kms = kms;
        this.bodyOnFrame = bodyOnFrame;
        this.lastRevisionDate = lastRevisionDate;
        this.itvDate = itvDate;
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

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public String getBodyOnFrame() {
        return bodyOnFrame;
    }

    public void setBodyOnFrame(String bodyOnFrame) {
        this.bodyOnFrame = bodyOnFrame;
    }

    public LocalDate getLastRevisionDate() {
        return lastRevisionDate;
    }

    public void setLastRevisionDate(LocalDate lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
    }

    public LocalDate getItvDate() {
        return itvDate;
    }

    public void setItvDate(LocalDate itvDate) {
        this.itvDate = itvDate;
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
