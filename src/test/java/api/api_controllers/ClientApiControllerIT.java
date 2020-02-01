package api.api_controllers;

import api.PropertiesResolver;
import api.dtos.ClientDto;
import api.dtos.CredentialsDto;
import api.dtos.MechanicDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class ClientApiControllerIT {

    public static final String APP_BASE_URL = "app.url";
    private static final Logger LOGGER = LogManager.getLogger(ClientApiControllerIT.class);
    private static final String API_PATH = "/api/v0";
    Client client;
    Properties properties;
    private List<Integer> createdMechanics = new ArrayList<>();
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        MechanicDto mechanicDto = new MechanicDto();
        mechanicDto.setName("mechanicName");
        mechanicDto.setPassword("mechanicPass");
        createdMechanics.add(mechanicApiController.create(mechanicDto));
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(mechanicDto.getName(), mechanicDto.getPassword())).getEntity();
    }

    @Test
    void create_and_read_clientDto() {
        try {
            ClientDto clientDto = new ClientDto("fullNameTest", 4);
            Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS)
                    .request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, authToken)
                    .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));

            assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

            String id = response.readEntity(String.class);
            ClientDto createdClientDto = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS + "/" + id)
                    .request(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, authToken)
                    .get(ClientDto.class);

            assertThat(createdClientDto.getFullName(), is("fullNameTest"));
            assertThat(createdClientDto.getHours(), is(4));
        } catch (Exception e) {
            LOGGER.error(e);
            LOGGER.error(e.getMessage());
        }
    }

    @Test
    void delete_client() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));
        String id = response.readEntity(String.class);

        Response deleteResponse = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();

        assertThat(deleteResponse.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
    }

    @Test
    void update_client() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);

        Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        String id = response.readEntity(String.class);

        ClientDto createdClientDto = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(ClientDto.class);

        createdClientDto.setFullName("newFullName");
        createdClientDto.setHours(5);

        Response updateResponse = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .put(Entity.entity(createdClientDto, MediaType.APPLICATION_JSON_TYPE));

        assertThat(updateResponse.getStatus(), is(Response.Status.OK.getStatusCode()));

        ClientDto updatedClientDto = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .get(ClientDto.class);

        assertThat(updateResponse.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(updatedClientDto.getFullName(), is("newFullName"));
        assertThat(updatedClientDto.getHours(), is(5));
    }

    @AfterEach
    void delete_mechanic() {
        createdMechanics.forEach(mechanic -> mechanicApiController.delete(mechanic.toString()));
    }

}