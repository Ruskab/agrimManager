package selenium.views;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import javax.ws.rs.core.UriBuilder;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class VehiclesPage {

    private final String domain;

    public VehiclesPage(String domain) {
        this.domain = domain;
    }

    public void openPage() {
        String url = UriBuilder.fromPath(domain).path("/backoffice/vehicles.xhtml").build().toString();
        open(url);
    }

    public void checkExistVehiclesTable() {
        $(".vehicles-datatable").shouldBe(Condition.visible);
    }
}
