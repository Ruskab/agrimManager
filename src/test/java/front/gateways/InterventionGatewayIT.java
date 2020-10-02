package front.gateways;

import api.api_controllers.MechanicApiController;
import api.object_mothers.FrontClientMother;
import api.object_mothers.FrontInterventionMother;
import api.object_mothers.MechanicDtoMother;
import front.AgrimDomainFactory;
import front.dtos.Client;
import front.dtos.Intervention;
import front.dtos.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.NotFoundException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InterventionGatewayIT {

    private static final Logger LOGGER = LogManager.getLogger(InterventionGatewayIT.class);

    private final MechanicApiController mechanicApiController = new MechanicApiController();
    private final AuthenticationGateway authenticationGateway = new AuthenticationGateway();
    private String authToken;
    private ClientGateway clientGateway;
    private VehicleGateway vehicleGateway;
    private InterventionGateway interventionGateway;
    private OperationsGateway operationsGateway;


    @BeforeEach
    void setUp() {
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + authenticationGateway.authenticate(AgrimDomainFactory.fakeCredentials());
        clientGateway = new ClientGateway(authToken);
        vehicleGateway = new VehicleGateway(authToken);
        interventionGateway = new InterventionGateway(authToken);
        operationsGateway = new OperationsGateway(authToken);
    }

    @Test
    void create_and_read_intervention() {
        Client clientDto = FrontClientMother.client();
        String clientId = clientGateway.create(clientDto);
        Vehicle vehicle = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicle);
        Intervention interventionDto = FrontInterventionMother.withVehicle(vehicleId);

        String interventionId = interventionGateway.create(interventionDto);

        Intervention createdInterventionDto = interventionGateway.read(interventionId);
        assertThat(createdInterventionDto.getVehicleId(), is(vehicleId));
        assertThat(createdInterventionDto.getInterventionType(), is("REPAIR"));
    }

    @Test
    void delete_intervention() {
        Client clientDto = FrontClientMother.client();
        String clientId = clientGateway.create(clientDto);
        Vehicle vehicle = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicle);
        Intervention interventionDto = FrontInterventionMother.withVehicle(vehicleId);
        String interventionId = interventionGateway.create(interventionDto);

        interventionGateway.delete(Integer.parseInt(interventionId));

        assertThrows(NotFoundException.class, () -> interventionGateway.read(interventionId));
    }

    @Test
    void update_intervention() {
        Client clientDto = FrontClientMother.client();
        String clientId = clientGateway.create(clientDto);
        Vehicle vehicle = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicle);
        Intervention interventionDto = FrontInterventionMother.withVehicle(vehicleId);
        String interventionId = interventionGateway.create(interventionDto);
        Intervention createdIntervention = interventionGateway.read(interventionId);
        createdIntervention.setTitle("new title");
        interventionGateway.update(createdIntervention);

        Intervention updatedIntervention = interventionGateway.read(interventionId);

        assertThat(updatedIntervention.getTitle(), is("new title"));
    }

    @AfterEach
    void delete_data() {
        LOGGER.info("clean database after test");
        operationsGateway.deleteAll();
    }

}