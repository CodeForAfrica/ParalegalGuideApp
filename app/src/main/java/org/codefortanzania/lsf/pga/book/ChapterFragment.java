package org.codefortanzania.lsf.pga.book;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import org.codefortanzania.lsf.pga.R;
import org.codefortanzania.lsf.pga.book.Contents.IconifiedEntry;
import org.codefortanzania.lsf.pga.ui.HtmlFileFragment;

/**
 * Given table of contents {@link IconifiedEntry entry}, displays the book's chapter using {@link
 * HtmlFileFragment}.
 */
public class ChapterFragment extends HtmlFileFragment {

  private View view;

  /**
   * Creates a new instance of this Fragment.
   *
   * @param entry given chapter's table of contents entry.
   * @return new instant.
   */
  public static ChapterFragment instance(@NonNull final IconifiedEntry entry) {
    final Bundle args = new Bundle();
    args.putString(HtmlFileFragment.URL_NAME, entry.file());
    final ChapterFragment fragment = new ChapterFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @NonNull
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    this.view
        = inflater.inflate(R.layout.fragment_book_chapter, container, false);
    super.onCreateView(inflater, container, savedInstanceState);
    return this.view;
  }

  @Override
  protected WebView createWebView() {
    return this.view.findViewById(R.id.fragment_book_chapter_web_view);
  }
}
