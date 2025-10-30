import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);

        driver.get("http://localhost:9999/");
    }

    @Test
    void shouldReturnSuccessBlock() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Иванов");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79031465678");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id = order-success]"));
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.getText().trim());
    }

    @Test
    void shouldReturnMistakeWithBlankName() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79031465678");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        assertEquals("Поле обязательно для заполнения", result.getText().trim());
    }

    @Test
    void shouldReturnMistakeWithLatinName() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivan Ivanov");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79031465678");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test
    void shouldReturnMistakeWithBlankPhone() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        assertEquals("Поле обязательно для заполнения", result.getText().trim());

    }

    @Test
    void shouldReturnMistakeWithShortPhone() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Иванов");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7903146");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());

    }

    @Test
    void shouldReturnMistakeWithBlankAgreement() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Иванов");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79031465678");
        driver.findElement(By.className("button")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text"));
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", result.getText().trim());

    }

    @Test
    void shouldReturnFirstIfFormIsBlank() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        driver.findElement(By.className("button")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        assertEquals("Поле обязательно для заполнения", result.getText().trim());

    }

    @Test
    void shouldReturnFirstIfPhoneAndNameIsInvalid() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivan Ivanov");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+790314656");
        driver.findElement(By.className("button")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }


}
