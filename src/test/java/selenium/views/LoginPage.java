package selenium.views;

import org.openqa.selenium.By;

import javax.ws.rs.core.UriBuilder;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final String domain;

    public LoginPage(String domain) {
        this.domain = domain;
    }

    public void openPage() {
        String url = UriBuilder.fromPath(domain).path("/user_access/loginPage.xhtml").build().toString();
        open(url);
    }

    public void login(String username, String password) {
        $(By.id("loginPanel:loginForm:username")).setValue(username);
        $(By.id("loginPanel:loginForm:password")).setValue(password);
        $(By.id("loginPanel:loginForm:loginButton")).click();
    }
}
