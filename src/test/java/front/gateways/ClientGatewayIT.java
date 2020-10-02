package front.gateways;

import api.api_controllers.AuthenticationApiController;
import api.api_controllers.MechanicApiController;
import api.dtos.CredentialsDto;
import api.object_mothers.FrontClientMother;
import api.object_mothers.MechanicDtoMother;
import front.AgrimDomainFactory;
import front.dtos.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class ClientGatewayIT {

    private static final Logger LOGGER = LogManager.getLogger(ClientGatewayIT.class);

    private final MechanicApiController mechanicApiController = new MechanicApiController();
    private final AuthenticationGateway authenticationGateway = new AuthenticationGateway();
    private String authToken;
    private ClientGateway clientGateway;
    private OperationsGateway operationsGateway;

    @BeforeEach
    void setUp() {
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + authenticationGateway.authenticate(AgrimDomainFactory.fakeCredentials());
        clientGateway = new ClientGateway(authToken);
        operationsGateway = new OperationsGateway(authToken);

    }

    @Test
    void create_and_read_clientDto() {
        String clientId = clientGateway.create(FrontClientMother.client());

        Client createdClientDto = clientGateway.read(clientId);

        assertThat(createdClientDto.getFullName(), is(FrontClientMother.FAKE_FULL_NAME));
        assertThat(createdClientDto.getHours(), is(FrontClientMother.HOURS));
    }

    @Test
    void delete_client() {
        String clientId = clientGateway.create(FrontClientMother.client());

        clientGateway.delete(Integer.parseInt(clientId));

        assertThat(clientGateway.readAll().isEmpty(), is(true));
    }

    @Test
    void update_client() {
        Client clientDto = FrontClientMother.client();
        String clientId = clientGateway.create(clientDto);
        Client createdClient = clientGateway.read(clientId);
        createdClient.setFullName("newFullName");
        createdClient.setHours(5);

        clientGateway.update(createdClient);

        Client updatedClientDto = clientGateway.read(clientId);
        assertThat(updatedClientDto.getFullName(), is("newFullName"));
        assertThat(updatedClientDto.getHours(), is(5));
    }

    @AfterEach
    void delete_data() {
        LOGGER.info("clean database after test");
        operationsGateway.deleteAll();
    }


}