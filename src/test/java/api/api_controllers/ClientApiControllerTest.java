package api.api_controllers;

import api.dtos.ClientDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

import static org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

@Disabled("Falta configurar Travis para que arranque el servidor...")
class ClientApiControllerTest {

    Client client;

    @BeforeEach
    void setUp()
    {
        JacksonJsonProvider jsonProvider;
        //Create client
        // Create an ObjectMapper to be used for (de)serializing to/from JSON.
        ObjectMapper objectMapper = new ObjectMapper();
        // Register the JavaTimeModule for JSR-310 DateTime (de)serialization
        objectMapper.registerModule(new JavaTimeModule());
        // Configure the object mapper te serialize to timestamp strings.
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // Create a Jackson Provider
        jsonProvider = new JacksonJaxbJsonProvider(objectMapper, DEFAULT_ANNOTATIONS);
        client = ClientBuilder.newClient().register(jsonProvider);
    }

    @Test
    void create_and_read_clientDto() {
        ClientDto clientDto = new ClientDto("fullNameTest", 4);
         Response response = client.target("http://localhost:8080/agrimManager_war_exploded/api/clients")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(clientDto, MediaType.APPLICATION_JSON_TYPE));

         assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        String id = response.readEntity(String.class);

        ClientDto createdClientDto = client.target("http://localhost:8080/agrimManager_war_exploded/api/clients/" + id)
                .request(MediaType.APPLICATION_JSON)
                .get(ClientDto.class);

        assertThat(createdClientDto.getFullName(), is("fullNameTest"));
        assertThat(createdClientDto.getHours(), is(4));
    }

//    @Test
//    void delete_client() {
//        ClientDto clientDto = new ClientDto("fullNameTest", 4);
//        WebResource clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients");
//        ClientResponse response = clientsResourse.type("application/json").post(ClientResponse.class, clientDto);
//        String createdClient = response.getEntity(String.class);
//        clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients/" + createdClient);
//
//        response = clientsResourse.delete(ClientResponse.class);
//
//        assertThat(response.getStatus(), is(204));
//
//    }
//
//    @Test
//    void update_client() {
//        ClientDto clientDto = new ClientDto("fullNameTest", 4);
//        WebResource clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients");
//        ClientResponse response = clientsResourse.type("application/json").post(ClientResponse.class, clientDto);
//        String createdClient = response.getEntity(String.class);
//        clientsResourse = client.resource("http://localhost:8080/agrimManager_war_exploded/api/clients/" + createdClient);
//        clientDto.setFullName("newFullName");
//        clientDto.setHours(5);
//
//        response = clientsResourse.type("application/json").put(ClientResponse.class, clientDto);
//
//        assertThat(response.getStatus(), is(200));
//
//        //read client
//        response = clientsResourse.get(ClientResponse.class);
//        ClientDto updatedClientDto = response.getEntity(ClientDto.class);
//
//        assertThat(response.getStatus(), is(200));
//        assertThat(updatedClientDto.getFullName(), is("newFullName"));
//        assertThat(updatedClientDto.getHours(), is(5));
//    }
//
    @Test
    void update_vehicle() {
        VehicleDto vehicleDto = createVehicleDto("2214", "AABBCCDD");

        Response response = client.target("http://localhost:8080/agrimManager_war_exploded/api/vehicles")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(vehicleDto, MediaType.APPLICATION_JSON_TYPE));

        String id = response.getEntity().toString();

          VehicleDto clientDto1 = client.target("http://localhost:8080/agrimManager_war_exploded/api/vehicles/104")
                .request(MediaType.APPLICATION_JSON)
                .get(VehicleDto.class);

          assertThat(clientDto1.getRegistrationPlate(), is(vehicleDto.getRegistrationPlate()));
        assertThat(clientDto1.getBrand(), is(vehicleDto.getBrand()));
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