package org.codefortanzania.lsf.pga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import org.codefortanzania.lsf.pga.book.Contents;
import org.codefortanzania.lsf.pga.book.ContentsEntrySelectedEvent;
import org.codefortanzania.lsf.pga.book.ContentsFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Main application activity handling book's table of contents, navigation drawer as well as main
 * menu.
 */
public class MainActivity extends SingleFragmentActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private static final int ABBREVIATIONS_INDEX = 0;
  private static final int PREFACE_INDEX = 1;
  private static final int ACKNOWLEDGEMENTS_INDEX = 2;
  private static final int INTRODUCTION_INDEX = 3;
  private static final int BIBLIOGRAPHY_INDEX = 0;

  /**
   * Creates a new instance of this Activity.
   *
   * @param packageContext .
   * @param contents the application model data.
   * @return the instance.
   */
  public static Intent instance(@NonNull final Context packageContext,
      @NonNull final Contents contents) {
    final Intent intent = new Intent(packageContext, MainActivity.class);
    intent.putExtra(Contents.MODEL_NAME, contents);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.setupStrictMode();

    this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

    final DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
    final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, this.getToolbar(), R.string.navigation_drawer_open,
        R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    final NavigationView navigationView = this.findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  @Override
  protected int getContentView() {
    return R.layout.activity_main;
  }

  @Override
  protected Fragment createFragment() {
    return ContentsFragment.instance(this.getContents());
  }

  private void setupStrictMode() {
    StrictMode.ThreadPolicy.Builder builder =
        new StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog();
    if (BuildConfig.DEBUG) {
      builder.penaltyFlashScreen();
    }
    StrictMode.setThreadPolicy(builder.build());
  }

  @Override
  public void onResume() {
    super.onResume();

    EventBus.getDefault().register(this);
  }

  @SuppressWarnings("unused")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onContentsEntrySelected(final ContentsEntrySelectedEvent event) {
    this.startActivity(BookActivity.instance(this, this.getContents(), event.getNumber()));
  }

  @Override
  public void onBackPressed() {
    final DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    final Contents contents = this.getContents();
    final DrawerLayout drawer = findViewById(R.id.drawer_layout);
    switch (item.getItemId()) {
      case R.id.nav_abbreviations:
        this.startHtmlFileActivityFor(contents.frontMatter().get(ABBREVIATIONS_INDEX));
        break;
      case R.id.nav_preface:
        this.startHtmlFileActivityFor(contents.frontMatter().get(PREFACE_INDEX));
        break;
      case R.id.nav_acknowledgements:
        this.startHtmlFileActivityFor(contents.frontMatter().get(ACKNOWLEDGEMENTS_INDEX));
        break;
      case R.id.nav_introduction:
        this.startHtmlFileActivityFor(contents.frontMatter().get(INTRODUCTION_INDEX));
        break;
      case R.id.nav_bibliography:
        this.startHtmlFileActivityFor(contents.backMatter().get(BIBLIOGRAPHY_INDEX));
        break;
    }
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  private void startHtmlFileActivityFor(final Contents.Entry entry) {
    final String fileUrl = entry.file();
    final String title = entry.title();
    this.startActivity(HtmlFileActivity
        .instance(this, this.getContents(), fileUrl, title));
  }

  @Override
  public void onPause() {
    EventBus.getDefault().unregister(this);

    super.onPause();
  }
}
