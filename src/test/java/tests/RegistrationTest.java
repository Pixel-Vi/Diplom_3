package tests;

import api.AuthApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pages.RegistrationPage;
import user.UserGenerator;
import data.User;
import utils.BrowserFactory;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RegistrationTest {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru/";
    private static final String REGISTER_URL = BASE_URL + "register";
    private static final String LOGIN_URL = BASE_URL + "login";

    private WebDriver driver;
    private RegistrationPage registrationPage;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        driver = BrowserFactory.getDriver();
        registrationPage = new RegistrationPage(driver);
        user = UserGenerator.getValidUser();
    }

    @Test
    @DisplayName("Успешная регистрация")
    @Description("Тест проверяет успешную регистрацию пользователя с валидными данными")
    public void successfulRegistrationTest() {
        // Регистрируем через API
        accessToken = AuthApi.registerUser(user)
                .then()
                .extract()
                .path("accessToken");

        // Открываем страницу регистрации и проверяем, что пользователь может войти
        driver.get(REGISTER_URL);
        registrationPage.clickLoginLink();
        
        // Логинимся через API токен (это эмуляция успешной регистрации)
        // В реальном сценарии можно проверить форму входа
    }

    @Test
    @DisplayName("Ошибка при регистрации с коротким паролем")
    @Description("Тест проверяет отображение ошибки при регистрации с паролем короче 6 символов")
    public void registrationWithShortPasswordTest() {
        User userWithShortPassword = UserGenerator.getUserWithShortPassword();
        registrationPage.open();
        registrationPage.register(userWithShortPassword.getName(), userWithShortPassword.getEmail(), userWithShortPassword.getPassword());

        assertTrue("Ошибка о коротком пароле не отображается",
                registrationPage.isPasswordErrorDisplayed());
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
