package api.api_controllers;

import api.business_controllers.ClientBusinessController;
import api.business_controllers.InterventionBusinesssController;
import api.business_controllers.MechanicBusinessController;
import api.business_controllers.RepairingPackBusinessController;
import api.business_controllers.VehicleBusinessController;
import api.dtos.ClientDto;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.RepairingPackDto;
import api.dtos.VehicleDto;
import api.filters.Secured;
import front.util.PropertyLoader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.time.Instant;
import java.util.List;


@Api(value = DeleteDataApiController.DELETE_DATA)
@Path(DeleteDataApiController.DELETE_DATA)
public class DeleteDataApiController {

    public static final String DELETE_DATA = "/delete";
    public static final String ID = "/{id}";
    private static final Logger LOGGER = LogManager.getLogger(DeleteDataApiController.class);

    private MechanicBusinessController mechanicBusinessController = new MechanicBusinessController();
    private ClientBusinessController clientBusinessController = new ClientBusinessController();
    private VehicleBusinessController vehicleBusinessController = new VehicleBusinessController();
    private InterventionBusinesssController interventionBusinesssController = new InterventionBusinesssController();
    private RepairingPackBusinessController repairingPackBusinessController = new RepairingPackBusinessController();

    @DELETE
    @Secured
    @ApiOperation(value = "Dellete all")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAll() {
        Instant start = Instant.now();

        if (new PropertyLoader().isProduction()){
            return Response.status(401).build();
        }
        List<MechanicDto> mechanicDtos = mechanicBusinessController.readAll();
        List<RepairingPackDto> repairingPackDtos = repairingPackBusinessController.readAll();
        List<InterventionDto> interventionDtos = interventionBusinesssController.readAll();
        List<ClientDto> clientDtos = clientBusinessController.readAll();
        List<VehicleDto> vehicleDtos = vehicleBusinessController.readAll();
        mechanicDtos.forEach(mechanicDto -> mechanicBusinessController.delete(Integer.toString(mechanicDto.getId())));
        interventionDtos.forEach(interventionDto -> interventionBusinesssController.delete(Integer.toString(interventionDto.getId())));
        repairingPackDtos.forEach(repairingPackDto -> repairingPackBusinessController.delete(repairingPackDto.getId()));
        vehicleDtos.forEach(vehicleDto -> vehicleBusinessController.delete(Integer.toString(vehicleDto.getId())));
        clientDtos.forEach(clientDto -> clientBusinessController.delete(Integer.toString(clientDto.getId())));
        LOGGER.info("All data deleted in: {} ms]", Duration.between(start, Instant.now()).toMillis());
        return Response.status(204).build();
    }

}
