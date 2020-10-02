package api.api_controllers;

import api.business_controllers.ClientBusinessController;
import api.dtos.ClientDto;
import api.exceptions.FieldInvalidException;
import api.filters.Secured;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = ClientApiController.CLIENTS)
@Path(ClientApiController.CLIENTS)
public class ClientApiController {

    public static final String CLIENTS = "/clients";
    public static final String ID = "/{id}";
    private static final Logger LOGGER = LogManager.getLogger(ClientApiController.class);

    private ClientBusinessController clientBusinessController = new ClientBusinessController();

    @POST
    @Secured
    @ApiOperation(value = "Create new client")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(
            @ApiParam(value = "Client to create", required = true)
            @Valid ClientDto clientDto) {
        this.validate(clientDto, "clientDto");
        this.validate(clientDto.getFullName(), "clientDto FullName");
        LOGGER.info("ClienteDto valido");
        return Response.status(201).entity(clientBusinessController.create(clientDto)).build();
    }

    @GET
    @Secured
    @ApiOperation(value = "Get clients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll(
            @ApiParam(value = "search by username")
            @QueryParam("query")
                    String fullName) {
        if (fullName != null) {
            return Response.ok().entity(clientBusinessController.searchByFullName(fullName)).build();
        }
        return Response.ok().entity(clientBusinessController.readAll()).build();
    }

    @GET
    @Secured
    @ApiOperation(value = "Get client by ID")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getById(@ApiParam(value = "Get by id", required = true) @PathParam("id") String id) {
        this.validateId(id, "client id");
        return Response.ok().entity(this.clientBusinessController.read(id)).build();
    }

    @GET
    @Secured
    @ApiOperation(value = "Get client vehicles")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/vehicles")
    public Response readVehicles(
            @PathParam("id")
            @ApiParam(value = "Client ID", required = true)
                    String clientId) {
        return Response.ok().entity(clientBusinessController.searchClientVehicles(clientId)).build();
    }


    @PUT
    @Secured
    @ApiOperation(value = "Update client information")
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response update(
            @ApiParam(value = "client ID", required = true)
            @PathParam("id")
                    String id,
            @ApiParam(value = "updated client", required = true)
                    ClientDto clientDto) {
        this.validate(clientDto, "clientDto");
        this.validate(clientDto.getFullName(), "clientDto FullName");
        this.validateId(id, "client id: ");
        LOGGER.info("ClienteDto valido");
        this.clientBusinessController.update(id, clientDto);
        return Response.status(200).build();
    }

    @DELETE
    @Secured
    @ApiOperation(value = "Delete client by Id")
    @Path("{id}")
    public Response delete(
            @ApiParam(value = "Client Id", required = true)
            @PathParam("id")
                    String id) {
        this.validateId(id, "client id: ");
        this.clientBusinessController.delete(id);
        return Response.status(204).build();
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
