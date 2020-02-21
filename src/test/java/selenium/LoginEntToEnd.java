package selenium;

import api.PropertiesResolver;
import api.RestClientLoader;
import api.api_controllers.AuthenticationApiController;
import api.api_controllers.MechanicApiController;
import api.dtos.CredentialsDto;
import api.object_mothers.MechanicDtoMother;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class LoginEntToEnd {

    public static final String APP_BASE_URL = "app.url";
    private static final String API_PATH = "/api/v0";
    Client client;
    Properties properties;
    private MechanicApiController mechanicApiController = new MechanicApiController();
    private String authToken;

    @BeforeEach
    void setUp() {
        client = new RestClientLoader().creteRestClient();
        properties = new PropertiesResolver().loadPropertiesFile("config.properties");
        mechanicApiController.create(MechanicDtoMother.mechanicDto());
        authToken = "Bearer " + new AuthenticationApiController().authenticateUser(new CredentialsDto(MechanicDtoMother.FAKE_NAME, MechanicDtoMother.FAKE_PASSWORD)).getEntity();
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

    @AfterEach
    void tearDown() {
        client.target(properties.getProperty(APP_BASE_URL) + API_PATH + "/delete")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, authToken)
                .delete();
    }
}