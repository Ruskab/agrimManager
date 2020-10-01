package selenium.views;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import javax.ws.rs.core.UriBuilder;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ClientsPage {

    private final String domain;

    public ClientsPage(String domain) {
        this.domain = domain;
    }

    public void openPage() {
        String url = UriBuilder.fromPath(domain).path("/backoffice/clients.xhtml").build().toString();
        open(url);
    }

    public void checkExistClientsTable() {
        $(By.id("data-view-clients-form:clientsTable")).shouldBe(Condition.visible);
    }

    public void openClient() {
        $(".ui-datatable-selectable .d-full-name").click();
        $(By.id("data-view-clients-form:configClientComponent:configClient")).shouldBe(Condition.visible);
    }
}
