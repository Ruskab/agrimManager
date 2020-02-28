package selenium;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.api_controllers.AuthenticationApiController;
import api.api_controllers.MechanicApiController;
import api.dtos.CredentialsDto;
import api.object_mothers.MechanicDtoMother;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class LoginEntToEnd {

    public static final String APP_BASE_URL = "app.url";
    private static final Logger LOGGER = LogManager.getLogger(LoginEntToEnd.class);
    private static final String API_PATH = "/api/v0";
    @Rule
    public BrowserWebDriverContainer chrome =
            new BrowserWebDriverContainer()
                    .withCapabilities(DesiredCapabilities.chrome());
    Client client;
    Properties properties;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;

    @Before
    void setUp() {
        RemoteWebDriver driver = chrome.getWebDriver();
        WebDriverRunner.setWebDriver(driver);
    }

    @BeforeEach
    void createMechanic() {
        client = new RestClientLoader().creteRestClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
        LOGGER.info("credo el cliente rest y usuario en DB");
    }

    @Test
    void login() {
        open("http://localhost:8080/agrimManager");
        $(By.id("loginPanel:loginForm:username")).setValue(MechanicDtoMother.FAKE_NAME);
        $(By.id("loginPanel:loginForm:password")).setValue(MechanicDtoMother.FAKE_PASSWORD);
        $(By.id("loginPanel:loginForm:loginButton")).click();
        $(By.id("activeInterventionPanel:activeInterventionForm:pnlEmptyActiveIntervention")).shouldBe(Condition.visible);
        $(By.id("activeInterventionPanel:activeInterventionForm:pnlEmptyActiveIntervention")).shouldHave(Condition.text("No hay intervenciones"));
    }

    @After
    void tearDown() {
        WebDriverRunner.closeWebDriver();

        LOGGER.info("Deleteting all data");
        client.target(properties.getProperty(APP_BASE_URL) + API_PATH + "/delete")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }
}