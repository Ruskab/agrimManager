package api.api_controllers;

import api.business_controllers.InterventionBusinesssController;
import api.dtos.InterventionDto;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.RequestInvalidException;
import com.mysql.cj.util.StringUtils;

import java.util.List;

public class InterventionApiController {
    public static final String INTERVENTIONS = "/interventions";

    public static final String ID = "/{id}";

    public static final String REPAIRING_PACK = "/repairing-pack";

    private InterventionBusinesssController interventionBusinessController = new InterventionBusinesssController();

    public int create(InterventionDto interventionDto) {
        this.validate(interventionDto, "interventionDto");
        this.validate(interventionDto.getState(), "State");
        return interventionBusinessController.create(interventionDto);
    }

    public void delete(String interventionId) {
        this.validate(interventionId, "intervention id");
        this.validateId(interventionId, "intenvention id");
        this.interventionBusinessController.delete(interventionId);
    }

    public InterventionDto read(String interventionId) {
        this.validateId(interventionId, "RepairingPack id");
        return this.interventionBusinessController.read(interventionId);
    }

    public List<InterventionDto> readAll() {
        return this.interventionBusinessController.readAll();
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }

    private void validateId(String id, String message) {
        if (!StringUtils.isStrictlyNumeric(id)) {
            throw new RequestInvalidException(message + " Should be numeric");
        }
    }

}
