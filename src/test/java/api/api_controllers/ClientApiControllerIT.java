package api.api_controllers;

import api.dtos.ClientDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class ClientApiControllerIT {

    private static final Logger LOGGER = LogManager.getLogger(ClientApiControllerIT.class);
    Client client;
    Properties properties;
    public static final String APP_BASE_URL = "app.url";
    private static final String API_PATH = "/api/v0";

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = this.loadPropertiesFile("config.properties");
    }

    @Test
    void create_and_read_clientDto() {

        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        LOGGER.info(" peticion a {}", properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS);
        Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        String id = response.readEntity(String.class);

        ClientDto createdClientDto = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(ClientDto.class);

        assertThat(createdClientDto.getFullName(), is("fullNameTest"));
        assertThat(createdClientDto.getHours(), is(4));
    }

    @Test
    void delete_client() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));
        String id = response.readEntity(String.class);

        Response deleteResponse = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertThat(deleteResponse.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
    }

    @Test
    void update_client() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);

        Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        String id = response.readEntity(String.class);

        ClientDto createdClientDto = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(ClientDto.class);

        createdClientDto.setFullName("newFullName");
        createdClientDto.setHours(5);

        Response updateResponse = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(createdClientDto, MediaType.APPLICATION_JSON_TYPE));

        assertThat(updateResponse.getStatus(), is(Response.Status.OK.getStatusCode()));

        ClientDto updatedClientDto = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + ClientApiController.CLIENTS + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(ClientDto.class);

        assertThat(updateResponse.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(updatedClientDto.getFullName(), is("newFullName"));
        assertThat(updatedClientDto.getHours(), is(5));
    }

    //todo mover a clase comun se usa en muchos lados
    private Properties loadPropertiesFile(String filePath) {

        Properties prop = new Properties();

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            System.err.println("Unable to load properties file : " + filePath);
        }
        return prop;
    }

}