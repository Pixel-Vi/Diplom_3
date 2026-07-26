# Stellar Burgers - Автотесты

![Java](https://img.shields.io/badge/Java-11-blue)
![Selenium](https://img.shields.io/badge/Selenium-4.25.0-green)
![JUnit](https://img.shields.io/badge/JUnit-4.13.2-orange)
![Allure](https://img.shields.io/badge/Allure-2.28.1-blue)

Автоматизированные тесты для веб-приложения "Космическая бургерная Stellar Burgers"

## Описание проекта

Этот проект содержит набор UI и API автотестов для проверки функциональности веб-приложения Stellar Burgers.

### Основные возможности:

- **UI Тесты**: Проверка интерфейса и пользовательских сценариев через Selenium WebDriver
- **API Тесты**: Проверка работы backend API через REST Assured
- **Генерация отчетов**: Формирование детальных отчетов с помощью Allure
- **Генерация тестовых данных**: Использование JavaFaker для создания валидных данных

## Технологический стек

| Компонент | Технология |
|-----------|-----------|
| Язык программирования | Java 11 |
| Фреймворк для тестирования | JUnit 4 |
| Инструмент для UI тестирования | Selenium WebDriver 4.25.0 |
| Инструмент для API тестирования | REST Assured 5.5.0 |
| Фреймворк для генерации данных | JavaFaker 1.0.2 |
| Отчеты | Allure 2.28.1 |
| Управление драйверами | WebDriverManager 5.9.2 |
| Сборщик проекта | Maven |

## Структура проекта

```
src/
├── main/                    # Основной код приложения
└── test/
    └── java/
        ├── api/              # API тесты
        │   └── AuthApi.java  # API для работы с авторизацией
        ├── data/             # Данные для тестов
        │   └── User.java     # Модель пользователя
        ├── pages/            # Page Object Model
        │   ├── LoginPage.java
        │   ├── MainPage.java
        │   └── RegistrationPage.java
        ├── tests/            # Автотесты
        │   ├── ConstructorTest.java
        │   ├── LoginTest.java
        │   └── RegistrationTest.java
        ├── user/             # Генераторы пользовательских данных
        │   └── UserGenerator.java
        └── utils/            # Вспомогательные утилиты
            └── BrowserFactory.java
```

## Запуск тестов

### Предварительные требования

- Java 11 или выше
- Maven 3.6 или выше
- Браузер (Chrome или Yandex Browser)

### Настройка окружения

#### Для Chrome (по умолчанию):
WebDriverManager автоматически загрузит драйвер Chrome.

#### Для Yandex Browser:
Создайте файл `src/test/resources/yandex-config.properties` со следующим содержимым:

```properties
yandex.driver.path=/path/to/yandex driver.exe
yandex.binary.path=/path/to/yandex.exe
```

Или установите переменные окружения:
- `YANDEX_DRIVER_PATH` - путь к YandexDriver
- `YANDEX_BINARY_PATH` - путь к исполняемому файлу Yandex Browser

### Команды запуска

#### Запуск всех тестов:
```bash
mvn clean test
```

#### Запуск конкретного теста:
```bash
mvn test -Dtest=LoginTest
```

#### Запуск с определенным браузером:
```bash
mvn test -Dbrowser=yandex
```

#### Генерация Allure отчета:
```bash
mvn clean test site
allure serve target/allure-results
```

## Типы тестов

### UI Тесты (Selenium WebDriver)

#### [ConstructorTest.java](src/test/java/tests/ConstructorTest.java)
Проверка работы конструктора бургера:
- Переход к разделу "Соусы"
- Переход к разделу "Начинки"
- Переход к разделу "Булки"

#### [LoginTest.java](src/test/java/tests/LoginTest.java)
Проверка входа в аккаунт:
- Вход через кнопку "Войти в аккаунт" на главной странице
- Вход через кнопку "Личный кабинет" в хедере
- Вход через ссылку в форме регистрации
- Вход через ссылку в форме восстановления пароля

#### [RegistrationTest.java](src/test/java/tests/RegistrationTest.java)
Проверка регистрации:
- Успешная регистрация пользователя
- Ошибка при регистрации с коротким паролем (менее 6 символов)

### API Тесты (REST Assured)

#### [AuthApi.java](src/test/java/api/AuthApi.java)
API эндпоинты для тестирования:
- `POST /api/auth/register` - Регистрация пользователя
- `POST /api/auth/login` - Вход в аккаунт
- `DELETE /api/auth/user` - Удаление пользователя

## Генерация тестовых данных

Используется библиотека [JavaFaker](https://github.com/DiUS/java-faker) для генерации реалистичных тестовых данных:

```java
// Генерация валидного пользователя
User user = UserGenerator.getValidUser();

// Генерация пользователя с коротким паролем
User user = UserGenerator.getUserWithShortPassword();
```

## Page Object Model

Проект использует паттерн Page Object Model для лучшей поддержки и читаемости тестов:

- [MainPage](src/test/java/pages/MainPage.java) - главная страница с конструктором
- [LoginPage](src/test/java/pages/LoginPage.java) - страница входа
- [RegistrationPage](src/test/java/pages/RegistrationPage.java) - страница регистрации

## Именование и документация

Все тесты снабжены аннотациями Allure для улучшенной документации:

```java
@Test
@DisplayName("Вход по кнопке 'Войти в аккаунт'")
@Description("Тест проверяет вход через кнопку 'Войти в аккаунт' на главной странице")
public void loginViaMainPageButtonTest() {
    // тест-кейс
}
```

## Известные особенности

1. Все тесты используют эндпоинт для проверки: `https://stellarburgers.education-services.ru/`
2. API тесты используют тот же URL для backend операций
3. При необходимости тесты регистрируют и удаляют пользователей через API для чистоты тестовой среды

## Решение проблем

### Проблема: Тесты падают с ошибкой TimeOut
**Решение**: Увеличьте время ожидания в классе `BrowserFactory` или используйте более быстрое интернет-соединение.

### Проблема: Тесты падают на Yandex Browser
**Решение**: Убедитесь, что пути в `yandex-config.properties` указаны верно и браузер не блокирует автотесты.

### Проблема: Ошибки при генерации отчетов Allure
**Решение**: Убедитесь, что установлен Allure Commandline: `brew install allure` или `choco install allure`

## Участие в проекте

При добавлении новых тестов:
1. Следуйте существующей структуре файлов
2. Используйте Page Object Pattern
3. Добавьте Allure аннотации с описанием
4. Пишите читаемые имена тестов в camelCase

## Лицензия

Этот проект создан для учебных целей.
#   D i p l o m _ 3  
 