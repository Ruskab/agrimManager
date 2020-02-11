package api.api_controllers;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.dtos.CredentialsDto;
import api.dtos.VehicleDto;
import api.dtos.builder.VehicleDtoBuilder;
import api.object_mothers.ClientDtoMother;
import api.object_mothers.MechanicDtoMother;
import client_beans.clients.ClientGateway;
import client_beans.vehicles.VehicleGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class VehicleApiControllerIT {

    private static final String API_PATH = "/api/v0";
    private static final String APP_BASE_URL = "app.url";
    private Client client;
    private Properties properties;
    private String authToken;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private VehicleGateway vehicleGateway;
    private ClientGateway clientGateway;

    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
        clientGateway = new ClientGateway(authToken);
        vehicleGateway = new VehicleGateway(authToken);
    }

    @Test
    void create_and_read_vehicle() {
        String clientId = clientGateway.create(ClientDtoMother.clientDto());
        VehicleDto vehicleDto = createVehicleDto(clientId, "AABBDDCC");
        String vehicleId = vehicleGateway.create(vehicleDto);

        VehicleDto createdVehicleDto = vehicleGateway.read(vehicleId);

        assertThat(createdVehicleDto.getRegistrationPlate(), is("AABBDDCC"));
        assertThat(createdVehicleDto.getClientId(), is(clientId));
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
    void delete_vehicle() {
        String clientId = clientGateway.create(ClientDtoMother.clientDto());
        VehicleDto vehicleDto = createVehicleDto(clientId, "AABBDDCC");

        String vehicleId = vehicleGateway.create(vehicleDto);

        vehicleGateway.delete(Integer.parseInt(vehicleId));
    }

    @Test
    void update_vehicle() {
        String clientId = clientGateway.create(ClientDtoMother.clientDto());
        VehicleDto vehicleDto = createVehicleDto(clientId, "AABBDDCC");
        String vehicleId = vehicleGateway.create(vehicleDto);
        VehicleDto createdVehicleDto = vehicleGateway.read(vehicleId);
        createdVehicleDto.setRegistrationPlate("CCDDAABB");

        vehicleGateway.update(createdVehicleDto);

        VehicleDto updatedVehicleDto = vehicleGateway.read(vehicleId);
        assertThat(updatedVehicleDto.getRegistrationPlate(), is("CCDDAABB"));
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

    @AfterEach
    void delete_data() {
        client.target(properties.getProperty(APP_BASE_URL) + API_PATH + "/delete")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }


}