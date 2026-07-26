package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class MainPage {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru/";
    private static final String TAB_CLASS = "tab_tab__1SPyG";
    private static final String ACTIVE_TAB_CLASS = "tab_tab_type_current__2BEPc";

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы
    private final By constructorTitle = By.xpath("//h1[contains(text(), 'Соберите')]");
    private final By loginAccountButton = By.xpath(".//button[text()='Войти в аккаунт']");
    private final By personalAccountButton = By.xpath(".//p[text()='Личный Кабинет']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    @Step("Открытие главной страницы")
    public void open() {
        driver.get(BASE_URL);
        waitForPageLoad();
    }
    @Step("Ожидание загрузки главной страницы")
    public void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(constructorTitle));
    }
    
    // Вспомогательный метод для получения вкладки по тексту
    private WebElement getTabByName(String tabName) {
        List<WebElement> tabs = driver.findElements(By.className(TAB_CLASS));
        for (WebElement tab : tabs) {
            String text = tab.getText();
            if (text.contains(tabName)) {
                return tab;
            }
        }
        throw new RuntimeException("Вкладка с названием '" + tabName + "' не найдена");
    }
    
    @Step("Клик по разделу Булки")
    public void clickBunsSection() {
        getTabByName("Булки").click();
        waitForTabActive(getTabByName("Булки"));
    }
    @Step("Клик по разделу Соусы")
    public void clickSaucesSection() {
        getTabByName("Соусы").click();
        waitForTabActive(getTabByName("Соусы"));
    }
    @Step("Клик по разделу Начинки")
    public void clickFillingsSection() {
        getTabByName("Начинки").click();
        waitForTabActive(getTabByName("Начинки"));
    }
    
    @Step("Ожидание активации вкладки")
    public void waitForTabActive(WebElement tab) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.attributeContains(tab, "class", ACTIVE_TAB_CLASS));
    }

    // Методы проверки состояния
    @Step("Проверка активности раздела Булки")
    public boolean isBunsActive() {
        WebElement tab = getTabByName("Булки");
        return tab.getAttribute("class").contains(ACTIVE_TAB_CLASS);
    }

    @Step("Проверка активности раздела Соусы")
    public boolean isSaucesActive() {
        WebElement tab = getTabByName("Соусы");
        return tab.getAttribute("class").contains(ACTIVE_TAB_CLASS);
    }

    @Step("Проверка активности раздела Начинки")
    public boolean isFillingsActive() {
        WebElement tab = getTabByName("Начинки");
        return tab.getAttribute("class").contains(ACTIVE_TAB_CLASS);
    }

    // метод проверки начального состояния вкладок конструктора бургеров, проверяет,
    // что раздел Булки активен по умолчанию
    @Step("Проверка, что раздел Булки активен по умолчанию")
    public boolean isBunsActiveByDefault() {
        return isBunsActive();
    }
    
    @Step("Клик по кнопке Войти в аккаунт")
    public void clickLoginAccountButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginAccountButton)).click();
    }

    @Step("Клик по кнопке Личный кабинет")
    public void clickPersonalAccountButton() {
        wait.until(ExpectedConditions.elementToBeClickable(personalAccountButton)).click();
    }

    @Step("Ожидание перехода на главную страницу")
    public void waitForUrlToBeBase() {
        wait.until(ExpectedConditions.urlToBe(BASE_URL));
    }
}
