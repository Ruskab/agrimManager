package api;

import api.business_controllers.VehicleBusinessController;
import api.dtos.VehicleDto;
import api.exceptions.ArgumentNotValidException;
import com.mysql.cj.core.util.StringUtils;

import java.util.List;

public class VehicleApiController {

    static final String VEHICLES = "/vehicles";

    static final String ID_ID = "/{id}";

    VehicleBusinessController vehicleBusinessController = new VehicleBusinessController();

    public Object create(VehicleDto vehicleDto) {
        this.validate(vehicleDto, "vehicleDto");
        this.validate(vehicleDto.getRegistrationPlate(), "registration plate");
        this.validateId(vehicleDto.getClientId(), "Vehicle id");
        return vehicleBusinessController.create(vehicleDto);
    }

    void delete(String id) {
        validateId(id,"Vehicle id");
        vehicleBusinessController.delete(id);
    }

    VehicleDto read(String id) {
        validateId(id, "vehicle id");
        return vehicleBusinessController.read(id);
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }

    private void validateId(String id, String message) {
        validate(id, message);
        if ( !StringUtils.isStrictlyNumeric(id)){
            throw new ArgumentNotValidException(message +  " Should be numeric");
        }
    }

    public List<VehicleDto> readAll() {
        return this.vehicleBusinessController.readAll();
    }
}
