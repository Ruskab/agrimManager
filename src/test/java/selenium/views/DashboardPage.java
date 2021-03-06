package selenium.views;

import com.codeborne.selenide.Condition;

import javax.ws.rs.core.UriBuilder;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DashboardPage {

    private final String domain;

    public DashboardPage(String domain) {
        this.domain = domain;
    }

    public void openPage() {
        String url = UriBuilder.fromPath(domain).path("/backoffice/dashboard.xhtml").build().toString();
        open(url);
    }

    public void checkHeaderMenu() {
        $(".d-header-menu").shouldBe(Condition.visible);
    }

    public void checkActiveIntervention() {
        $(".active-intervention-description").shouldHave(Condition.text("test description"));
    }

    public void finishActiveIntervention() {
        $(".finish-active-interventions-btn").click();
    }

    public void checkNoActiveInterventions() {
        $(".no-active-interventions-panel").shouldHave(Condition.text("No hay intervenciones activas"));
    }
}
