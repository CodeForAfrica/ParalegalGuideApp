package org.codefortanzania.lsf.pga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import java.util.List;
import org.codefortanzania.lsf.pga.book.ChapterAdapter;
import org.codefortanzania.lsf.pga.book.Contents;
import org.codefortanzania.lsf.pga.book.Contents.Entry;

/**
 * Displays a book, one chapter a time.
 */
public class BookActivity extends BaseActivity {

  private static final String VIEW_CHAPTER_EVENT_NAME = "view_chapter";
  private static final String VIEW_CHAPTER_EVENT_NUMBER = "chapter_number";
  private static final String VIEW_CHAPTER_EVENT_TITLE = "chapter_title";
  private ViewPager viewPager;
  private ChapterAdapter adapter;

  public static Intent instance(@NonNull final Context context,
      @NonNull final Contents contents, @Nullable final String chapterTitle) {
    final Intent intent
        = new Intent(context, BookActivity.class);
    if (chapterTitle != null && !chapterTitle.isEmpty()) {
      intent.putExtra(Contents.MODEL_NAME, contents);
      intent.putExtra(Entry.MODEL_NAME, chapterTitle);
    }
    return intent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.viewPager
        = this.findViewById(R.id.activity_book_chapter_pager_view_pager);
    this.setupViewPager();
  }

  @Override
  protected int getContentView() {
    return R.layout.activity_book_chapter_pager;
  }

  private void setupViewPager() {
    final Contents book = this.getContents();
    this.adapter = new ChapterAdapter(this, book);
    this.viewPager.setAdapter(this.adapter);
    final String chapterNumber = this.getChapterNumber();
    final int currentItem;
    if (chapterNumber == null || chapterNumber.isEmpty()) {
      currentItem = 0;
    } else {
      final List<Contents.IconifiedEntry> chapterEntries = book.body();
      int foundItem = 0;
      for (int i = 0; i < chapterEntries.size(); i++) {
        if (chapterEntries.get(i).number().equals(chapterNumber)) {
          foundItem = i;
          break;
        }
      }
      currentItem = foundItem;
    }
    this.viewPager.setCurrentItem(currentItem);
    this.viewPager.addOnPageChangeListener(new BookChapterChangeListener());
  }

  @Override
  protected void onResume() {
    super.onResume();

    this.updateTitle(this.viewPager.getCurrentItem());
  }

  private void updateTitle(int position) {
    final CharSequence title = this.adapter.getPageTitle(position);
    final Bundle params = new Bundle();
    params.putInt(VIEW_CHAPTER_EVENT_NUMBER, position + 1);
    params.putCharSequence(VIEW_CHAPTER_EVENT_TITLE, title);
    this.analytics().logEvent(VIEW_CHAPTER_EVENT_NAME, params);
    this.setTitle(title);
  }

  /**
   * Listens to page selection change and update activity's title.
   */
  private class BookChapterChangeListener implements OnPageChangeListener {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      BookActivity.this.updateTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  }
}
