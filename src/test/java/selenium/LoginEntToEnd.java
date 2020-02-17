package selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

class LoginEntToEnd {

    private WebDriver driver;

    @Test
    @Disabled
    void login() {
        System.setProperty("webdriver.chrome.driver","C:\\forja/components/selenium/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/agrimManager");
        driver.findElement(By.xpath("//*[@id=\"j_idt12:loginForm:username\"]")).sendKeys("ruskab");
        driver.findElement(By.xpath("//*[@id=\"j_idt12:loginForm:password\"]")).sendKeys("test");
        driver.findElement(By.xpath("//*[@id=\"j_idt12:loginForm:loginButton\"]")).click();
    }

    @AfterEach
    private void endTest(){
        driver.close();
        driver.quit();
    }

}