package org.codefortanzania.lsf.pga;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.codefortanzania.lsf.pga.book.Contents;
import org.codefortanzania.lsf.pga.book.ContentsEntrySelectedEvent;
import org.codefortanzania.lsf.pga.book.ContentsFragment;
import org.codefortanzania.lsf.pga.db.DatabaseTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * .
 */
public class SearchableActivity extends SingleFragmentActivity implements
    SearchView.OnQueryTextListener {

  private DatabaseTable db;
  private String query = "";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    this.handleIntent();

    super.onCreate(savedInstanceState);

    this.setTitle(this.query);
  }

  @Override
  protected Fragment createFragment() {
    return ContentsFragment.instance(this.getContents());
  }

  @Override
  protected int getContentView() {
    return R.layout.activity_searchable;
  }

  private void handleIntent() {
    final Intent intent = getIntent();
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      this.query = intent.getStringExtra(SearchManager.QUERY);
      Log.e(this.getClass().getSimpleName(), this.query);
      this.db = new DatabaseTable(this);
      final Cursor cursor = this.db.findChapterFilesMatching(this.query);
      final List<String> foundInChapters;
      if (cursor == null || !cursor.moveToFirst()) {
        Log.e(this.getClass().getSimpleName(), "Found: 0");
        foundInChapters = Collections.emptyList();
      } else {
        Log.e(this.getClass().getSimpleName(), "Found: " + cursor.getCount());
        foundInChapters = new ArrayList<>(cursor.getCount());
        boolean hasNext = true;
        while (hasNext) {
          foundInChapters.add(cursor.getString(0)); // 1st column, file_name
          hasNext = cursor.moveToNext();
        }
      }
      final Contents contents = this.db.currentContents().filter(foundInChapters);
      intent.putExtra(Contents.MODEL_NAME, contents);
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    EventBus.getDefault().register(this);
    this.setTitle(this.query);
  }

  @SuppressWarnings("unused")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onContentsEntrySelected(final ContentsEntrySelectedEvent event) {
    this.startActivity(BookActivity.instance(this, this.db.currentContents(), event.getNumber()));
  }

  @Override
  public void onPause() {
    EventBus.getDefault().unregister(this);

    super.onPause();
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    return false;
  }
}
