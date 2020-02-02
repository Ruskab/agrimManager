package api.api_controllers;

import api.business_controllers.ClientBusinessController;
import api.business_controllers.InterventionBusinesssController;
import api.business_controllers.RepairingPackBusinessController;
import api.business_controllers.VehicleBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.ClientDto;
import api.dtos.InterventionDto;
import api.dtos.RepairingPackDto;
import api.dtos.VehicleDto;
import api.filters.Secured;
import client_beans.util.PropertyLoader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Api(value = DeleteDataApiController.DELETE_DATA)
@Path(DeleteDataApiController.DELETE_DATA)
public class DeleteDataApiController {

    public static final String DELETE_DATA = "/delete";
    public static final String ID = "/{id}";
    private static final Logger LOGGER = LogManager.getLogger(DeleteDataApiController.class);

    static {
        DaoFactory.setFactory(new DaoFactoryHibr());
    }

    private ClientBusinessController clientBusinessController = new ClientBusinessController();
    private VehicleBusinessController vehicleBusinessController = new VehicleBusinessController();
    private InterventionBusinesssController interventionBusinesssController = new InterventionBusinesssController();
    private RepairingPackBusinessController repairingPackBusinessController = new RepairingPackBusinessController();

    @DELETE
    @Secured
    @ApiOperation(value = "Dellete all")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAll() {
        if (new PropertyLoader().isProduction()){
            return Response.status(401).build();
        }
        List<RepairingPackDto> repairingPackDtos = repairingPackBusinessController.readAll();
        List<InterventionDto> interventionDtos = interventionBusinesssController.readAll();
        List<ClientDto> clientDtos = clientBusinessController.readAll();
        List<VehicleDto> vehicleDtos = vehicleBusinessController.readAll();
        interventionDtos.forEach(interventionDto -> interventionBusinesssController.delete(Integer.toString(interventionDto.getId())));
        repairingPackDtos.forEach(repairingPackDto -> repairingPackBusinessController.delete(repairingPackDto.getId()));
        vehicleDtos.forEach(vehicleDto -> vehicleBusinessController.delete(Integer.toString(vehicleDto.getId())));
        clientDtos.forEach(clientDto -> clientBusinessController.delete(Integer.toString(clientDto.getId())));
        return Response.status(204).build();
    }

}