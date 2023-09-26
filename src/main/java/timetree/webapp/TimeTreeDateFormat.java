package timetree.webapp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class TimeTreeDateFormat {
  public static final DateTimeFormatter TIMETREE_CURRENT_CALENDAR_DATE_FORMATTER =
      new DateTimeFormatterBuilder()
          .appendPattern("yyyy-MM")
          .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
          .toFormatter();

  public static LocalDate ParseDateElementValue(String value) {
    return LocalDate.parse(value, TIMETREE_CURRENT_CALENDAR_DATE_FORMATTER);
  }

  public static String GetFormatForDateCell(LocalDate date) {
    return date.getYear()
        + "-"
        + GetValueWithZero(date.getMonth().getValue())
        + "-"
        + GetValueWithZero(date.getDayOfMonth())
        + "']";
  }

  private static String GetValueWithZero(int value) {
    String result = "";

    if (value <= 9) {
      result += "0";
    }

    return result + value;
  }
}
