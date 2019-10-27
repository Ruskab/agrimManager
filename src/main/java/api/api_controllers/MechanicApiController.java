package api.api_controllers;

import api.business_controllers.MechanicBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value="/mechanics")
@Path("mechanics")
public class MechanicApiController {
    public static final String MECHANICS = "/mechanics";

    public static final String ID = "/{id}";

    public static final String ID_INTERVENTIONS = ID + "/interventions";

    private MechanicBusinessController mechanicBusinesssController = new MechanicBusinessController();

    static {
        DaoFactory.setFactory(new DaoFactoryHibr());
    }

    @POST
    @ApiOperation(value = "Create new Mechanic")
    @Consumes(MediaType.APPLICATION_JSON)
    public int create(MechanicDto mechanicDto) {
        return this.mechanicBusinesssController.create(mechanicDto);
    }

    @POST
    @ApiOperation(value = "Create new Mechanic Intervention")
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/interventions")
    public void createIntervention(@PathParam("id") String mechanicId, InterventionDto interventionDto) {
        this.validate(interventionDto, "interventionDto");
        this.validate(interventionDto.getState(), "State");
        this.validate(interventionDto.getStartTime(), "Start Time");
        this.validateId(mechanicId, "Mechanic id");
        this.mechanicBusinesssController.createIntervention(mechanicId, interventionDto);
    }

    @GET
    @ApiOperation(value = "Get all mechanics")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MechanicDto> readAll() {
        return mechanicBusinesssController.readAll();
    }

    @GET
    @ApiOperation(value = "Get mechanic by ID")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public MechanicDto read(@PathParam("id") String id) {
        this.validateId(id, "mechanic id");
        return this.mechanicBusinesssController.read(id);
        //todo handle exceptions like not found
    }

    @DELETE
    @ApiOperation(value = "Delete mechanic by Id")
    @Path("{id}")
    public Response delete(@PathParam("id") String id) {
        this.validateId(id, "client id: ");
        this.mechanicBusinesssController.delete(id);
        return Response.status(204).build();
    }



    private void validate(Object property, String message) {
        if (property == null || property.toString().equals("")) {
            throw new FieldInvalidException(message + " is missing");
        }
    }

    private void validateId(String id, String message) {
        validate(id, message);
        if ( !StringUtils.isStrictlyNumeric(id)){
            throw new NotFoundException(message +  " Should be numeric");
        }
    }
}

