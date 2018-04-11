package org.codefortanzania.lsf.pga.book;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.codefortanzania.lsf.pga.ui.HtmlFileFragment;

public class BookAdapter extends FragmentStatePagerAdapter {

  private final Book book;

  public BookAdapter(final FragmentActivity context, final Book book) {
    super(context.getSupportFragmentManager());

    this.book = book;
  }

  @Override
  public Fragment getItem(int position) {
    final String path = this.book.getChapterFile(position);
    return HtmlFileFragment.instance("file:///android_asset/book/" + path);
  }

  @Override
  public int getCount() {
    return this.book.getChapterCount();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return this.book.getChapterTitle(position);
  }
}
