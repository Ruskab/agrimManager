package front.gateways;

import api.api_controllers.MechanicApiController;
import api.object_mothers.MechanicDtoMother;
import front.dtos.Credentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class AuthenticationGatewayIT {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationGatewayIT.class);
    private final MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;
    private OperationsGateway operationsGateway;
    private AuthenticationGateway authenticationGateway;

    @BeforeEach
    void setUp() {
        mechanicApiController.create(MechanicDtoMother.authUser());
        authenticationGateway = new AuthenticationGateway();
    }

    @Test
    void authenticate_user() {
        Credentials credentialsDto = new Credentials(MechanicDtoMother.FAKE_AUTH_NAME, MechanicDtoMother.FAKE_AUTH_PASSWORD);

        authToken = authenticationGateway.authenticate(credentialsDto);

        assertThat(authToken.isEmpty(), is(false));
    }

    @AfterEach
    void delete_mechanic() {
        LOGGER.info("clean database after test");
        operationsGateway = new OperationsGateway(authToken);
        operationsGateway.deleteAll();
    }

}