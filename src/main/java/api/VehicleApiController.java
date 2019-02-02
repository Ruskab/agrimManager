package api;

import api.businessControllers.VehicleBusinessController;
import api.dtos.VehicleDto;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.NotFoundException;
import com.mysql.cj.core.util.StringUtils;

public class VehicleApiController {

    public static final String VEHICLES = "/vehicles";

    public static final String ID_ID = "/{id}";

    VehicleBusinessController vehicleBusinessController = new VehicleBusinessController();

    public Object create(VehicleDto vehicleDto) {
        this.validate(vehicleDto, "vehicleDto");
        this.validate(vehicleDto.getRegistrationPlate(), "registration plate");
        this.validateId(vehicleDto.getClientId(), "Vehicle id");
        return vehicleBusinessController.create(vehicleDto);
    }

    public void delete(String id) {
        validateId(id,"Vehicle id");
        vehicleBusinessController.delete(id);
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }

    private void validateId(String id, String message) {
        validate(id, message);
        if ( !StringUtils.isStrictlyNumeric(id)){
            throw new NotFoundException(message +  " Should be numeric");
        }
    }
}
