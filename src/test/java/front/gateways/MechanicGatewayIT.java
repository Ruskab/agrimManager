package front.gateways;

import api.api_controllers.MechanicApiController;
import api.object_mothers.FrontClientMother;
import api.object_mothers.FrontInterventionMother;
import api.object_mothers.FrontMechanicMother;
import api.object_mothers.MechanicDtoMother;
import front.AgrimDomainFactory;
import front.dtos.Client;
import front.dtos.Intervention;
import front.dtos.Mechanic;
import front.dtos.Vehicle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class MechanicGatewayIT {

    private static final Logger LOGGER = LogManager.getLogger(MechanicGatewayIT.class);
    private final MechanicApiController mechanicApiController = new MechanicApiController();
    private final AuthenticationGateway authenticationGateway = new AuthenticationGateway();
    private String authToken;
    private Integer mechanicId;
    private ClientGateway clientGateway;
    private VehicleGateway vehicleGateway;
    private InterventionGateway interventionGateway;
    private MechanicGateway mechanicGateway;
    private OperationsGateway operationsGateway;

    @BeforeEach
    void setUp() {
        mechanicId = (Integer) mechanicApiController.create(MechanicDtoMother.mechanicDto()).getEntity();
        authToken = "Bearer " + authenticationGateway.authenticate(AgrimDomainFactory.fakeCredentials());

        clientGateway = new ClientGateway(authToken);
        vehicleGateway = new VehicleGateway(authToken);
        interventionGateway = new InterventionGateway(authToken);
        mechanicGateway = new MechanicGateway(authToken);
        operationsGateway = new OperationsGateway(authToken);

    }

    @Test
    void create_and_read_mechanic() {
        Mechanic mechanic = FrontMechanicMother.mechanic();

        String mechanicId = mechanicGateway.create(mechanic);

        assertThat(mechanicId, is(notNullValue()));
    }

    @Test
    void add_intervention_to_mechanic() {
        Client client = FrontClientMother.client();
        String clientId = clientGateway.create(client);
        Vehicle vehicle = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicle);
        Intervention interventionDto = FrontInterventionMother.withVehicle(vehicleId);
        Mechanic mechanic = FrontMechanicMother.mechanic();
        mechanic.setId(mechanicId);
        mechanicGateway.createIntervention(mechanic, interventionDto);

        Mechanic updatedMechanic = mechanicGateway.read(Integer.toString(mechanicId));

        assertThat(updatedMechanic.getInterventionIds().isEmpty(), is(false));
    }

    @Test
    void read_mechanic_active_interventions() {
        Client clientDto = FrontClientMother.client();
        String clientId = clientGateway.create(clientDto);
        Vehicle vehicle = AgrimDomainFactory.createVehicle(clientId);
        String vehicleId = vehicleGateway.create(vehicle);
        Intervention interventionDto = FrontInterventionMother.withVehicle(vehicleId);
        Mechanic mechanic = FrontMechanicMother.mechanic();
        mechanic.setId(mechanicId);
        mechanicGateway.createIntervention(mechanic, interventionDto);

        List<Intervention> interventionDtos = mechanicGateway.searchInterventionsByFilter(Integer.toString(mechanicId), false);

        assertThat(interventionDtos.isEmpty(), is(false));

    }

    @AfterEach
    void delete_data() {
        LOGGER.info("clean database after test");
        operationsGateway.deleteAll();
    }
}