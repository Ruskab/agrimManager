package selenium.views;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import javax.ws.rs.core.UriBuilder;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class InterventionsPage {

    private final String domain;

    public InterventionsPage(String domain) {
        this.domain = domain;
    }

    public void openPage() {
        String url = UriBuilder.fromPath(domain).path("/backoffice/interventions.xhtml").build().toString();
        open(url);
    }

    public void checkExistInterventionTable() {
        $(By.id("data-view-all-interventions-form:myInterventionTable")).shouldBe(Condition.visible);
    }
}
