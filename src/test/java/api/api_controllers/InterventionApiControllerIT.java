package api.api_controllers;

import api.AgrimDomainFactory;
import api.MechanicDtoMother;
import api.PropertiesResolver;
import api.dtos.ClientDto;
import api.dtos.CredentialsDto;
import api.dtos.InterventionDto;
import api.dtos.MechanicDto;
import api.dtos.VehicleDto;
import api.entity.InterventionType;
import client_beans.clients.ClientGateway;
import client_beans.interventions.InterventionGateway;
import client_beans.vehicles.VehicleGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

class InterventionApiControllerIT {

    public static final String APP_BASE_URL = "app.url";
    private static final Logger LOGGER = LogManager.getLogger(InterventionApiControllerIT.class);
    private static final String API_PATH = "/api/v0";
    Client client;
    Properties properties;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;
    private ClientGateway clientGateway;
    private VehicleGateway vehicleGateway;
    private InterventionGateway interventionGateway;

    @BeforeEach
    void setUp() {
        configureApiClient();
        createAuthToken();
        clientGateway = new ClientGateway(authToken);
        vehicleGateway = new VehicleGateway(authToken);
        interventionGateway = new InterventionGateway(authToken);
    }

    private void createAuthToken() {
        LOGGER.info("creamos mecanico fake authorizado");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
    }

    private void configureApiClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
    }

    @Test
    void create_and_read_intervention() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        String clientId = clientGateway.create(clientDto);
        VehicleDto vehicleDto = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicleDto);
        InterventionDto interventionDto = AgrimDomainFactory.createInterventionDto(vehicleId);

        String interventionId = interventionGateway.create(interventionDto);

        InterventionDto createdInterventionDto = interventionGateway.read(interventionId);
        assertThat(createdInterventionDto.getVehicleId(), is(vehicleId));
        assertThat(createdInterventionDto.getInterventionType(), is(InterventionType.REPAIR));
    }

    @Test
    void delete_intervention() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        String clientId = clientGateway.create(clientDto);
        VehicleDto vehicleDto = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicleDto);
        InterventionDto interventionDto = AgrimDomainFactory.createInterventionDto(vehicleId);
        String interventionId = interventionGateway.create(interventionDto);

        clientGateway.delete(Integer.parseInt(clientId));
    }

    @Test
    void update_intervention() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
        String clientId = clientGateway.create(clientDto);
        VehicleDto vehicleDto = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicleDto);
        InterventionDto interventionDto = AgrimDomainFactory.createInterventionDto(vehicleId);
        String interventionId = interventionGateway.create(interventionDto);
        InterventionDto createdInterventionDto = interventionGateway.read(interventionId);
        createdInterventionDto.setTitle("new title");
        interventionGateway.update(createdInterventionDto);

        InterventionDto updatedInterventionDto = interventionGateway.read(interventionId);

        assertThat(updatedInterventionDto.getTitle(), is("new title"));
    }

    @AfterEach
    void delete_data() {
        LOGGER.info("clean database after test");
        client.target(properties.getProperty(APP_BASE_URL) + API_PATH + "/delete")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }

}