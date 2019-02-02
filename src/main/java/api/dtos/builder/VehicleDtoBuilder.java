package api.dtos.builder;

import api.dtos.VehicleDto;

import java.time.LocalDate;

public class VehicleDtoBuilder {
    private int id;
    private String registrationPlate;
    private String brand;
    private String clientId;
    private String kms;
    private String bodyOnFrame;
    private LocalDate lastRevisionDate;
    private LocalDate itvDate;
    private LocalDate nextItvDate;
    private String airFilterReference;
    private String oilFilterReference;
    private String fuelFilter;
    private String motorOil;

    public VehicleDtoBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public VehicleDtoBuilder setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
        return this;
    }

    public VehicleDtoBuilder setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public VehicleDtoBuilder setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public VehicleDtoBuilder setKMS(String kms) {
        this.kms = kms;
        return this;
    }

    public VehicleDtoBuilder setBodyOnFrame(String bodyOnFrame) {
        this.bodyOnFrame = bodyOnFrame;
        return this;
    }

    public VehicleDtoBuilder setLastRevisionDate(LocalDate lastRevisionDate) {
        this.lastRevisionDate = lastRevisionDate;
        return this;
    }

    public VehicleDtoBuilder setItvDate(LocalDate itvDate) {
        this.itvDate = itvDate;
        return this;
    }

    public VehicleDtoBuilder setNextItvDate(LocalDate nextItvDate) {
        this.nextItvDate = nextItvDate;
        return this;
    }

    public VehicleDtoBuilder setAirFilterReference(String airFilterReference) {
        this.airFilterReference = airFilterReference;
        return this;
    }

    public VehicleDtoBuilder setOilFilterReference(String oilFilterReference) {
        this.oilFilterReference = oilFilterReference;
        return this;
    }

    public VehicleDtoBuilder setFuelFilter(String fuelFilter) {
        this.fuelFilter = fuelFilter;
        return this;
    }

    public VehicleDtoBuilder setMotorOil(String motorOil) {
        this.motorOil = motorOil;
        return this;
    }

    public VehicleDto createVehicleDto() {
        return new VehicleDto(id, registrationPlate, brand, clientId, kms, bodyOnFrame, lastRevisionDate, itvDate, nextItvDate, airFilterReference, oilFilterReference, fuelFilter, motorOil);
    }
}