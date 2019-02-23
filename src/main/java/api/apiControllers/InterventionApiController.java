package api.apiControllers;

import api.businessControllers.InterventionBusinesssController;
import api.dtos.InterventionDto;
import api.entity.State;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.NotFoundException;
import com.mysql.cj.core.util.StringUtils;

public class InterventionApiController {
    public static final String INTERVENTIONS = "/interventions";

    public static final String ID = "/{id}";

    InterventionBusinesssController interventionBusinessController = new InterventionBusinesssController();

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

    private void validate(Object property, String message) {
        if (property == null) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }

    private void validateId(String id, String message) {
        if (!StringUtils.isStrictlyNumeric(id)) {
            throw new NotFoundException(message + " Should be numeric");
            //todo change to BadRequestException
        }
    }
}
