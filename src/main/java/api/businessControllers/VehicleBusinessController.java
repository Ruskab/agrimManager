package api.businessControllers;

import api.daos.DaoFactory;
import api.dtos.VehicleDto;
import api.entity.Client;
import api.entity.Vehicle;
import api.exceptions.NotFoundException;

import java.util.Optional;

public class VehicleBusinessController {
    public VehicleDto create(VehicleDto vehicleDto) {
        Client client;
        if (vehicleDto.getClientId() != null){
            client = DaoFactory.getFactory().getClientDao().read(vehicleDto.getId())
                    .orElseThrow(() -> new NotFoundException("Client not found"));
        }

    }
}
