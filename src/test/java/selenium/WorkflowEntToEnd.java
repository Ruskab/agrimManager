package selenium;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.api_controllers.MechanicApiController;
import api.object_mothers.MechanicDtoMother;
import front.dtos.Credentials;
import front.gateways.AuthenticationGateway;
import front.gateways.OperationsGateway;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import selenium.views.ClientsPage;
import selenium.views.DashboardPage;
import selenium.views.HeaderMenuPage;
import selenium.views.InterventionsPage;
import selenium.views.LoginPage;
import selenium.views.MyInterventionsPage;
import selenium.views.OperationsPage;
import selenium.views.VehiclesPage;

import javax.ws.rs.client.Client;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(SeleniumJupiter.class)
class WorkflowEntToEnd {

    public static final String APP_BASE_URL = "app.url";
    private static final Logger LOGGER = LogManager.getLogger(WorkflowEntToEnd.class);
    private static final String API_PATH = "/api/v0";
    private final AuthenticationGateway authenticationGateway = new AuthenticationGateway();
    private final MechanicApiController mechanicApiController = new MechanicApiController();
    Client client;
    Properties properties;
    private OperationsGateway operationsGateway;
    private String authToken;
    private String domain;
    private ClientsPage clientsPage = new ClientsPage("http://localhost:8080");
    private VehiclesPage vehiclesPage = new VehiclesPage("http://localhost:8080");
    private LoginPage loginPage = new LoginPage("http://localhost:8080");
    private DashboardPage dashboardPage = new DashboardPage("http://localhost:8080");
    private HeaderMenuPage headerMenuPage = new HeaderMenuPage("http://localhost:8080");
    private MyInterventionsPage myInterventionsPage = new MyInterventionsPage("http://localhost:8080");
    private InterventionsPage interventionsPage = new InterventionsPage("http://localhost:8080");
    private OperationsPage operationsPage = new OperationsPage("http://localhost:8080");

    @BeforeEach
    void createMechanic() {
        LOGGER.error("BeforeEach : start");
        client = new RestClientLoader().creteRestClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        domain = properties.getProperty(APP_BASE_URL);
        clientsPage = new ClientsPage(domain);
        vehiclesPage = new VehiclesPage(domain);
        loginPage = new LoginPage(domain);
        dashboardPage = new DashboardPage(domain);
        myInterventionsPage = new MyInterventionsPage(domain);
        interventionsPage = new InterventionsPage(domain);
        operationsPage = new OperationsPage(domain);
        headerMenuPage = new HeaderMenuPage(domain);
        LOGGER.error("Test pages init ok");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        Credentials credentials = Credentials.builder()
                .username(MechanicDtoMother.FAKE_NAME)
                .password(MechanicDtoMother.FAKE_PASSWORD)
                .build();
        LOGGER.error("Mechanic created");
        authToken = "Bearer " + authenticationGateway.authenticate(credentials);
        operationsGateway = new OperationsGateway(authToken);
        LOGGER.error("credo el cliente rest y usuario en DB");
    }

    @Test
    void navigate_all_pages() {
        LOGGER.error("start test navigate_all_pages");
        open(domain);
        LOGGER.error("abierto el dominio {}", domain);
        loginPage.login(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD);
        dashboardPage.checkHeaderMenu();
        clientsPage.openPage();
        clientsPage.checkExistClientsTable();
        clientsPage.openPage();
        clientsPage.openClient();
        vehiclesPage.openPage();
        vehiclesPage.checkExistVehiclesTable();
        myInterventionsPage.openPage();
        myInterventionsPage.checkExistMyInterventionTable();
        interventionsPage.openPage();
        interventionsPage.checkExistInterventionTable();
        operationsPage.openPage();
        operationsPage.checkExistOperationsForm();
    }

    @Test @Disabled
    void login_create_and_finish_intervention() {
        open(domain);
        loginPage.login(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD);
        dashboardPage.checkHeaderMenu();
        headerMenuPage.createIntervention();
        dashboardPage.checkActiveIntervention();
        dashboardPage.finishActiveIntervention();
        dashboardPage.checkNoActiveInterventions();
    }
//
//    @AfterEach
//    void tearDown() { //NOSONAR
//        LOGGER.info("Deleteting all data");
//        operationsGateway.deleteAll();
//    }
}