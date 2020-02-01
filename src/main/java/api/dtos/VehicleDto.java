package api.dtos;

import api.entity.Vehicle;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDate;

public class VehicleDto implements Serializable {

    private int id;
    private String registrationPlate;
    private String brand;
    private String clientId;
    private String kms;
    private String bodyOnFrame; //bastidor
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastRevisionDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate itvDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate nextItvDate;
    private String airFilterReference;
    private String oilFilterReference;
    private String fuelFilter;
    private String motorOil;

    public VehicleDto() {
    }

    public VehicleDto(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.registrationPlate = vehicle.getRegistrationPlate();
        this.brand = vehicle.getBrand();
        this.clientId = Integer.toString(vehicle.getClient().getId());
        this.kms = vehicle.getKms();
        this.bodyOnFrame = vehicle.getBodyOnFrame();
        this.lastRevisionDate = vehicle.getLastRevisionDate();
        this.itvDate = vehicle.getItvDate();
        this.nextItvDate = vehicle.getNextItvDate();
        this.airFilterReference = vehicle.getAirFilterReference();
        this.oilFilterReference = vehicle.getOilFilterReference();
        this.fuelFilter = vehicle.getFuelFilter();
        this.motorOil = vehicle.getMotorOil();
    }

    public VehicleDto(String registrationPlate, String clientId) {
        this.registrationPlate = registrationPlate;
        this.clientId = clientId;
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

    @Transient
    public String getVehicleDataSheet() {
        return String.format("%s %s %s", registrationPlate, brand, bodyOnFrame);
    }

    @Override
    public String toString() {
        return "VehicleDto{" +
                "id=" + id +
                ", registrationPlate='" + registrationPlate + '\'' +
                ", brand='" + brand + '\'' +
                ", clientId='" + clientId + '\'' +
                ", kms='" + kms + '\'' +
                ", BodyOnFrame='" + bodyOnFrame + '\'' +
                ", lastRevisionDate=" + lastRevisionDate +
                ", ItvDate=" + itvDate +
                ", nextItvDate=" + nextItvDate +
                ", airFilterReference='" + airFilterReference + '\'' +
                ", oilFilterReference='" + oilFilterReference + '\'' +
                ", fuelFilter='" + fuelFilter + '\'' +
                ", motorOil='" + motorOil + '\'' +
                '}';
    }
}
