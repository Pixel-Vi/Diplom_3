package tests;

import api.AuthApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import pages.MainPage;
import user.UserGenerator;
import data.User;
import utils.BrowserFactory;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class LoginTest {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru/";
    private static final String REGISTER_URL = BASE_URL + "register";
    private static final String LOGIN_URL = BASE_URL + "login";

    private WebDriver driver;
    private MainPage mainPage;
    private LoginPage loginPage;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        driver = BrowserFactory.getDriver();
        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        
        user = UserGenerator.getValidUser();
        
        // Регистрируем пользователя через API
        accessToken = AuthApi.registerUser(user)
                .then()
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Вход по кнопке 'Войти в аккаунт'")
    @Description("Тест проверяет вход через кнопку 'Войти в аккаунт' на главной странице")
    public void loginViaMainPageButtonTest() {
        driver.get(REGISTER_URL);
        loginPage.clickLoginLink();
        loginPage.login(user.getEmail(), user.getPassword());

        mainPage.waitForUrlToBeBase();
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActive());
    }

    @Test
    @DisplayName("Вход через кнопку 'Личный кабинет'")
    @Description("Тест проверяет вход через кнопку 'Личный кабинет' в хедере")
    public void loginViaPersonalAccountButtonTest() {
        mainPage.open();
        mainPage.clickPersonalAccountButton();
        loginPage.login(user.getEmail(), user.getPassword());

        mainPage.waitForUrlToBeBase();
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActive());
    }

    @Test
    @DisplayName("Вход через ссылку в форме регистрации")
    @Description("Тест проверяет вход через ссылку 'Войти' на странице регистрации")
    public void loginViaRegistrationFormLinkTest() {
        driver.get(REGISTER_URL);
        loginPage.clickLoginLink();
        loginPage.login(user.getEmail(), user.getPassword());

        mainPage.waitForUrlToBeBase();
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActive());
    }

    @Test
    @DisplayName("Вход через ссылку в форме восстановления пароля")
    @Description("Тест проверяет вход через ссылку 'Войти' на странице восстановления пароля")
    public void loginViaPasswordRecoveryFormLinkTest() {
        driver.get(LOGIN_URL);
        loginPage.clickRestorePasswordLink();
        
        // Возвращаемся на страницу входа
        driver.navigate().back();
        loginPage.waitForPageLoad();
        loginPage.login(user.getEmail(), user.getPassword());

        mainPage.waitForUrlToBeBase();
        assertTrue("Главная страница не отображается после входа", mainPage.isBunsActive());
    }

    @After
    public void tearDown() {
        try {
            if (accessToken != null) {
                AuthApi.deleteUser(accessToken);
            }
        } catch (Exception e) {
            System.out.println("Ошибка очистки: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
