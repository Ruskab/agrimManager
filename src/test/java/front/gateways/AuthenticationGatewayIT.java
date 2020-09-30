package front.gateways;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.api_controllers.AuthenticationApiController;
import api.api_controllers.MechanicApiController;
import api.dtos.CredentialsDto;
import api.object_mothers.MechanicDtoMother;
import front.dtos.Credentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class AuthenticationGatewayIT {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationGatewayIT.class);
    private final MechanicApiController mechanicApiController = new MechanicApiController();
    Client client;
    Properties properties;
    private String authToken;
    private OperationsGateway operationsGateway;
    private AuthenticationGateway authenticationGateway;

    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteRestClient();
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD))
                .getEntity();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        authenticationGateway = new AuthenticationGateway();
        operationsGateway = new OperationsGateway(authToken);
    }

    @Test
    void authenticate_user() {
        Credentials credentialsDto = new Credentials(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD);

        String token = authenticationGateway.authenticate(credentialsDto);

        assertThat(token.isEmpty(), is(false));
    }

    @AfterEach
    void delete_mechanic() {
        LOGGER.info("clean database after test");
        operationsGateway.deleteAll();
    }

}