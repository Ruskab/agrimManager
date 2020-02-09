package api.api_controllers;

import api.MechanicDtoMother;
import api.PropertiesResolver;
import api.dtos.ClientDto;
import api.dtos.CredentialsDto;
import api.dtos.MechanicDto;
import client_beans.clients.ClientGateway;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
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
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;
    private ClientGateway clientGateway;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
        clientGateway = new ClientGateway(authToken);

    }

    @Test
    void create_and_read_clientDto() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        String clientId = clientGateway.create(clientDto);

        ClientDto createdClientDto = clientGateway.read(clientId);

        assertThat(createdClientDto.getFullName(), is("fullNameTest"));
        assertThat(createdClientDto.getHours(), is(4));
    }

    @Test
    void delete_client() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        String clientId = clientGateway.create(clientDto);

        clientGateway.delete(Integer.parseInt(clientId));
    }

    @Test
    void update_client() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
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
//        client.target(properties.getProperty(APP_BASE_URL) + API_PATH + "/delete")
//                .request(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, authToken)
//                .delete();
        new DeleteDataApiController().deleteAll();
    }


}