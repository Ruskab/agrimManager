package api.api_controllers;

import api.business_controllers.InterventionBusinesssController;
import api.dtos.InterventionDto;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import api.filters.Secured;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = InterventionApiController.INTERVENTIONS)
@Path(InterventionApiController.INTERVENTIONS)
public class InterventionApiController {
    public static final String INTERVENTIONS = "/interventions";

    public static final String ID = "/{id}";

    private InterventionBusinesssController interventionBusinessController = new InterventionBusinesssController();

    private static final Logger LOGGER = LogManager.getLogger(InterventionApiController.class);


    @POST
    @Secured
    @ApiOperation(value = "Create new intervention")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@ApiParam(value = "Intervention", required = true) InterventionDto interventionDto) {
        this.validate(interventionDto, "interventionDto");
        this.validate(interventionDto.getInterventionType(), "InterventionType");
        return Response.status(201).entity(interventionBusinessController.create(interventionDto)).build();
    }

    @DELETE
    @Secured
    @ApiOperation(value = "Delete intervention by Id")
    @Path("{id}")
    public void delete(
            @ApiParam(value = "Intervention", required = true)
            @PathParam("id")
                String interventionId) {
        this.validate(interventionId, "intervention id");
        this.validateId(interventionId, "intenvention id");
        this.interventionBusinessController.delete(interventionId);
        Response.status(204).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getById(
            @ApiParam(value = "Intervention", required = true)
            @PathParam("id")
                    String interventionId) {
        this.validateId(interventionId, "Intervention id");
        return Response.ok().entity(this.interventionBusinessController.read(interventionId)).build();
    }

    @PUT
    @ApiOperation(value = "Update intervention information")
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response update(
            @ApiParam(value = "InterventionId", required = true)
            @PathParam("id")
                    String id,
            @ApiParam(value = "Intervention", required = true)
            InterventionDto interventionDto) {
        this.validateId(id, "intervention id: ");
        this.validate(interventionDto, "interventionDto");
        this.validate(interventionDto.getInterventionType(), "interventionDto interventionType");
        this.validate(interventionDto.getStartTime(), "interventionDto startType");
        LOGGER.info("ClienteDto valido");
        this.interventionBusinessController.update(id, interventionDto);
        return Response.status(200).build();
    }


    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<InterventionDto> listAll() {
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
