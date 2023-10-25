package timetree.webapp;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import org.openqa.selenium.By;
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

  private static final String COMPEX_EVENT_OPEN_CSS_SELECTOR =
      "div[data-test-id='quick-create-more'";
  private static final String COMPLEX_EVENT_SUBMIT_BUTTON_CSS_SELECTOR =
      "button[data-test-id='event-form-submit-button']";
  private static final String COMPLEX_EVENT_CANCEL_BUTTON_CSS_SELECTOR =
      "button[data-test-id='event-form-cancel-button']";
  private static final String COMPEX_EVENT_END_DATE_CSS_SELECTOR =
      "input[data-test-id='end-date-picker']";
  private static final String COMPEX_EVENT_TITLE_CSS_SELECTOR = "form[data-test-id='event-form']";
  private static final String COMPEX_EVENT_LABEL_CSS_SELECTOR =
      "input[data-test-id='label-select']";
  private static final String COMPEX_EVENT_LABEL_SELECTION_CSS_SELECTOR =
      "li[data-test-id='Jewish Event']";
  private static final String COMPEX_EVENT_MEMBERS_CSS_SELECTOR =
      "div[data-test-id='attendees-select']";

  private static final String COMPEX_EVENT_MEMBERS_CSS_SELECTOR_PREFIX =
      "li[data-test-id='attendees-select-item-";

  private static final String COMPEX_EVENT_CANCEL_DAILOG_OKAY_CSS_SELECTOR =
      "button[data-test-id='confirm-dialog-ok-button']";

  private static final String EVENT_ADD_NEW_BUTTON_CSS_SELECTOR =
      "button[data-test-id='calendar-bar-event-add-button']";
  private static final String NEW_EVENT_START_DATE_INPUT_CSS_SELECTOR =
      "input[data-test-id='start-date-picker']";
  private static final String NEW_EVENT_END_DATE_INPUT_CSS_SELECTOR =
      "input[data-test-id='end-date-picker']";
  private static final String NEW_EVENT_NOTIFICATION_CSS_SELECTOR =
      "input[data-test-id='reminders-select']";

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

  public LocalDate getDateDisplayed() throws ParseException, InterruptedException {
    String result = Wait.WaitForElementVisible(driver, By.xpath("//time")).getAttribute("datetime");

    LocalDate date = TimeTreeDateFormat.ParseDateElementValue(result);

    return date;
  }

  public void addNewEvent(TimeTreeEvent event, List<String> users)
      throws ParseException, InterruptedException {
    logger.info("About to click and add complex event: " + event.getTitleOfEvent());

    clickNewEventButton(driver);
    enterTitleForEvent(driver, event.getTitleOfEvent());
    enterStartAndEndDate(driver, event.getStart(), event.getEnd());
    selectUsersForEvent(driver, users);
    selectHolidayTypeForEvent(driver);

    String notificationValue = getNotificationValueForEvent(driver);

    if (notificationValue == null || notificationValue.isBlank()) {
      setNotificationValueForEvent(driver);
    }

    submitNewEvent();
  }

  private void enterTitleForEvent(RemoteWebDriver driver, String titleOfEvent) {
    WebElement titleElement =
        driver
            .findElement(By.cssSelector(COMPEX_EVENT_TITLE_CSS_SELECTOR))
            .findElement(By.tagName("textarea"));

    titleElement.sendKeys(titleOfEvent);
  }

  private void selectHolidayTypeForEvent(RemoteWebDriver driver) throws InterruptedException {
    Wait.WaitForElementVisible(driver, By.cssSelector(COMPEX_EVENT_LABEL_CSS_SELECTOR)).click();
    Wait.WaitForElementVisible(driver, By.cssSelector(COMPEX_EVENT_LABEL_SELECTION_CSS_SELECTOR))
        .click();
  }

  private void submitNewEvent() throws InterruptedException {
    Wait.WaitForElementVisible(driver, By.cssSelector(COMPLEX_EVENT_SUBMIT_BUTTON_CSS_SELECTOR))
        .click();
  }

  private void selectUsersForEvent(RemoteWebDriver driver, List<String> users)
      throws InterruptedException {
    // click users label
    Wait.WaitForElementVisible(driver, By.cssSelector(COMPEX_EVENT_MEMBERS_CSS_SELECTOR)).click();

    for (String user : users) {
      WebElement userElement =
          Wait.WaitForElementVisible(
              driver, By.cssSelector(COMPEX_EVENT_MEMBERS_CSS_SELECTOR_PREFIX + user + "']"));
      WebElement nameElement = userElement.findElement(By.id("name"));

      boolean isChecked = "true".equals(nameElement.getAttribute("aria-checked"));

      if (!isChecked) {
        userElement.click();
        Wait.WaitForElementPropertyValue(nameElement, "aria-checked", "true");
      }
    }
    // click users label again to close the window
    Wait.WaitForElementVisible(driver, By.cssSelector(COMPEX_EVENT_MEMBERS_CSS_SELECTOR)).click();
  }

  private void enterStartAndEndDate(RemoteWebDriver driver, LocalDate start, LocalDate end)
      throws InterruptedException {
    WebElement startDateElement =
        Wait.WaitForElementVisible(driver, By.cssSelector(NEW_EVENT_START_DATE_INPUT_CSS_SELECTOR));
    WebElement endDateElement =
        Wait.WaitForElementVisible(driver, By.cssSelector(NEW_EVENT_END_DATE_INPUT_CSS_SELECTOR));

    startDateElement.clear();
    startDateElement.sendKeys(TimeTreeDateFormat.GetFormatForInput(start));

    endDateElement.clear();
    endDateElement.sendKeys(TimeTreeDateFormat.GetFormatForInput(end));
  }

  private void clickNewEventButton(WebDriver driver) throws InterruptedException {
    Wait.WaitForElementVisible(driver, By.cssSelector(EVENT_ADD_NEW_BUTTON_CSS_SELECTOR)).click();
  }

  private String getNotificationValueForEvent(WebDriver driver) throws InterruptedException {
    return Wait.WaitForElementVisible(driver, By.cssSelector(NEW_EVENT_NOTIFICATION_CSS_SELECTOR))
        .getAttribute("value");
  }

  private void setNotificationValueForEvent(WebDriver driver) throws InterruptedException {
    Wait.WaitForElementVisible(driver, By.cssSelector(NEW_EVENT_NOTIFICATION_CSS_SELECTOR)).click();

    Wait.WaitForElementVisible(driver, By.xpath("//button[text() = 'Add notification']")).click();
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
