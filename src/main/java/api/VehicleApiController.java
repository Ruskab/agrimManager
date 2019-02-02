package api;

import api.businessControllers.VehicleBusinessController;
import api.dtos.VehicleDto;
import api.exceptions.ArgumentNotValidException;

public class VehicleApiController {

    public static final String VEHICLES = "/vehicles";

    public static final String ID_ID = "/{id}";

    VehicleBusinessController vehicleBusinessController = new VehicleBusinessController();

    public Object create(VehicleDto vehicleDto) {
        this.validate(vehicleDto, "vehicleDto");
        this.validate(vehicleDto.getRegistrationPlate(), "registration plate");
        this.validate(vehicleDto.getClientId(), "Client id");
        return vehicleBusinessController.create(vehicleDto);
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }
}
