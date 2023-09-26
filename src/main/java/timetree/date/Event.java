package timetree.date;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Event {
  private static final LocalDate ROSH_HASHANAH = LocalDate.of(2024, 10, 2);

  public static List<LocalDate> GetAllDates() {
    return Arrays.asList(ROSH_HASHANAH);
  }
}
