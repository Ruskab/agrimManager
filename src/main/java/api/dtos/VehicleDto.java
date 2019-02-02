package api.dtos;

import java.time.LocalDate;

public class VehicleDto {

    private int id;
    private String registrationPlate;
    private String brand;
    private String clientId;
    private String KMS;
    private String BodyOnFrame; //bastidor
    private LocalDate lastRevisionDate;
    private LocalDate ItvDate;
    private LocalDate nextItvDate;
    private String airFilterReference;
    private String oilFilterReference;
    private String fuelFilter;
    private String motorOil;

    public VehicleDto(int id, String registrationPlate, String brand, String clientId, String KMS, String bodyOnFrame, LocalDate lastRevisionDate, LocalDate itvDate, LocalDate nextItvDate, String airFilterReference, String oilFilterReference, String fuelFilter, String motorOil) {
        this.id = id;
        this.registrationPlate = registrationPlate;
        this.brand = brand;
        this.clientId = clientId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "VehicleDto{" +
                "id=" + id +
                ", registrationPlate='" + registrationPlate + '\'' +
                ", brand='" + brand + '\'' +
                ", clientId='" + clientId + '\'' +
                ", KMS='" + KMS + '\'' +
                ", BodyOnFrame='" + BodyOnFrame + '\'' +
                ", lastRevisionDate=" + lastRevisionDate +
                ", ItvDate=" + ItvDate +
                ", nextItvDate=" + nextItvDate +
                ", airFilterReference='" + airFilterReference + '\'' +
                ", oilFilterReference='" + oilFilterReference + '\'' +
                ", fuelFilter='" + fuelFilter + '\'' +
                ", motorOil='" + motorOil + '\'' +
                '}';
    }
}
