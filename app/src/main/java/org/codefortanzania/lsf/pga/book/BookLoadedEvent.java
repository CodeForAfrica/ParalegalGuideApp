package org.codefortanzania.lsf.pga.book;

public class BookLoadedEvent {

  private Book contents;

  public BookLoadedEvent(final Book contents) {
    this.contents = contents;
  }

  public Book getBook() {
    return this.contents;
  }
}
