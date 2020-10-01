package selenium.views;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import javax.ws.rs.core.UriBuilder;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class OperationsPage {

    private final String domain;

    public OperationsPage(String domain) {
        this.domain = domain;
    }

    public void openPage() {
        String url = UriBuilder.fromPath(domain).path("/backoffice/operations.xhtml").build().toString();
        open(url);
    }

    public void checkExistOperationsForm() {
        $(By.id("operationsForm")).shouldBe(Condition.visible);
    }
}
