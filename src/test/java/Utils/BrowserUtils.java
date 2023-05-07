package Utils;

import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class BrowserUtils {

    public static void selectBy(WebElement location,String value,String methodName) {

        Select select = new Select(location);

        switch (methodName) {

            case "text":
                select.selectByVisibleText(value);
                break;

            case "value":
                select.selectByValue(value);
                break;

            case "index":
                select.selectByIndex(Integer.parseInt(value));
                break;

            default:
                System.out.println("Method name is not available, use text,value or index");


        }

    }

    @Test
    public void shortCutClass() throws InterruptedException {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://demo.guru99.com/test/newtours/reservation.php");

        WebElement oneWayButton = driver.findElement(By.xpath("//input[@value='oneway']"));
        oneWayButton.click();
        Thread.sleep(2000);

        WebElement passngerCount = driver.findElement(By.xpath("//select[@name='passCount']"));
        Select passengerSelect = new Select(passngerCount);
        passengerSelect.selectByIndex(0);
        Thread.sleep(2000);

        WebElement departing = driver.findElement(By.xpath("//select[@name='fromPort']"));
        Select departingSelect = new Select(departing);
        String actualFirstOption = departingSelect.getFirstSelectedOption().getText().trim();
        String expectedFirstOption = "Acapulco";
        Assert.assertEquals(actualFirstOption,expectedFirstOption);
        departingSelect.selectByVisibleText("Paris");
        Thread.sleep(2000);

        WebElement fromMonth = driver.findElement(By.xpath("//select[@name='fromMonth']"));
        BrowserUtils.selectBy(fromMonth,"8","value");

        Thread.sleep(2000);

        WebElement fromDay = driver.findElement(By.xpath("//select[@name='fromDay']"));
        BrowserUtils.selectBy(fromDay,"15","text");

        Thread.sleep(2000);

        WebElement arrivingIn = driver.findElement(By.xpath("//select[@name='toPort']"));
        BrowserUtils.selectBy(arrivingIn,"San Francisco","text");

        Thread.sleep(2000);

        WebElement returning = driver.findElement(By.xpath("//select[@name='toMonth']"));
        BrowserUtils.selectBy(returning,"12","value");

        Thread.sleep(2000);

        WebElement toDay = driver.findElement(By.xpath("//select[@name='toDay']"));
        BrowserUtils.selectBy(toDay,"15","text");

        Thread.sleep(2000);

        WebElement chooseClass = driver.findElement(By.xpath("//input[@value='First']"));
        chooseClass.click();

        WebElement airlineCarrier = driver.findElement(By.xpath("//select[@name='airline']"));
        Select airlineCarrierSelect = new Select(airlineCarrier);
        List<WebElement> actualAllOptions = airlineCarrierSelect.getOptions(); // 4 option inside
        List<String> expectedAllOptions = Arrays.asList("No Preference","Blue Skies Airlines","Unified Airlines","Pangea Airlines");

        for (int i = 0; i < actualAllOptions.size(); i++) {
            Assert.assertEquals(actualAllOptions.get(i).getText().trim(),expectedAllOptions.get(i));
        }
        airlineCarrierSelect.selectByVisibleText("Unified Airlines");
        Thread.sleep(1000);

        WebElement continueButton = driver.findElement(By.xpath("//input[@name='findFlights']"));
        continueButton.click();

        WebElement message = driver.findElement(By.xpath("//font[@size='4']"));
        String actualMessage = message.getText();
        String expectedMessage = "After flight finder - No Seats Available";
        Assert.assertEquals(actualMessage,expectedMessage);


    }

    public static String getText(WebElement element){

        return element.getText().trim();
    }

    public static String getTitleWithJS(WebDriver driver ){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript("return document.title").toString().trim();

    }

    public static void clickWithJS(WebDriver driver, WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click()",element);

    }

    public static void scrollWithJS(WebDriver driver, WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true)",element);



    }

    public static void switchById(WebDriver driver){
        String mainPageId = driver.getWindowHandle();
        Set<String> allPagesId = driver.getWindowHandles();

        for ( String id : allPagesId ) {

            if (!id.equals(mainPageId)){
                driver.switchTo().window(id);
            }
        }

    }

    public static void switchByTitle(WebDriver driver, String title){

        Set<String> allPagesId = driver.getWindowHandles();

        for ( String id : allPagesId ) {
            driver.switchTo().window(id);
            if (driver.getTitle().contains(title)){
                break;
            }
        }

    }

    public static void getScreenShot(WebDriver driver,String packageName){

        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

        String location = System.getProperty("src/test/java/"+packageName+"/");

        try {
            FileUtils.copyFile(file,new File(location+System.currentTimeMillis()));
        }catch (IOException e){
            throw new RuntimeException();

        }
    }


    public static void getScreenShotForCucumber(WebDriver driver, Scenario scenario){
        Date currentDate = new Date();
        String screenShotFileName = currentDate.toString().replace(" "," ").replace(":","-");
        if (scenario.isFailed()){
            File screenShotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(screenShotFile,new File("src/test/java/screenshot/"+screenShotFileName+".png"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }





}
