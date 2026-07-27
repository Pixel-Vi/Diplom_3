package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public class BrowserFactory {
    private static final Properties properties = new Properties();
    private static final Properties yandexProperties = new Properties();
    private static final int IMPLICIT_WAIT_SECONDS = 5;

    static {
        try (InputStream input = BrowserFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        try (InputStream input = BrowserFactory.class.getClassLoader().getResourceAsStream("yandex-config.properties")) {
            if (input != null) {
                yandexProperties.load(input);
            }
        } catch (IOException ex) {
            System.out.println("Файл yandex-config.properties не найден. Используются переменные окружения.");
        }
    }

    public static WebDriver getDriver() {
        String browser = System.getProperty("browser",
                        properties.getProperty("browser", "chrome"))
                .toLowerCase();

        System.out.println("Using browser: " + browser); // Логирование для отладки

        WebDriver driver;
        switch (browser) {
            case "yandex":
                driver = startYandexBrowser();
                break;
            case "chrome":
            default:
                driver = startChrome();
        }

        // Устанавливаем Implicit Wait для всех создаваемых драйверов
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        return driver;
    }

    private static WebDriver startYandexBrowser() {
        // Полное отключение логов WebDriver
        System.setProperty("webdriver.chrome.silentOutput", "true");
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.OFF);

        // Указываем путь к драйверу Яндекс.Браузера
        String yandexDriverPath = getPropertyValue("yandex.driver.path", "YANDEX_DRIVER_PATH");
        String yandexBinaryPath = getPropertyValue("yandex.binary.path", "YANDEX_BINARY_PATH");

        if (yandexDriverPath == null || yandexBinaryPath == null) {
            throw new RuntimeException("Путь к YandexDriver и/или YandexBinary не найден. " +
                    "Установите переменные окружения YANDEX_DRIVER_PATH и YANDEX_BINARY_PATH " +
                    "или создайте файл yandex-config.properties в src/test/resources/");
        }

        System.setProperty("webdriver.chrome.driver", yandexDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.setBinary(yandexBinaryPath);
        options.addArguments(
                "--remote-allow-origins=*",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--disable-blink-features=AutomationControlled",
                "--disable-logging",
                "--log-level=3",
                "--lang=ru"  // Добавлено: принудительный русский язык
        );

        // Полное отключение DevTools логов
        options.setExperimentalOption("excludeSwitches",
                new String[]{"enable-automation", "enable-logging"});

        WebDriver driver = new ChromeDriver(options);
        // Пробуем максимизировать, но не падаем, если не удалось (некоторые браузеры не поддерживают)
        try {
            driver.manage().window().maximize();
        } catch (Exception e) {
            System.out.println("Невозможно максимизировать окно Яндекс.Браузера: " + e.getMessage());
        }
        return driver;
    }

    private static WebDriver startChrome() {
        // Полное отключение логов WebDriver
        System.setProperty("webdriver.chrome.silentOutput", "true");
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.OFF);

        WebDriverManager.chromedriver()
                .setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--remote-allow-origins=*",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--disable-blink-features=AutomationControlled",
                "--disable-logging",
                "--log-level=3"
        );

        // Полное отключение DevTools
        options.setExperimentalOption("excludeSwitches",
                new String[]{"enable-automation", "enable-logging"});

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * Получает значение свойства: сначала из yandex-config.properties, затем из переменных окружения
     */
    private static String getPropertyValue(String propertyName, String envVarName) {
        // Сначала пробуем из yandex-config.properties
        String value = yandexProperties.getProperty(propertyName);
        if (value != null && !value.trim().isEmpty()) {
            return value;
        }
        
        // Затем из переменных окружения
        value = System.getenv(envVarName);
        return value;
    }
}
