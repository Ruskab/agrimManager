package api.api_controllers;

import api.AgrimDomainFactory;
import api.PropertiesResolver;
import api.RestClientLoader;
import api.dtos.ClientDto;
import api.dtos.CredentialsDto;
import api.dtos.InterventionDto;
import api.dtos.VehicleDto;
import api.entity.InterventionType;
import api.object_mothers.ClientDtoMother;
import api.object_mothers.InterventionDtoMother;
import api.object_mothers.MechanicDtoMother;
import client_beans.clients.ClientGateway;
import client_beans.interventions.InterventionGateway;
import client_beans.vehicles.VehicleGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class InterventionApiControllerIT {

    public static final String APP_BASE_URL = "app.url";
    private static final Logger LOGGER = LogManager.getLogger(InterventionApiControllerIT.class);
    private static final String API_PATH = "/api/v0";
    Client client;
    Properties properties;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;
    private ClientGateway clientGateway;
    private VehicleGateway vehicleGateway;
    private InterventionGateway interventionGateway;

    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        createAuthToken();
        clientGateway = new ClientGateway(authToken);
        vehicleGateway = new VehicleGateway(authToken);
        interventionGateway = new InterventionGateway(authToken);
    }

    private void createAuthToken() {
        LOGGER.info("creamos mecanico fake authorizado");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
    }

    @Test
    void create_and_read_intervention() {
        ClientDto clientDto = ClientDtoMother.clientDto();
        String clientId = clientGateway.create(clientDto);
        VehicleDto vehicleDto = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicleDto);
        InterventionDto interventionDto = InterventionDtoMother.withVehicle(vehicleId);

        String interventionId = interventionGateway.create(interventionDto);

        InterventionDto createdInterventionDto = interventionGateway.read(interventionId);
        assertThat(createdInterventionDto.getVehicleId(), is(vehicleId));
        assertThat(createdInterventionDto.getInterventionType(), is(InterventionType.REPAIR));
    }

    @Test
    void delete_intervention() {
        ClientDto clientDto = ClientDtoMother.clientDto();
        String clientId = clientGateway.create(clientDto);
        VehicleDto vehicleDto = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicleDto);
        InterventionDto interventionDto = InterventionDtoMother.withVehicle(vehicleId);
        String interventionId = interventionGateway.create(interventionDto);

        clientGateway.delete(Integer.parseInt(clientId));
    }

    @Test
    void update_intervention() {
        ClientDto clientDto = ClientDtoMother.clientDto();
        String clientId = clientGateway.create(clientDto);
        VehicleDto vehicleDto = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicleDto);
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
        client.target(properties.getProperty(APP_BASE_URL) + API_PATH + "/delete")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }

}