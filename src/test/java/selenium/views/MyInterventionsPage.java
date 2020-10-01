package selenium.views;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import javax.ws.rs.core.UriBuilder;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MyInterventionsPage {

    private final String domain;

    public MyInterventionsPage(String domain) {
        this.domain = domain;
    }

    public void openPage() {
        String url = UriBuilder.fromPath(domain).path("/backoffice/myInterventions.xhtml").build().toString();
        open(url);
    }

    public void checkExistMyInterventionTable() {
        $(By.id("data-view-my-interventions-form:interventionTable")).shouldBe(Condition.visible);
    }
}
