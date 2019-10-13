package api.api_controllers;

import api.business_controllers.InterventionBusinesssController;
import api.dtos.InterventionDto;
import api.exceptions.FieldInvalidException;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value=InterventionApiController.INTERVENTIONS)
@Path(InterventionApiController.INTERVENTIONS)
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public InterventionDto read(@PathParam("id") String interventionId) {
        this.validateId(interventionId, "RepairingPack id");
        return this.interventionBusinessController.read(interventionId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<InterventionDto> readAll() {
        return this.interventionBusinessController.readAll();
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new FieldInvalidException(message + " is missing");
        }
    }


    private void validateId(String id, String message) {
        if (!StringUtils.isStrictlyNumeric(id)) {
            throw new FieldInvalidException(message + " Should be numeric");
        }
    }

}
