package api.api_controllers;

import api.AgrimDomainFactory;
import api.PropertiesResolver;
import api.RestClientLoader;
import api.dtos.ClientDto;
import api.dtos.CredentialsDto;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.VehicleDto;
import api.entity.InterventionType;
import api.object_mothers.ClientDtoMother;
import api.object_mothers.InterventionDtoMother;
import api.object_mothers.MechanicDtoMother;
import front.gateways.ClientGateway;
import front.gateways.InterventionGateway;
import front.gateways.MechanicGateway;
import front.gateways.VehicleGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MechanicApiControllerIT {

    public static final String APP_BASE_URL = "app.url";
    private static final Logger LOGGER = LogManager.getLogger(MechanicApiControllerIT.class);
    private static final String API_PATH = "/api/v0";
    Client client;
    Properties properties;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;
    private Integer mechanicId;
    private ClientGateway clientGateway;
    private VehicleGateway vehicleGateway;
    private InterventionGateway interventionGateway;
    private MechanicGateway mechanicGateway;

    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteRestClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        createAuthToken();
        clientGateway = new ClientGateway(authToken);
        vehicleGateway = new VehicleGateway(authToken);
        interventionGateway = new InterventionGateway(authToken);
        mechanicGateway = new MechanicGateway(authToken);
    }

    @Test
    void create_and_read_mechanic() {
        MechanicDto mechanic = MechanicDtoMother.mechanicDto();

        String mechanicId = mechanicGateway.create(mechanic);

        assertThat(mechanicId, is(notNullValue()));
    }

    @Test
    void add_intervention_to_mechanic() {
        ClientDto clientDto = ClientDtoMother.clientDto();
        String clientId = clientGateway.create(clientDto);
        VehicleDto vehicleDto = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicleDto);
        InterventionDto interventionDto = InterventionDtoMother.withVehicle(vehicleId);
        MechanicDto mechanic = MechanicDtoMother.mechanicDto();
        mechanic.setId(mechanicId);

        mechanicGateway.createIntervention(mechanic ,interventionDto);

        MechanicDto updatedMechanic = mechanicGateway.read(Integer.toString(mechanicId));
        assertThat(updatedMechanic.getInterventionIds().isEmpty(), is(false));

    }

    @AfterEach
    void delete_data() {
        LOGGER.info("clean database after test");
        client.target(properties.getProperty(APP_BASE_URL) + API_PATH + "/delete")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }


    private void createAuthToken() {
        LOGGER.info("creamos mecanico fake authorizado");
        mechanicId = (Integer) mechanicApiController.create(MechanicDtoMother.mechanicDto()).getEntity();
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
    }
}