import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class TestSelenide {
    @BeforeAll
    static void setAppAll(){
        WebDriverManager.chromedriver().setup();
//        System.setProperty("webdriver.chrome.driver", "driver/win/chromedriver.exe");
    }

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setUpp() {
        open("http://localhost:9999");
    }

    @Test
    public void test(){
        String planningDate = generateDate(4);
        $x("//input[@placeholder='Дата встречи']")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Петров-Боткин Илья");
        $("[data-test-id='phone'] input").setValue("+79874561234");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();

        String notification = "Встреча успешно забронирована на " + planningDate;
        $("[data-test-id='notification']")
                .should(appear, Duration.ofSeconds(15)).should(Condition.text(notification));


    }

}
