package api.entity.builder;

import api.entity.Client;
import api.entity.Vehicle;

import java.time.LocalDate;

public class VehicleBuilder {
    private String registrationPlate;
    private String brand;
    private Client client;
    private String kms;
    private String bodyOnFrame;
    private LocalDate lastRevisionDate;
    private LocalDate itvDate;
    private LocalDate nextItvDate;
    private String airFilterReference;
    private String oilFilterReference;
    private String fuelFilter;
    private String motorOil;

    public VehicleBuilder setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
        return this;
    }

    public VehicleBuilder setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public VehicleBuilder setClient(Client client) {
        this.client = client;
        return this;
    }

    public VehicleBuilder setKMS(String kms) {
        this.kms = kms;
        return this;
    }

    public VehicleBuilder setBodyOnFrame(String bodyOnFrame) {
        this.bodyOnFrame = bodyOnFrame;
        return this;
    }

    public VehicleBuilder setLastRevisionDate(LocalDate lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
        return this;
    }

    public VehicleBuilder setItvDate(LocalDate itvDate) {
        this.itvDate = itvDate;
        return this;
    }

    public VehicleBuilder setNextItvDate(LocalDate nextItvDate) {
        this.nextItvDate = nextItvDate;
        return this;
    }

    public VehicleBuilder setAirFilterReference(String airFilterReference) {
        this.airFilterReference = airFilterReference;
        return this;
    }

    public VehicleBuilder setOilFilterReference(String oilFilterReference) {
        this.oilFilterReference = oilFilterReference;
        return this;
    }

    public VehicleBuilder setFuelFilter(String fuelFilter) {
        this.fuelFilter = fuelFilter;
        return this;
    }

    public VehicleBuilder setMotorOil(String motorOil) {
        this.motorOil = motorOil;
        return this;
    }

    public Vehicle createVehicle() {
        return new Vehicle(registrationPlate, brand, client, kms, bodyOnFrame, lastRevisionDate, itvDate, nextItvDate, airFilterReference, oilFilterReference, fuelFilter, motorOil);
    }
}