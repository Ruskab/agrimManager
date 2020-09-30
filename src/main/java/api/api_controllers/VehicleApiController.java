package api.api_controllers;

import api.business_controllers.VehicleBusinessController;
import api.dtos.VehicleDto;
import api.exceptions.FieldInvalidException;
import api.filters.Secured;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

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
import java.util.List;

@Api(value = VehicleApiController.VEHICLES)
@Path(VehicleApiController.VEHICLES)
public class VehicleApiController {

    public static final String VEHICLES = "/vehicles";

    public static final String ID_ID = "/{id}";

    private VehicleBusinessController vehicleBusinessController = new VehicleBusinessController();

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@ApiParam(value = "Vehicle", required = true)VehicleDto vehicleDto) {
        this.validate(vehicleDto, "vehicleDto");
        this.validate(vehicleDto.getRegistrationPlate(), "registration plate");
        this.validateId(vehicleDto.getClientId(), "Vehicle id");
        return Response.status(201).entity(vehicleBusinessController.create(vehicleDto)).build();
    }

    @DELETE
    @Secured
    @Path("{id}")
    public Response delete(
            @ApiParam(value = "Vehicle ID", required = true)
            @PathParam("id")
                    String id) {
        validateId(id, "Vehicle id");
        vehicleBusinessController.delete(id);
        return Response.status(204).build();
    }

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<VehicleDto> listAll(
                @ApiParam(value = "Search by vehicle datasheet")
                @QueryParam("query")
                        String query) {
        if (query != null) {
            return this.vehicleBusinessController.searchBy(query);
        }
        return this.vehicleBusinessController.readAll();
    }

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public VehicleDto getById(@PathParam("id") String id) {
        validateId(id, "vehicle id");
        return vehicleBusinessController.read(id);
    }

    @PUT
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response update(@PathParam("id") String id, VehicleDto vehicleDto) {
        this.validate(vehicleDto, "vehicleDto");
        this.validate(vehicleDto.getRegistrationPlate(), "vehicle Registration plate");
        this.validate(vehicleDto.getBrand(), "vehicle Brand");
        this.validate(vehicleDto.getKms(), "vehicle KMS");
        this.validate(vehicleDto.getBodyOnFrame(), "vehicle BodyOnFrame");
        this.validate(vehicleDto.getLastRevisionDate(), "vehicle LastRevisionDate");
        this.validate(vehicleDto.getItvDate(), "vehicle ItvDate");
        this.validate(vehicleDto.getNextItvDate(), "vehicle NextItvDate");
        this.validate(vehicleDto.getAirFilterReference(), "vehicle AirFilterReference");
        this.validate(vehicleDto.getOilFilterReference(), "vehicle OilFilterReference");
        this.validate(vehicleDto.getFuelFilter(), "vehicle FuelFilter");
        this.validate(vehicleDto.getMotorOil(), "vehicle MotorOil");
        this.validateId(id, "Vehicle id: ");
        this.vehicleBusinessController.update(id, vehicleDto);
        return Response.status(200).build();
    }


    private void validate(Object property, String message) {
        if (property == null) {
            throw new FieldInvalidException(message + " is missing");
        }
    }

    private void validateId(String id, String message) {
        validate(id, message);
        if (!StringUtils.isStrictlyNumeric(id)) {
            throw new FieldInvalidException(message + " Should be numeric");
        }
    }
}
