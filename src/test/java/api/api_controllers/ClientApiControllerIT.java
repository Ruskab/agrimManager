package api.api_controllers;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.dtos.ClientDto;
import api.dtos.CredentialsDto;
import api.object_mothers.ClientDtoMother;
import api.object_mothers.MechanicDtoMother;
import front.gateways.ClientGateway;
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

class ClientApiControllerIT {

    public static final String APP_BASE_URL = "app.url";
    private static final Logger LOGGER = LogManager.getLogger(ClientApiControllerIT.class);
    private static final String API_PATH = "/api/v0";
    Client client;
    Properties properties;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;
    private ClientGateway clientGateway;

    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteRestClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
        clientGateway = new ClientGateway(authToken);

    }

    @Test
    void create_and_read_clientDto() {
        String clientId = clientGateway.create(ClientDtoMother.clientDto());

        ClientDto createdClientDto = clientGateway.read(clientId);

        assertThat(createdClientDto.getFullName(), is(ClientDtoMother.FAKE_FULL_NAME));
        assertThat(createdClientDto.getHours(), is(ClientDtoMother.HOURS));
    }

    @Test
    void delete_client() {
        String clientId = clientGateway.create(ClientDtoMother.clientDto());

        clientGateway.delete(Integer.parseInt(clientId));

        assertThat(clientGateway.readAll().isEmpty(), is(true));
    }

    @Test
    void update_client() {
        ClientDto clientDto = ClientDtoMother.clientDto();
        String clientId = clientGateway.create(clientDto);
        ClientDto createdClientDto = clientGateway.read(clientId);
        createdClientDto.setFullName("newFullName");
        createdClientDto.setHours(5);

        clientGateway.update(createdClientDto);

        ClientDto updatedClientDto = clientGateway.read(clientId);
        assertThat(updatedClientDto.getFullName(), is("newFullName"));
        assertThat(updatedClientDto.getHours(), is(5));
    }

    @AfterEach
    void delete_data() {
        client.target(properties.getProperty(APP_BASE_URL) + API_PATH + "/delete")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }


}