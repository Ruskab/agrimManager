package front.gateways;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.api_controllers.AuthenticationApiController;
import api.api_controllers.MechanicApiController;
import api.dtos.CredentialsDto;
import api.object_mothers.FrontClientMother;
import api.object_mothers.MechanicDtoMother;
import front.dtos.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class ClientGatewayIT {

    private static final Logger LOGGER = LogManager.getLogger(ClientGatewayIT.class);

    javax.ws.rs.client.Client client;
    Properties properties;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;
    private ClientGateway clientGateway;
    private OperationsGateway operationsGateway;

    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteRestClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD))
                .getEntity();
        clientGateway = new ClientGateway(authToken);
        operationsGateway = new OperationsGateway(authToken);

    }

    @Test
    void create_and_read_clientDto() {
        String clientId = clientGateway.create(FrontClientMother.client());

        Client createdClientDto = clientGateway.read(clientId);

        assertThat(createdClientDto.getFullName(), is(FrontClientMother.FAKE_FULL_NAME));
        assertThat(createdClientDto.getHours(), is(FrontClientMother.HOURS));
    }

    @Test
    void delete_client() {
        String clientId = clientGateway.create(FrontClientMother.client());

        clientGateway.delete(Integer.parseInt(clientId));

        assertThat(clientGateway.readAll().isEmpty(), is(true));
    }

    @Test
    void update_client() {
        Client clientDto = FrontClientMother.client();
        String clientId = clientGateway.create(clientDto);
        Client createdClient = clientGateway.read(clientId);
        createdClient.setFullName("newFullName");
        createdClient.setHours(5);

        clientGateway.update(createdClient);

        Client updatedClientDto = clientGateway.read(clientId);
        assertThat(updatedClientDto.getFullName(), is("newFullName"));
        assertThat(updatedClientDto.getHours(), is(5));
    }

    @AfterEach
    void delete_data() {
        LOGGER.info("clean database after test");
        operationsGateway.deleteAll();
    }


}