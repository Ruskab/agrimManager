package api.api_controllers;

import api.dtos.ClientDto;
import api.dtos.InterventionDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import api.entity.State;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class VehicleApiControllerTest {

    Client client;
    Properties properties;
    Integer createdClientId;
    private static final String API_PATH = "app.api.base.path";
    public static final String APP_BASE_URL = "app.url";

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonJsonProvider jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
        properties = this.loadPropertiesFile("config.properties");
        Response response = new ClientApiController().create(new ClientDto("fake", 3));
        createdClientId = (Integer) response.getEntity();

    }

    @Test
    void create_and_read_vehicle() {
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "AABBDDCC");

        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        String id = response.readEntity(String.class);

        VehicleDto createdVehicleDto = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(VehicleDto.class);

        assertThat(createdVehicleDto.getRegistrationPlate(), is("AABBDDCC"));
        assertThat(createdVehicleDto.getClientId(), is(createdClientId.toString()));
        assertThat(createdVehicleDto.getBrand(), is("Opel"));
        assertThat(createdVehicleDto.getKms(), is("03-03-2017 94744"));
        assertThat(createdVehicleDto.getBodyOnFrame(), is("VF1KC0JEF31065732"));
        assertThat(createdVehicleDto.getLastRevisionDate(), is(LocalDate.now().minusMonths(2)));
        assertThat(createdVehicleDto.getItvDate(), is(LocalDate.now().minusMonths(3)));
        assertThat(createdVehicleDto.getNextItvDate(), is(LocalDate.now().plusYears(1)));
        assertThat(createdVehicleDto.getAirFilterReference(), is("1813029400"));
        assertThat(createdVehicleDto.getOilFilterReference(), is("1812344000"));
        assertThat(createdVehicleDto.getFuelFilter(), is("181315400"));
        assertThat(createdVehicleDto.getMotorOil(), is("5.5  5W30"));
    }

    @Test
    void create_and_read_mechanic_intervention() {
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "AABBDDCC");

        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        String id = response.readEntity(String.class);
        InterventionDto interventionDto = createInterventionDto(id);

        Response response1 = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + MechanicApiController.MECHANICS + "/122/interventions")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(interventionDto, MediaType.APPLICATION_JSON_TYPE));


        VehicleDto createdVehicleDto = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(VehicleDto.class);

        assertThat(createdVehicleDto.getRegistrationPlate(), is("AABBDDCC"));
        assertThat(createdVehicleDto.getClientId(), is(createdClientId.toString()));
        assertThat(createdVehicleDto.getBrand(), is("Opel"));
        assertThat(createdVehicleDto.getKms(), is("03-03-2017 94744"));
        assertThat(createdVehicleDto.getBodyOnFrame(), is("VF1KC0JEF31065732"));
        assertThat(createdVehicleDto.getLastRevisionDate(), is(LocalDate.now().minusMonths(2)));
        assertThat(createdVehicleDto.getItvDate(), is(LocalDate.now().minusMonths(3)));
        assertThat(createdVehicleDto.getNextItvDate(), is(LocalDate.now().plusYears(1)));
        assertThat(createdVehicleDto.getAirFilterReference(), is("1813029400"));
        assertThat(createdVehicleDto.getOilFilterReference(), is("1812344000"));
        assertThat(createdVehicleDto.getFuelFilter(), is("181315400"));
        assertThat(createdVehicleDto.getMotorOil(), is("5.5  5W30"));
    }

    private InterventionDto createInterventionDto(String vehicleId) {
        return new InterventionDto("Reparacion", State.REPAIR, vehicleId, null,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
    }

    @Test
    void delete_client() {
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "AABBDDCC");

        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));
        String id = response.readEntity(String.class);

        Response deleteResponse = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertThat(deleteResponse.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
    }

    @Test
    void update_client() {
        VehicleDto vehicleDto = createVehicleDto(Integer.toString(createdClientId), "AABBDDCC");

        Response response = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));

        String id = response.readEntity(String.class);

        VehicleDto createdVehicleDto = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(VehicleDto.class);

        createdVehicleDto.setRegistrationPlate("CCDDAABB");

        Response updateResponse = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(createdVehicleDto, MediaType.APPLICATION_JSON_TYPE));

        assertThat(updateResponse.getStatus(), is(Response.Status.OK.getStatusCode()));

        VehicleDto updatedVehicleDto = client.target(properties.getProperty(APP_BASE_URL) + properties.getProperty(API_PATH) + VehicleApiController.VEHICLES + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(VehicleDto.class);

        assertThat(updateResponse.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(updatedVehicleDto.getRegistrationPlate(), is("CCDDAABB"));
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

    private VehicleDto createVehicleDto(String clientId, String registrationPlate) {
        return new VehicleDtoBuilder()
                .setRegistrationPlate(registrationPlate)
                .setClientId(clientId)
                .setBrand("Opel")
                .setKMS("03-03-2017 94744")
                .setBodyOnFrame("VF1KC0JEF31065732")
                .setLastRevisionDate(LocalDate.now().minusMonths(2))
                .setItvDate(LocalDate.now().minusMonths(3))
                .setNextItvDate(LocalDate.now().plusYears(1))
                .setAirFilterReference("1813029400")
                .setOilFilterReference("1812344000")
                .setFuelFilter("181315400")
                .setMotorOil("5.5  5W30")
                .createVehicleDto();
    }


}