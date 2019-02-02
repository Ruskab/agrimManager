package api.businessControllers;

import api.daos.DaoFactory;
import api.dtos.VehicleDto;
import api.entity.Client;
import api.entity.Vehicle;
import api.entity.builder.VehicleBuilder;
import api.exceptions.NotFoundException;

public class VehicleBusinessController {
    public int create(VehicleDto vehicleDto) {
        Client client = null;
        if (vehicleDto.getClientId() != null) {
            client = DaoFactory.getFactory().getClientDao().read(vehicleDto.getId())
                    .orElseThrow(() -> new NotFoundException("Client not found"));
        }
        Vehicle vehicle = new VehicleBuilder().setRegistrationPlate(vehicleDto.getRegistrationPlate())
                .setBrand(vehicleDto.getBrand()).setClient(client).setKMS(vehicleDto.getKMS())
                .setBodyOnFrame(vehicleDto.getBodyOnFrame()).setLastRevisionDate(vehicleDto.getLastRevisionDate())
                .setItvDate(vehicleDto.getItvDate()).setNextItvDate(vehicleDto.getNextItvDate())
                .setAirFilterReference(vehicleDto.getAirFilterReference()).setOilFilterReference(vehicleDto.getOilFilterReference())
                .setFuelFilter(vehicleDto.getFuelFilter()).setMotorOil(vehicleDto.getMotorOil()).createVehicle();

        DaoFactory.getFactory().getVehicleDao().create(vehicle);
        return vehicle.getId();
    }
}
