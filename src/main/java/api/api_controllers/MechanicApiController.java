package api.api_controllers;

import api.business_controllers.MechanicBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import api.filters.Secured;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = MechanicApiController.MECHANICS)
@Path(MechanicApiController.MECHANICS)
public class MechanicApiController {
    public static final String MECHANICS = "/mechanics";

    public static final String ID = "/{id}";

    public static final String ID_INTERVENTIONS = ID + "/interventions";

    static {
        DaoFactory.setFactory(new DaoFactoryHibr());
    }

    private MechanicBusinessController mechanicBusinesssController = new MechanicBusinessController();

    @POST
    @Secured
    @ApiOperation(value = "Create new Mechanic")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(MechanicDto mechanicDto) {
        this.validate(mechanicDto, "mechanicDto");
        this.validate(mechanicDto.getName(), "mechanicDto Name");
        this.validate(mechanicDto.getPassword(), "mechanicDto Password");
        return Response.status(201).entity(mechanicBusinesssController.create(mechanicDto)).build();
    }

    @POST
    @Secured
    @ApiOperation(value = "Create new Mechanic Intervention")
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/interventions")
    public void createIntervention(@PathParam("id") String mechanicId, InterventionDto interventionDto) {
        this.validate(interventionDto, "interventionDto");
        this.validate(interventionDto.getInterventionType(), "State");
        this.validate(interventionDto.getStartTime(), "Start Time");
        this.validateId(mechanicId, "Mechanic id");
        this.mechanicBusinesssController.createIntervention(mechanicId, interventionDto);
    }

    @GET
    @Secured
    @ApiOperation(value = "Get all mechanics")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MechanicDto> readAll() {
        return mechanicBusinesssController.readAll();
    }

    @GET
    @Secured
    @ApiOperation(value = "Get mechanic by ID")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public MechanicDto read(@PathParam("id") String id) {
        this.validateId(id, "mechanic id");
        return this.mechanicBusinesssController.read(id);
        //todo handle exceptions like not found
    }

    @DELETE
    @Secured
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
        if (!StringUtils.isStrictlyNumeric(id)) {
            throw new NotFoundException(message + " Should be numeric");
        }
    }
}

