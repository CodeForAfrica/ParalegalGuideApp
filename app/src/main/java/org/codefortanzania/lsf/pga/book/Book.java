package org.codefortanzania.lsf.pga.book;

import java.util.List;

public class Book {

  static class Chapter {

    String file;
    String title;
  }

  List<Chapter> chapters;

  int getChapterCount() {
    return this.chapters.size();
  }

  String getChapterFile(int position) {
    return this.chapters.get(position).file;
  }

  String getChapterTitle(int position) {
    return this.chapters.get(position).title;
  }
}
