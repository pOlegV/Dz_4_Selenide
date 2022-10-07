import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TestSelenide {
    private WebDriver driver;
    @BeforeAll
    static void setAppAll(){
        WebDriverManager.chromedriver().setup();
    }
    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
    public String generateDateInCalendar(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd"));
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
    @Test
    public void testFillingOutFormsPopup(){
        $("[data-test-id='city'] input").setValue("Мо");
        ElementsCollection city = $$(".menu-item__control" );
        city.find(exactText("Москва")).click();
        String dataNow;
        $x("//input[@placeholder='Дата встречи']").click();
        ElementsCollection data = $$("[data-day]" );
        String planningDate = generateDateInCalendar(7);
        for (int i = 1; i < 10; i++){
            String tmp = "0" + i;
            if (tmp.equals(planningDate)){
                dataNow = planningDate.substring(1, 2);
                planningDate = dataNow;
            }
        }
        if (data.find(exactText(planningDate)).exists()){
            $x("//input[@placeholder='Дата встречи']").click();
            data.find(exactText(planningDate)).click();
        }
        else {
            $x("//input[@placeholder='Дата встречи']").click();
            ElementsCollection rightMonth = $$(".calendar__arrow_direction_right");
            rightMonth.get(1).click();
            data = $$("[data-day]" );
            data.find(exactText(planningDate)).click();
        }

        $("[data-test-id='name'] input").setValue("Петров-Боткин Илья");
        $("[data-test-id='phone'] input").setValue("+79874561234");
        $("[data-test-id='agreement']").click();
        $$("button").find(exactText("Забронировать")).click();
        String notification = "Встреча успешно забронирована на " + planningDate;
        $("[data-test-id='notification']")
                .should(appear, Duration.ofSeconds(15)).should(Condition.text(notification));
    }

}
