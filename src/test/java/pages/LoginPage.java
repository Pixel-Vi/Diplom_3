package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private static final String LOGIN_URL = "https://stellarburgers.education-services.ru/login";

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы (используем атрибуты name для надежности)
    private final By emailField = By.name("name");
    private final By passwordField = By.xpath(".//input[@type='password']");
    private final By loginButton = By.xpath("//button[contains(text(), 'Войти')]");
    private final By registerLink = By.xpath("//a[contains(text(), 'Зарегистрироваться')]");
    private final By restorePasswordLink = By.xpath("//a[contains(text(), 'Восстановить пароль')]");
    private final By loginLink = By.xpath("//a[contains(text(), 'Войти')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    @Step("Открытие страницы входа")
    public void open() {
        driver.get(LOGIN_URL);
        waitForPageLoad();
    }
    @Step("Ожидание загрузки страницы входа")
    public void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(loginButton));
    }
    @Step("Вход с email и паролем")
    public void login(String email, String password) {
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }
    @Step("Клик по ссылке Восстановить пароль")
    public void clickRestorePasswordLink() {
        driver.findElement(restorePasswordLink).click();
    }
    @Step("Клик по ссылке 'Войти'")
    public void clickLoginLink() {
        driver.findElement(loginLink).click();
    }

    @Step("Ожидание перехода на страницу регистрации")
    public void waitForUrlContainsRegister() {
        wait.until(ExpectedConditions.urlContains("register"));
    }

}
