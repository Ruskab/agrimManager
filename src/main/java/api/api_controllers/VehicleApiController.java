package api.api_controllers;

import api.business_controllers.VehicleBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.VehicleDto;
import api.exceptions.ArgumentNotValidException;
import com.mysql.cj.core.util.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/vehicles")
public class VehicleApiController {

    public static final String VEHICLES = "/vehicles";

    public static final String ID_ID = "/{id}";

    static { DaoFactory.setFactory(new DaoFactoryHibr()); }

    private VehicleBusinessController vehicleBusinessController = new VehicleBusinessController();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(VehicleDto vehicleDto) {
        this.validate(vehicleDto, "vehicleDto");
        this.validate(vehicleDto.getRegistrationPlate(), "registration plate");
        this.validateId(vehicleDto.getClientId(), "Vehicle id");
        return Response.status(201).entity(vehicleBusinessController.create(vehicleDto)).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String id) {
        validateId(id,"Vehicle id");
        vehicleBusinessController.delete(id);
        return Response.status(204).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<VehicleDto> readAll() {
        return this.vehicleBusinessController.readAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public VehicleDto read(@PathParam("id") String id) {
        validateId(id, "vehicle id");
        return vehicleBusinessController.read(id);
    }

    private void validate(Object property, String message) {
        if (property == null) {
            throw new ArgumentNotValidException(message + " is missing");
        }
    }

    private void validateId(String id, String message) {
        validate(id, message);
        if ( !StringUtils.isStrictlyNumeric(id)){
            throw new ArgumentNotValidException(message +  " Should be numeric");
        }
    }
}
