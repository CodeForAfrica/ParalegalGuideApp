package org.codefortanzania.lsf.pga.book;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.List;
import org.codefortanzania.lsf.pga.book.Contents.IconifiedEntry;

/**
 * An adapter for paging book's (body text) chapters using table of contents {@link Contents#body()
 * entries}.
 */
public class ChapterAdapter extends FragmentStatePagerAdapter {

  private final List<IconifiedEntry> body;

  public ChapterAdapter(final FragmentActivity context, final Contents contents) {
    super(context.getSupportFragmentManager());

    this.body = contents.body();
  }

  @Override
  public Fragment getItem(int position) {
    return ChapterFragment.instance(this.body.get(position));
  }

  @Override
  public int getCount() {
    return this.body.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return this.body.get(position).title();
  }
}
