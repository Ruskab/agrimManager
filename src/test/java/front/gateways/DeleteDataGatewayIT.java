package front.gateways;

import front.AgrimDomainFactory;
import api.PropertiesResolver;
import api.RestClientLoader;
import api.api_controllers.AuthenticationApiController;
import api.api_controllers.DeleteDataApiController;
import api.api_controllers.MechanicApiController;
import api.business_controllers.ClientBusinessController;
import api.business_controllers.InterventionBusinesssController;
import api.business_controllers.RepairingPackBusinessController;
import api.business_controllers.VehicleBusinessController;
import api.daos.DaoFactory;
import api.daos.hibernate.DaoFactoryHibr;
import api.dtos.CredentialsDto;
import api.object_mothers.ClientDtoMother;
import api.object_mothers.InterventionDtoMother;
import api.object_mothers.MechanicDtoMother;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Stream;

import static front.AgrimDomainFactory.createVehicle;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class DeleteDataGatewayIT {

    public static final String APP_BASE_URL = "app.url";
    private static final String API_PATH = "/api/v0";
    private static ClientBusinessController clientBusinessController;
    private static VehicleBusinessController vehicleBusinessController;
    private static InterventionBusinesssController interventionBusinesssController;
    private static RepairingPackBusinessController repairingPackBusinessController;
    private final MechanicApiController mechanicApiController = new MechanicApiController();
    Client client;
    Properties properties;
    private OperationsGateway operationsGateway;
    private VehicleGateway vehicleGateway;

    private String authToken;


    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteRestClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD))
                .getEntity();
        DaoFactory.setFactory(new DaoFactoryHibr());
        operationsGateway = new OperationsGateway(authToken);
        vehicleGateway = new VehicleGateway(authToken);
    }


    @Test
    void deleteAll() {
        clientBusinessController = new ClientBusinessController();
        vehicleBusinessController = new VehicleBusinessController();
        interventionBusinesssController = new InterventionBusinesssController();
        repairingPackBusinessController = new RepairingPackBusinessController();
        List<Integer> clients = generateClients();
        List<Integer> vehicles = generateVehicles(clients);
        List<Integer> repairingPacks = generateRepairingPacks();
        generateMoreVehicles(clients);
        generateCafeInterventions();
        generateRepairInterventions(vehicles);
        generateInterventionsWithRepairingPack(vehicles, repairingPacks);

        Response response = client.target(properties.getProperty(APP_BASE_URL) + API_PATH + DeleteDataApiController.DELETE_DATA)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();

        assertThat(response.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
    }

    private void generateInterventionsWithRepairingPack(List<Integer> vehicles, List<Integer> repairingPacks) {
        repairingPacks.stream()
                .map(repairingPackId -> InterventionDtoMother.withVehicle(Integer.toString(vehicles.get(new Random().nextInt(vehicles
                        .size())))))
                .forEach(interventionBusinesssController::create);
    }

    @NotNull
    private List<Integer> generateRepairingPacks() {
        return Stream.generate(AgrimDomainFactory::createRepairingPackDto).limit(3)
                .map(repairingPackBusinessController::create).collect(toList());
    }

    @NotNull
    private List<Integer> generateRepairInterventions(List<Integer> vehicles) {
        return vehicles.stream()
                .limit(5)
                .map(vehicleId -> InterventionDtoMother.withVehicle(Integer.toString(vehicleId)))
                .map(interventionBusinesssController::create)
                .collect(toList());
    }

    private void generateCafeInterventions() {
        Stream.generate(InterventionDtoMother::cafe).limit(5).forEach(interventionBusinesssController::create);
    }

    private void generateMoreVehicles(List<Integer> clients) {
        clients.stream()
                .limit(5)
                .map(clientId -> createVehicle(Integer.toString(clientId)))
                .forEach(vehicleGateway::create);
    }

    @NotNull
    private List<Integer> generateVehicles(List<Integer> clients) {
        return clients.stream()
                .limit(5)
                .map(clientId -> createVehicle(Integer.toString(clientId)))
                .map(vehicleGateway::create)
                .map(Integer::parseInt)
                .collect(toList());
    }

    @NotNull
    private List<Integer> generateClients() {
        return Stream.generate(ClientDtoMother::clientDto)
                .limit(5)
                .map(clientBusinessController::create)
                .collect(toList());
    }
}