package org.codefortanzania.lsf.pga.book;

/**
 * Event representing single entry selection on contents entry list.
 */
public class ContentsEntrySelectedEvent {

  private final int position;
  private final String number;
  private final String title;

  ContentsEntrySelectedEvent(final int position, final String number,
      final String title) {
    this.position = position;
    this.number = number;
    this.title = title;
  }

  public int getPosition() {
    return this.position;
  }

  public String getNumber() {
    return this.number;
  }

  public String getTitle() {
    return this.title;
  }
}
