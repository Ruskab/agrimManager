package api.api_controllers;

import api.business_controllers.MechanicBusinessController;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import api.filters.Secured;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = MechanicApiController.MECHANICS)
@Path(MechanicApiController.MECHANICS)
public class MechanicApiController {
    public static final String MECHANICS = "/mechanics";

    public static final String ID = "/{id}";

    private final MechanicBusinessController mechanicBusinessController = new MechanicBusinessController();

    @POST
    @Secured
    @ApiOperation(value = "Create new Mechanic")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@ApiParam(value = "Mechanic", required = true) MechanicDto mechanicDto) {
        this.validate(mechanicDto, "mechanicDto");
        this.validate(mechanicDto.getName(), "mechanicDto Name");
        this.validate(mechanicDto.getPassword(), "mechanicDto Password");
        return Response.status(201).entity(mechanicBusinessController.create(mechanicDto)).build();
    }

    @POST
    @Secured
    @ApiOperation(value = "Create new Mechanic Intervention")
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/interventions")
    public Response createIntervention(
            @ApiParam(value = "Mechanic ID", required = true)
            @PathParam("id")
                    String mechanicId,
            @ApiParam(value = "Intervention", required = true)
                    InterventionDto interventionDto) {
        this.validate(interventionDto, "interventionDto");
        this.validate(interventionDto.getInterventionType(), "State");
        this.validate(interventionDto.getStartTime(), "Start Time");
        this.validateId(mechanicId, "Mechanic id");
        this.mechanicBusinessController.createIntervention(mechanicId, interventionDto);
        return Response.status(201).build();
    }

    @GET
    @Secured
    @ApiOperation(value = "Get mechanic by ID")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getById(
            @ApiParam(value = "Mechanic ID", required = true)
            @PathParam("id")
                    String mechanicId) {
        this.validateId(mechanicId, "mechanic id");
        try {
            return Response.ok().entity(this.mechanicBusinessController.read(mechanicId)).build();
        } catch (NotFoundException e) {
            return Response.status(404).build();
        }
    }

    @GET
    @Secured
    @ApiOperation(value = "Get mechanics")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MechanicDto> listAll(
            @ApiParam(value = "Mechanic username")
            @QueryParam("username")
                    String username) {
        if (username != null) {
            return mechanicBusinessController.searchBy(username);
        }
        return mechanicBusinessController.readAll();
    }

    @GET
    @Secured
    @ApiOperation(value = "Get mechanic interventions")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/interventions")
    public List<InterventionDto> readInterventions(
            @PathParam("id")
            @ApiParam(value = "Mechanic ID", required = true)
                    Integer id,
            @ApiParam(value = "Filter by active interventions", type = "Boolean")
            @QueryParam("active")
                    Boolean active) {
        return mechanicBusinessController.getMechanicInterventions(id, active);
    }

    @POST
    @ApiOperation(value = "Finish active information")
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/interventions/{interventionId}/finish")
    public Response finishIntervention(
            @ApiParam(value = "Mechanic ID", required = true)
            @PathParam("id")
                    String mechanicId,
            @ApiParam(value = "Active intervention ID", required = true)
            @PathParam("interventionId")
                    String interventionId) {
        this.validateId(mechanicId, "intervention id: ");
        this.validateId(interventionId, "intervention id: ");
        this.mechanicBusinessController.finishIntervention(Integer.parseInt(mechanicId), interventionId);
        return Response.ok().build();
    }


    @DELETE
    @Secured
    @ApiOperation(value = "Delete mechanic by Id")
    @Path("{id}")
    public Response delete(
            @ApiParam(value = "Mechanic ID", required = true)
            @PathParam("id")
                    String mechanicId) {
        this.validateId(mechanicId, "client id: ");
        this.mechanicBusinessController.delete(mechanicId);
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
            throw NotFoundException.throwBecauseOf(message + " Should be numeric");
        }
    }
}

