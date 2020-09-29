package api.api_controllers;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.dtos.CredentialsDto;
import api.object_mothers.MechanicDtoMother;
import front.gateways.AuthenticationGateway;
import front.gateways.OperationsGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class AuthenticationApiControllerIT {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationApiControllerIT.class);

    Client client;
    Properties properties;
    private String authToken;
    private final MechanicApiController mechanicApiController = new MechanicApiController();
    private OperationsGateway operationsGateway;
    private AuthenticationGateway authenticationGateway;

    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteRestClient();
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        authenticationGateway = new AuthenticationGateway();
        operationsGateway = new OperationsGateway(authToken);
    }

    @Test
    void authenticate_user() {
        CredentialsDto credentialsDto = new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD);

        String token = authenticationGateway.authenticate(credentialsDto);

        assertThat(token.isEmpty(), is(false));
    }

    @AfterEach
    void delete_mechanic() {
        LOGGER.info("clean database after test");
        operationsGateway.deleteAll();
    }

}