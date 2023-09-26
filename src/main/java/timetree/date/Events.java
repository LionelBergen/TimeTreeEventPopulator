package timetree.date;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Events {
  private static final LocalDate ROSH_HASHANAH = LocalDate.of(2024, 10, 2);

  public static List<LocalDate> GetAllEvents() {
    return Arrays.asList(ROSH_HASHANAH);
  }
}
