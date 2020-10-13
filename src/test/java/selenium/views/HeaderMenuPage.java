package selenium.views;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class HeaderMenuPage {

    private final String domain;

    public HeaderMenuPage(String domain) {
        this.domain = domain;
    }

    public void createIntervention() {
        new Actions(WebDriverRunner.getWebDriver()).moveToElement($(".submenu-interventions")).perform();
        $(".submenu-create-intervention").shouldBe(Condition.visible);
        $(".submenu-create-intervention").click();
        $(".dev-btn-caffe").click();
        $$(".create-intervention-wizzard .ui-button-text.ui-c").get(1).click();
        $(".dev-intervention-description").sendKeys("test description");
        $$(".create-intervention-wizzard .ui-button-text.ui-c").get(1).click();
        $(".create-button").click();
    }


}
