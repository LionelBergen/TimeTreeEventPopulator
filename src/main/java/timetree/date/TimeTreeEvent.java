package timetree.date;

import java.time.LocalDate;

public class TimeTreeEvent {
  private LocalDate start;
  private LocalDate end;
  private String titleOfEvent;

  public TimeTreeEvent(LocalDate start, LocalDate end, String titleOfEvent) {
    this.start = start;
    this.end = end;
    this.titleOfEvent = titleOfEvent;
  }

  public TimeTreeEvent(LocalDate start, String titleOfEvent) {
    this(start, null, titleOfEvent);
  }

  public LocalDate getStart() {
    return start;
  }

  public void setStart(LocalDate start) {
    this.start = start;
  }

  public LocalDate getEnd() {
    return end;
  }

  public void setEnd(LocalDate end) {
    this.end = end;
  }

  public String getTitleOfEvent() {
    return titleOfEvent;
  }

  public void setTitleOfEvent(String titleOfEvent) {
    this.titleOfEvent = titleOfEvent;
  }

  @Override
  public String toString() {
    return this.start.toString() + " - " + (this.end == null ? "null" : this.end.toString());
  }
}
