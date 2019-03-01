package api.dtos.builder;

import api.dtos.VehicleDto;

import java.time.LocalDate;

public class VehicleDtoBuilder {

    VehicleDto vehicleDto;

    public VehicleDtoBuilder (){
        this.vehicleDto = new VehicleDto();
    }

    public VehicleDtoBuilder setId(int id) {
        this.vehicleDto.setId(id);
        return this;
    }

    public VehicleDtoBuilder setRegistrationPlate(String registrationPlate) {
        this.vehicleDto.setRegistrationPlate(registrationPlate);
        return this;
    }

    public VehicleDtoBuilder setBrand(String brand) {
        this.vehicleDto.setBrand(brand);
        return this;
    }

    public VehicleDtoBuilder setClientId(String clientId) {
        this.vehicleDto.setClientId(clientId);
        return this;
    }

    public VehicleDtoBuilder setKMS(String kms) {
        this.vehicleDto.setKms(kms);
        return this;
    }

    public VehicleDtoBuilder setBodyOnFrame(String bodyOnFrame) {
        this.vehicleDto.setBodyOnFrame(bodyOnFrame);
        return this;
    }

    public VehicleDtoBuilder setLastRevisionDate(LocalDate lastRevisionDate) {
        this.vehicleDto.setLastRevisionDate(lastRevisionDate);
        return this;
    }

    public VehicleDtoBuilder setItvDate(LocalDate itvDate) {
        this.vehicleDto.setItvDate(itvDate);
        return this;
    }

    public VehicleDtoBuilder setNextItvDate(LocalDate nextItvDate) {
        this.vehicleDto.setNextItvDate(nextItvDate);
        return this;
    }

    public VehicleDtoBuilder setAirFilterReference(String airFilterReference) {
        this.vehicleDto.setAirFilterReference(airFilterReference);
        return this;
    }

    public VehicleDtoBuilder setOilFilterReference(String oilFilterReference) {
        this.vehicleDto.setOilFilterReference(oilFilterReference);
        return this;
    }

    public VehicleDtoBuilder setFuelFilter(String fuelFilter) {
        this.vehicleDto.setFuelFilter(fuelFilter);
        return this;
    }

    public VehicleDtoBuilder setMotorOil(String motorOil) {
        this.vehicleDto.setMotorOil(motorOil);
        return this;
    }

    public VehicleDtoBuilder byDefault() {
        VehicleDtoBuilder vehicleDtoBuilder = new VehicleDtoBuilder();
        return vehicleDtoBuilder
                .setId(2)
                .setClientId("4")
                .setBrand("Opel")
                .setKMS("03-03-2017 94744")
                .setBodyOnFrame("VF1KC0JEF31065732")
                .setLastRevisionDate(LocalDate.now().minusMonths(2))
                .setItvDate(LocalDate.now().minusMonths(3))
                .setNextItvDate(LocalDate.now().plusYears(1))
                .setAirFilterReference("1813029400")
                .setOilFilterReference("1812344000")
                .setFuelFilter("181315400")
                .setMotorOil("5.5  5W30");
    }

    public VehicleDto createVehicleDto() {
        return this.vehicleDto;
    }

}