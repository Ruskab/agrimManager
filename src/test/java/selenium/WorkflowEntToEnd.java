package selenium;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.api_controllers.MechanicApiController;
import api.object_mothers.MechanicDtoMother;
import com.codeborne.selenide.WebDriverRunner;
import front.dtos.Credentials;
import front.gateways.AuthenticationGateway;
import front.gateways.OperationsGateway;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import selenium.views.ClientsPage;
import selenium.views.DashboardPage;
import selenium.views.InterventionsPage;
import selenium.views.LoginPage;
import selenium.views.MyInterventionsPage;
import selenium.views.OperationsPage;
import selenium.views.VehiclesPage;

import javax.ws.rs.client.Client;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.open;

class WorkflowEntToEnd {

    @Rule
    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer().withCapabilities(DesiredCapabilities.chrome());
    public static final String APP_BASE_URL = "app.url";
    private static final Logger LOGGER = LogManager.getLogger(WorkflowEntToEnd.class);
    private static final String API_PATH = "/api/v0";
    private OperationsGateway operationsGateway;
    private final AuthenticationGateway authenticationGateway = new AuthenticationGateway();
    Client client;
    Properties properties;
    private String authToken;
    private String domain;
    private final MechanicApiController mechanicApiController = new MechanicApiController();

    private ClientsPage clientsPage = new ClientsPage("http://localhost:8080");
    private VehiclesPage vehiclesPage = new VehiclesPage("http://localhost:8080");
    private LoginPage loginPage = new LoginPage("http://localhost:8080");
    private DashboardPage dashboardPage = new DashboardPage("http://localhost:8080");
    private MyInterventionsPage myInterventionsPage = new MyInterventionsPage("http://localhost:8080");
    private InterventionsPage interventionsPage = new InterventionsPage("http://localhost:8080");
    private OperationsPage operationsPage = new OperationsPage("http://localhost:8080");

    @Before
    void setUp() {
        RemoteWebDriver driver = chrome.getWebDriver();
        LOGGER.info("get webDriver : {}", driver.getCurrentUrl());
        WebDriverRunner.setWebDriver(driver);
    }

    @BeforeEach
    void createMechanic() {
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

        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        Credentials credentials = Credentials.builder()
                .username(MechanicDtoMother.FAKE_NAME)
                .password(MechanicDtoMother.FAKE_PASSWORD)
                .build();
        authToken = "Bearer " +  authenticationGateway.authenticate(credentials);
        operationsGateway = new OperationsGateway(authToken);
        LOGGER.info("credo el cliente rest y usuario en DB");
    }

    @Test
    void navigate_all_pages() {
        open(domain);
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

    @After
    void tearDown() {
        WebDriverRunner.closeWebDriver();
        LOGGER.info("Deleteting all data");
        operationsGateway.deleteAll();
    }
}