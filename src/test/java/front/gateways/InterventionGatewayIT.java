package front.gateways;

import front.AgrimDomainFactory;
import api.PropertiesResolver;
import api.RestClientLoader;
import api.api_controllers.AuthenticationApiController;
import api.api_controllers.MechanicApiController;
import api.dtos.CredentialsDto;
import api.dtos.InterventionDto;
import front.dtos.Vehicle;
import api.object_mothers.FrontClientMother;
import api.object_mothers.InterventionDtoMother;
import api.object_mothers.MechanicDtoMother;
import front.dtos.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.NotFoundException;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InterventionGatewayIT {

    private static final Logger LOGGER = LogManager.getLogger(InterventionGatewayIT.class);

    javax.ws.rs.client.Client client;
    Properties properties;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;
    private ClientGateway clientGateway;
    private VehicleGateway vehicleGateway;
    private InterventionGateway interventionGateway;
    private OperationsGateway operationsGateway;


    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteRestClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        createAuthToken();
        clientGateway = new ClientGateway(authToken);
        vehicleGateway = new VehicleGateway(authToken);
        interventionGateway = new InterventionGateway(authToken);
        operationsGateway = new OperationsGateway(authToken);
    }

    private void createAuthToken() {
        LOGGER.info("creamos mecanico fake authorizado");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD))
                .getEntity();
    }

    @Test
    void create_and_read_intervention() {
        Client clientDto = FrontClientMother.client();
        String clientId = clientGateway.create(clientDto);
        Vehicle vehicle = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicle);
        InterventionDto interventionDto = InterventionDtoMother.withVehicle(vehicleId);

        String interventionId = interventionGateway.create(interventionDto);

        InterventionDto createdInterventionDto = interventionGateway.read(interventionId);
        assertThat(createdInterventionDto.getVehicleId(), is(vehicleId));
        assertThat(createdInterventionDto.getInterventionType(), is("REPAIR"));
    }

    @Test
    void delete_intervention() {
        Client clientDto = FrontClientMother.client();
        String clientId = clientGateway.create(clientDto);
        Vehicle vehicle = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicle);
        InterventionDto interventionDto = InterventionDtoMother.withVehicle(vehicleId);
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
        InterventionDto interventionDto = InterventionDtoMother.withVehicle(vehicleId);
        String interventionId = interventionGateway.create(interventionDto);
        InterventionDto createdInterventionDto = interventionGateway.read(interventionId);
        createdInterventionDto.setTitle("new title");
        interventionGateway.update(createdInterventionDto);

        InterventionDto updatedInterventionDto = interventionGateway.read(interventionId);

        assertThat(updatedInterventionDto.getTitle(), is("new title"));
    }

    @AfterEach
    void delete_data() {
        LOGGER.info("clean database after test");
        operationsGateway.deleteAll();
    }

}