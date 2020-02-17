package api.business_controllers;

import api.daos.DaoFactory;
import api.dtos.VehicleDto;
import api.entity.Client;
import api.entity.Vehicle;
import api.entity.builder.VehicleBuilder;
import api.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleBusinessController {

    private static final String VEHICLE_ID_MSG = "Vehicle id: ";

    public int create(VehicleDto vehicleDto) {
        Client client = null;
        if (vehicleDto.getClientId() != null) {
            client = DaoFactory.getFactory().getClientDao().read(Integer.parseInt(vehicleDto.getClientId()))
                    .orElseThrow(() -> NotFoundException.throwBecauseOf("Client not found"));
        }
        Vehicle vehicle = new VehicleBuilder(vehicleDto.getRegistrationPlate())
                .setBrand(vehicleDto.getBrand()).setClient(client).setKMS(vehicleDto.getKms())
                .setBodyOnFrame(vehicleDto.getBodyOnFrame()).setLastRevisionDate(vehicleDto.getLastRevisionDate())
                .setItvDate(vehicleDto.getItvDate()).setNextItvDate(vehicleDto.getNextItvDate())
                .setAirFilterReference(vehicleDto.getAirFilterReference()).setOilFilterReference(vehicleDto.getOilFilterReference())
                .setFuelFilter(vehicleDto.getFuelFilter()).setMotorOil(vehicleDto.getMotorOil()).createVehicle();

        DaoFactory.getFactory().getVehicleDao().create(vehicle);
        return vehicle.getId();
    }

    public void delete(String id) {
        Vehicle vehicle = DaoFactory.getFactory().getVehicleDao().read(Integer.parseInt(id))
                .orElseThrow(() -> NotFoundException.throwBecauseOf(VEHICLE_ID_MSG + id));

        DaoFactory.getFactory().getVehicleDao().deleteById(vehicle.getId());
    }

    public VehicleDto read(String id) {
        return DaoFactory.getFactory().getVehicleDao().read(Integer.parseInt(id)).map(VehicleDto::new)
                .orElseThrow(() -> NotFoundException.throwBecauseOf(VEHICLE_ID_MSG + id));
    }

    public List<VehicleDto> readAll() {
        return DaoFactory.getFactory().getVehicleDao().findAll().map(VehicleDto::new).collect(Collectors.toList());
    }

    public void update(String id, VehicleDto vehicleDto) {
        Vehicle vehicle = DaoFactory.getFactory().getVehicleDao().read((Integer.parseInt(id)))
                .orElseThrow(() -> NotFoundException.throwBecauseOf(VEHICLE_ID_MSG + id));

        vehicle.setRegistrationPlate(vehicleDto.getRegistrationPlate());
        vehicle.setBrand(vehicleDto.getBrand());
        vehicle.setKms(vehicleDto.getKms());
        vehicle.setBodyOnFrame(vehicleDto.getBodyOnFrame());
        vehicle.setLastRevisionDate(vehicleDto.getLastRevisionDate());
        vehicle.setItvDate(vehicleDto.getItvDate());
        vehicle.setNextItvDate(vehicleDto.getNextItvDate());
        vehicle.setAirFilterReference(vehicleDto.getAirFilterReference());
        vehicle.setOilFilterReference(vehicleDto.getOilFilterReference());
        vehicle.setFuelFilter(vehicleDto.getFuelFilter());
        vehicle.setMotorOil(vehicleDto.getMotorOil());

        vehicle.setClient(DaoFactory.getFactory().getClientDao().read((Integer.parseInt(vehicleDto.getClientId())))
                .orElse(vehicle.getClient()));
        DaoFactory.getFactory().getVehicleDao().update(vehicle);
    }
}
