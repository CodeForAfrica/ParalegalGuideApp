package org.codefortanzania.lsf.pga.book;

public class ContentsLoadedEvent {

  private Contents contents;

  public ContentsLoadedEvent(final Contents contents) {
    this.contents = contents;
  }

  public Contents contents() {
    return this.contents;
  }
}
