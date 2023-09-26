package timetree.webapp;

import java.text.ParseException;
import java.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import timetree.date.TimeTreeEvent;
import timetree.manage.LogManager;
import timetree.manage.Logger;
import timetree.manage.Wait;

public class TimeTreeWebAppHandler {
  private static final Logger logger = LogManager.getLogger();

  private static final String TIMETREE_SIGNIN_URL = "https://timetreeapp.com/signin";

  private static final String SIGN_IN_USERNAME_ELEMENT_CSS_SELECTOR =
      "input[data-test-id='signin-form-email']";
  private static final String SIGN_IN_PASSWORD_ELEMENT_CSS_SELECTOR =
      "input[data-test-id='signin-form-password']";
  private static final String SIGN_IN_SUBMIT_CSS_SELECTOR =
      "button[data-test-id='signin-form-submit']";
  // 'pervious' is Timetree's typo.
  private static final String PREVIOUS_MONTH_CSS_SELECTOR =
      "button[data-test-id='pervious-button']";
  private static final String NEXT_MONTH_CSS_SELECTOR = "button[data-test-id='next-button']";

  private static final String CALENDAR_XPATH =
      "//p[contains(@class, 'e1rg0zpy2') and text() = 'Personal']";
  private static final String CALENDAR_XPATH_2 =
      "//div[contains(@class, 'e1rg0zpy1') and contains(@class, 'css-ok59kt')]";

  private RemoteWebDriver driver;

  public TimeTreeWebAppHandler(RemoteWebDriver webDriver) {
    this.driver = webDriver;
  }

  public void signIn(String username, String password) {
    driver.get(TIMETREE_SIGNIN_URL);

    sendKeysByCssSelector(driver, SIGN_IN_USERNAME_ELEMENT_CSS_SELECTOR, username);
    sendKeysByCssSelector(driver, SIGN_IN_PASSWORD_ELEMENT_CSS_SELECTOR, password);

    driver.findElement(By.cssSelector(SIGN_IN_SUBMIT_CSS_SELECTOR)).submit();
  }

  public void selectCalendar() throws InterruptedException {
    WebElement element = Wait.WaitForElementVisible(driver, CALENDAR_XPATH);
    driver.executeScript("arguments[0].click();", element);

    element = driver.findElement(By.xpath(CALENDAR_XPATH_2));
    driver.executeScript("arguments[0].click();", element);
  }

  public LocalDate getDateDisplayed() throws ParseException {
    String result = driver.findElement(By.xpath("//time")).getAttribute("datetime");

    LocalDate date = TimeTreeDateFormat.ParseDateElementValue(result);

    return date;
  }

  public void addNewEvent(TimeTreeEvent event) throws ParseException, InterruptedException {
    LocalDate current = getDateDisplayed();

    // if we're not already set to the right month in the calendar
    if (!currentDisplayedDateContainsEventDate(current, event.getStart())) {
      // Current will always be beginning of the month
      while (event.getStart().isBefore(current)) {
        current = goBackAMonth(current);
      }

      while (!currentDisplayedDateContainsEventDate(current, event.getStart())) {
        current = goForwardAMonth(current);
      }

      // TimetreeApp fix/hack... For some reason new events arent showing up without this...
      current = goForwardAMonth(current);
      current = goBackAMonth(current);
    }

    String cssSelector = getElementCssSelectorForDate(event.getStart());
    WebElement element = Wait.WaitForElementVisible(driver, By.cssSelector(cssSelector));

    String xPathForEventDate = getXPathForSetEvent(event);
    boolean eventInCalendar = Wait.IsElementVisible(driver, By.xpath(xPathForEventDate));

    if (!eventInCalendar) {
      addEventSimple(element, event.getTitleOfEvent(), xPathForEventDate);
    } else {
      logger.info(
          "Event already set: "
              + event.getTitleOfEvent()
              + " for date: "
              + event
              + " element xPath is: "
              + xPathForEventDate);
    }
  }

  private String getXPathForSetEvent(TimeTreeEvent event) {
    String xPathForEventDate =
        "//div[contains(@class, 'eventRow')]/div/div/div/span[text() = '"
            + event.getTitleOfEvent()
            + "']";

    return xPathForEventDate;
  }

  private boolean currentDisplayedDateContainsEventDate(
      LocalDate currentDisplayedDate, LocalDate eventDate) {
    return currentDisplayedDate.getMonthValue() == eventDate.getMonthValue()
        && currentDisplayedDate.getYear() == eventDate.getYear();
  }

  private void addEventSimple(WebElement element, String titleOfEvent, String xPathForEventDate)
      throws InterruptedException {
    logger.info("About to click and add event: " + titleOfEvent);
    element.click();
    Wait.WaitFor(50);
    element.click();
    Wait.WaitFor(500);
    driver.switchTo().activeElement().sendKeys(titleOfEvent + Keys.RETURN);

    Wait.WaitForElementVisible(driver, By.xpath(xPathForEventDate));

    logger.info("added event successfully: " + titleOfEvent);
  }

  private LocalDate goBackAMonth(LocalDate currentDate)
      throws InterruptedException, ParseException {
    return changeMonth(currentDate, -1);
  }

  private LocalDate goForwardAMonth(LocalDate currentDate)
      throws InterruptedException, ParseException {
    return changeMonth(currentDate, 1);
  }

  private LocalDate changeMonth(LocalDate currentDate, int direction)
      throws InterruptedException, ParseException {
    if (direction != -1 && direction != 1) {
      throw new IllegalArgumentException(
          "Direction should be -1 for previous month or 1 for next month");
    }

    LocalDate newExpectedCurrent = currentDate.plusMonths(direction);
    // We expect an element with the previous month, 2nd date to be visible (all dates for the month
    // should be visible, plus a few from the previous)
    String expectedCssSelectorToBeVisible =
        getElementCssSelectorForDate(newExpectedCurrent.plusDays(1));

    // click previous month button
    Wait.WaitForElementVisible(
            driver,
            By.cssSelector(direction == 1 ? NEXT_MONTH_CSS_SELECTOR : PREVIOUS_MONTH_CSS_SELECTOR))
        .click();

    // Wait for the month selection to load
    Wait.WaitForElementVisible(driver, By.cssSelector(expectedCssSelectorToBeVisible));

    // return new date (Don't calculate, to ensure working as expected)
    return getDateDisplayed();
  }

  private String getElementCssSelectorForDate(LocalDate date) {
    return "div[data-test-id='day-cell-" + TimeTreeDateFormat.GetFormatForDateCell(date);
  }

  private static void sendKeysByCssSelector(WebDriver driver, String cssSelector, String keys) {
    driver.findElement(By.cssSelector(cssSelector)).sendKeys(keys);
  }
}
