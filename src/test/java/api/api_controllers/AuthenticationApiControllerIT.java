package api.api_controllers;

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

class AuthenticationApiControllerIT {

    public static final String APP_BASE_URL = "app.url";
    private static final Logger LOGGER = LogManager.getLogger(AuthenticationApiControllerIT.class);
    private static final String API_PATH = "/api/v0";
    Client client;
    Properties properties;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private List<Integer> createdMechanics = new ArrayList<>();

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
    void authenticate_user() {
        MechanicDto mechanicDto = new MechanicDto();
        mechanicDto.setName("mechanicName");
        mechanicDto.setPassword("mechanicPass");
        createdMechanics.add(mechanicApiController.create(mechanicDto));
        CredentialsDto credentialsDto = new CredentialsDto(mechanicDto.getName(), mechanicDto.getPassword());

        Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + AuthenticationApiController.AUTH)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(credentialsDto, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(response.readEntity(String.class).isEmpty(), is(false));
    }

    @AfterEach
    void delete_mechanic() {
        createdMechanics.forEach(mechanic -> mechanicApiController.delete(mechanic.toString()));
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