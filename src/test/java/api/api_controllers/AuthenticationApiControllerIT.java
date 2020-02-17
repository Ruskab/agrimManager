package api.api_controllers;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.dtos.CredentialsDto;
import api.dtos.MechanicDto;
import api.object_mothers.MechanicDtoMother;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class AuthenticationApiControllerIT {

    public static final String APP_BASE_URL = "app.url";
    private static final String API_PATH = "/api/v0";
    Client client;
    Properties properties;
    private String authToken;
    private MechanicApiController mechanicApiController = new MechanicApiController();

    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteClient();
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
    }

    @Test
    void authenticate_user() {
        CredentialsDto credentialsDto = new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD);

        Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + AuthenticationApiController.AUTH)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(credentialsDto, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(response.readEntity(String.class).isEmpty(), is(false));
    }

    @AfterEach
    void delete_mechanic() {
        client.target(properties.getProperty(APP_BASE_URL) + API_PATH + "/delete")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }

}