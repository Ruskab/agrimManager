package api.entity.builder;

import api.entity.Client;
import api.entity.Vehicle;

import java.time.LocalDate;

public class VehicleBuilder {

    public static final String REGISTRATION_PLATE = "111111";

    private Vehicle vehicle;

    public VehicleBuilder(String registrationPlate) {
        this.vehicle = new Vehicle(registrationPlate);
    }

    public VehicleBuilder setRegistrationPlate(String registrationPlate) {
        this.vehicle.setRegistrationPlate(registrationPlate);
        return this;
    }

    public VehicleBuilder setBrand(String brand) {
        this.vehicle.setBrand(brand);
        return this;
    }

    public VehicleBuilder setClient(Client client) {
        this.vehicle.setClient(client);
        return this;
    }

    public VehicleBuilder setKMS(String kms) {
        this.vehicle.setKms(kms);
        return this;
    }

    public VehicleBuilder setBodyOnFrame(String bodyOnFrame) {
        this.vehicle.setBodyOnFrame(bodyOnFrame);
        return this;
    }

    public VehicleBuilder setLastRevisionDate(LocalDate lastRevisionDate) {
        this.vehicle.setLastRevisionDate(lastRevisionDate);
        return this;
    }

    public VehicleBuilder setItvDate(LocalDate itvDate) {
        this.vehicle.setItvDate(itvDate);
        return this;
    }

    public VehicleBuilder setNextItvDate(LocalDate nextItvDate) {
        this.vehicle.setNextItvDate(nextItvDate);
        return this;
    }

    public VehicleBuilder setAirFilterReference(String airFilterReference) {
        this.vehicle.setAirFilterReference(airFilterReference);
        return this;
    }

    public VehicleBuilder setOilFilterReference(String oilFilterReference) {
        this.vehicle.setOilFilterReference(oilFilterReference);
        return this;
    }

    public VehicleBuilder setFuelFilter(String fuelFilter) {
        this.vehicle.setFuelFilter(fuelFilter);
        return this;
    }

    public VehicleBuilder setMotorOil(String motorOil) {
        this.vehicle.setMotorOil(motorOil);
        return this;
    }

    public VehicleBuilder byDefault() {
        VehicleBuilder vehicleBuilder = new VehicleBuilder("AA1234BB");
        return vehicleBuilder
                .setBrand("Opel")
                .setKMS("03-03-2017 94744")
                .setBodyOnFrame("VF1KC0JEF31065732")
                .setLastRevisionDate(LocalDate.now().minusMonths(2))
                .setItvDate(LocalDate.now().minusMonths(3))
                .setNextItvDate(LocalDate.now().plusYears(1))
                .setAirFilterReference("1813029400")
                .setOilFilterReference("1812344000")
                .setFuelFilter("181315400")
                .setMotorOil("5.5 5W30");
    }

    public Vehicle createVehicle() {
        return this.vehicle;
    }


}