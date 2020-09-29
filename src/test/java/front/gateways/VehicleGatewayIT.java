package front.gateways;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.api_controllers.AuthenticationApiController;
import api.api_controllers.MechanicApiController;
import api.dtos.CredentialsDto;
import api.object_mothers.FrontClientMother;
import api.object_mothers.MechanicDtoMother;
import front.dtos.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import java.time.LocalDate;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VehicleGatewayIT {

    private static final Logger LOGGER = LogManager.getLogger(VehicleGatewayIT.class);

    private Client client;
    private Properties properties;
    private String authToken;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private VehicleGateway vehicleGateway;
    private ClientGateway clientGateway;
    private OperationsGateway operationsGateway;

    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteRestClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD))
                .getEntity();
        clientGateway = new ClientGateway(authToken);
        vehicleGateway = new VehicleGateway(authToken);
        vehicleGateway = new VehicleGateway(authToken);
        operationsGateway = new OperationsGateway(authToken);
    }

    @Test
    void create_and_read_vehicle() {
        String clientId = clientGateway.create(FrontClientMother.client());
        Vehicle vehicle = createVehicleDto(clientId, "AABBDDCC");
        String vehicleId = vehicleGateway.create(vehicle);

        Vehicle createdVehicleDto = vehicleGateway.read(vehicleId);

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
        String clientId = clientGateway.create(FrontClientMother.client());
        Vehicle vehicle = createVehicleDto(clientId, "AABBDDCC");

        String vehicleId = vehicleGateway.create(vehicle);

        vehicleGateway.delete(Integer.parseInt(vehicleId));

        assertThrows(NotFoundException.class, () -> vehicleGateway.read(vehicleId));
    }

    @Test
    void update_vehicle() {
        String clientId = clientGateway.create(FrontClientMother.client());
        Vehicle vehicle = createVehicleDto(clientId, "AABBDDCC");
        String vehicleId = vehicleGateway.create(vehicle);
        Vehicle createdVehicleDto = vehicleGateway.read(vehicleId);
        createdVehicleDto.setRegistrationPlate("CCDDAABB");

        vehicleGateway.update(createdVehicleDto);

        Vehicle updatedVehicleDto = vehicleGateway.read(vehicleId);
        assertThat(updatedVehicleDto.getRegistrationPlate(), is("CCDDAABB"));
    }

    private Vehicle createVehicleDto(String clientId, String registrationPlate) {
        return Vehicle.builder()
                .registrationPlate(registrationPlate)
                .clientId(clientId)
                .brand("Opel")
                .kms("03-03-2017 94744")
                .bodyOnFrame("VF1KC0JEF31065732")
                .lastRevisionDate(LocalDate.now().minusMonths(2))
                .itvDate(LocalDate.now().minusMonths(3))
                .nextItvDate(LocalDate.now().plusYears(1))
                .airFilterReference("1813029400")
                .oilFilterReference("1812344000")
                .fuelFilter("181315400")
                .motorOil("5.5  5W30")
                .build();
    }

    @AfterEach
    void delete_data() {
        LOGGER.info("clean database after test");
        operationsGateway.deleteAll();
    }


}