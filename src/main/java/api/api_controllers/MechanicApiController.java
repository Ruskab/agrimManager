package api.api_controllers;

import api.business_controllers.MechanicBusinessController;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.exception.ArgumentNotValidException;
import api.exception.NotFoundException;
import com.mysql.cj.util.StringUtils;

public class MechanicApiController {
    public static final String MECHANICS = "/mechanics";

    public static final String ID = "/{id}";

    public static final String ID_INTERVENTIONS = ID + "/interventions";

    private MechanicBusinessController mechanicBusinesssController = new MechanicBusinessController();

    public int create(MechanicDto mechanicDto) {
        return this.mechanicBusinesssController.create(mechanicDto);
    }

    public void createIntervention(String mechanicId, InterventionDto interventionDto) {
        this.validate(interventionDto, "interventionDto");
        this.validate(interventionDto.getState(), "State");
        this.validate(interventionDto.getPeriod(), "Period");
        this.validateId(mechanicId, "Mechanic id");
        this.mechanicBusinesssController.createIntervention(mechanicId, interventionDto);
    }

    private void validate(Object property, String message) {
        if (property == null || property.toString().equals("")) {
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
