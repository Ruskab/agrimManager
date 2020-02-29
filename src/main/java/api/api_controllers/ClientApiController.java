package api.api_controllers;

import api.business_controllers.ClientBusinessController;
import api.dtos.ClientDto;
import api.dtos.ClientVehiclesDto;
import api.exceptions.FieldInvalidException;
import api.exceptions.NotFoundException;
import api.filters.Secured;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(value = ClientApiController.CLIENTS)
@Path(ClientApiController.CLIENTS)
public class ClientApiController {

    public static final String CLIENTS = "/clients";
    public static final String ID = "/{id}";
    public static final String ID_VEHICLES = ID + "/vehicles";
    private static final Logger LOGGER = LogManager.getLogger(ClientApiController.class);

    private ClientBusinessController clientBusinessController = new ClientBusinessController();

    @POST
    @Secured
    @ApiOperation(value = "Create new client")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(ClientDto clientDto) {
        this.validate(clientDto, "clientDto");
        this.validate(clientDto.getFullName(), "clientDto FullName");
        LOGGER.info("ClienteDto valido");
        return Response.status(201).entity(clientBusinessController.create(clientDto)).build();
    }

    @GET
    @Secured
    @ApiOperation(value = "Get all clients")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClientDto> readAll() {
        return clientBusinessController.readAll();
    }

    @GET
    @Secured
    @ApiOperation(value = "Get client by ID")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public ClientDto read(@PathParam("id") String id) {
        this.validateId(id, "client id");
        return this.clientBusinessController.read(id);
        //todo handle exceptions like not found
    }


    @GET
    @Secured
    @ApiOperation(value = "Get all client vehicles ")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/vehicles/{id}")
    public ClientVehiclesDto clientVehiclesList(@PathParam("id") String clientId) {
        validateId(clientId, "client Id");
        return clientBusinessController.readClientVehicles(Integer.parseInt(clientId))
                .orElseThrow(() -> NotFoundException.throwBecauseOf("client with id: " + clientId));
    }

    @PUT
    @Secured
    @ApiOperation(value = "Update client information")
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response update(@PathParam("id") String id, ClientDto clientDto) {
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
    public Response delete(@PathParam("id") String id) {
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
