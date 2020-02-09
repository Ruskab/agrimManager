package api.api_controllers;

import api.AgrimDomainFactory;
import api.MechanicDtoMother;
import api.PropertiesResolver;
import api.business_controllers.ClientBusinessController;
import api.business_controllers.InterventionBusinesssController;
import api.business_controllers.RepairingPackBusinessController;
import api.business_controllers.VehicleBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Stream;

import static api.AgrimDomainFactory.createInterventionDto;
import static api.AgrimDomainFactory.createVehicle;
import static java.util.stream.Collectors.toList;
import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class DeleteDataApiControllerIT {

    public static final String APP_BASE_URL = "app.url";
    private static final String API_PATH = "/api/v0";
    private static ClientBusinessController clientBusinessController;
    private static VehicleBusinessController vehicleBusinessController;
    private static InterventionBusinesssController interventionBusinesssController;
    private static RepairingPackBusinessController repairingPackBusinessController;
    Client client;
    Properties properties;
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
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
        DaoFactory.setFactory(new DaoFactoryHibr());
    }


    @Test
    void deleteAll() {
        clientBusinessController = new ClientBusinessController();
        vehicleBusinessController = new VehicleBusinessController();
        interventionBusinesssController = new InterventionBusinesssController();
        repairingPackBusinessController = new RepairingPackBusinessController();
        List<Integer> clients;
        List<Integer> vehicles;
        List<Integer> repairingPacks;
        clients = Stream.generate(AgrimDomainFactory::createClientDto).limit(5).map(clientBusinessController::create).collect(toList());
        clients.stream().limit(5).map(clientId -> createVehicle(Integer.toString(clientId))).map(vehicleBusinessController::create).collect(toList());
        vehicles = clients.stream().limit(5).map(clientId -> createVehicle(Integer.toString(clientId))).map(vehicleBusinessController::create).collect(toList());
        Stream.generate(AgrimDomainFactory::createCaffeInterventionDto).limit(5).map(interventionBusinesssController::create).collect(toList());
        vehicles.stream().limit(5).map(vehicleId -> createInterventionDto(Integer.toString(vehicleId))).map(interventionBusinesssController::create).collect(toList());
        repairingPacks = Stream.generate(AgrimDomainFactory::createRepairingPackDto).limit(3)
                .map(repairingPackBusinessController::create).collect(toList());
        repairingPacks.stream().map(repairingPackId -> createInterventionDto(Integer.toString(vehicles.get(new Random().nextInt(vehicles.size())))))
                .map(interventionBusinesssController::create).collect(toList());

        Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + DeleteDataApiController.DELETE_DATA)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();

        assertThat(response.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
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