package api.api_controllers;

import api.business_controllers.ClientBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.dtos.ClientVehiclesDto;
import api.exceptions.ArgumentNotValidException;
import api.exceptions.NotFoundException;
import api.exceptions.RequestInvalidException;
import com.mysql.cj.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/clients")
public class ClientApiController {

    private static final Logger LOGGER = LogManager.getLogger(ClientApiController.class);
    public static final String CLIENTS = "/clients";


    public static final String ID = "/{id}";

    public static final String ID_VEHICLES = ID + "/vehicles";

    private ClientBusinessController clientBusinessController = new ClientBusinessController();

    static {
        DaoFactory.setFactory(new DaoFactoryHibr());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(ClientDto clientDto) {
        this.validate(clientDto, "clientDto");
        this.validate(clientDto.getFullName(), "clientDto FullName");
        LOGGER.info("ClienteDto valido");
        return Response.status(201).entity(clientBusinessController.create(clientDto)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClientDto> readAll() {
        return clientBusinessController.readAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public ClientDto read(@PathParam("id") String id) {
        this.validateId(id, "client id");
        return this.clientBusinessController.read(id);
        //todo handle exceptions like not found
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/vehicles/{id}")
    public ClientVehiclesDto clientVehiclesList(@PathParam("id") String clientId) {
        validateId(clientId, "client Id");
        return clientBusinessController.readClientVehicles(Integer.parseInt(clientId))
                .orElseThrow(() -> new NotFoundException("client with id: " + clientId));
    }

    @PUT
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
    @Path("{id}")
    public Response delete(@PathParam("id") String id) {
        this.validateId(id, "client id: ");
        this.clientBusinessController.delete(id);
        return Response.status(204).build();
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
