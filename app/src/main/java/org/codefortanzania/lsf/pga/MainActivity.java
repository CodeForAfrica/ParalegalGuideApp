package org.codefortanzania.lsf.pga;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import io.karim.MaterialTabs;
import org.codefortanzania.lsf.pga.book.Book;
import org.codefortanzania.lsf.pga.book.BookAdapter;
import org.codefortanzania.lsf.pga.book.BookFragment;
import org.codefortanzania.lsf.pga.book.BookLoadedEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private static final String MODEL = "book";
  private ViewPager pager;
  private BookAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.activity_main);

    setupStrictMode();
    this.pager = findViewById(R.id.pager);

    final Toolbar toolbar = this.findViewById(R.id.toolbar);
    this.setSupportActionBar(toolbar);
    ActionBar actionbar = getSupportActionBar();
    actionbar.setDisplayHomeAsUpEnabled(true);
    actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

    final DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
    final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open,
        R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    final NavigationView navigationView = this.findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  @Override
  public void onStart() {
    super.onStart();

    EventBus.getDefault().register(this);
    if (this.adapter == null) {
      final FragmentManager fragmentManager = this.getSupportFragmentManager();
      final BookFragment bookFragment
          = (BookFragment) fragmentManager.findFragmentByTag(MODEL);
      if (bookFragment == null) {
        fragmentManager.beginTransaction()
            .add(new BookFragment(), MODEL)
            .commit();
      } else if (bookFragment.getBook() != null) {
        this.setupPager(bookFragment.getBook());
      }
    }
  }

  @Override
  public void onStop() {
    EventBus.getDefault().unregister(this);

    super.onStop();
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
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    this.getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();
    final DrawerLayout drawer = findViewById(R.id.drawer_layout);
    if (id == R.id.nav_bookmark) {
      Snackbar
          .make(drawer, "BOOKMARKS", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
    } else if (id == R.id.nav_settings) {
      Snackbar
          .make(drawer, "SETTINGS", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
    } else if (id == R.id.nav_help) {
      Snackbar
          .make(drawer, "HELP", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
    }
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @SuppressWarnings("unused")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onBookLoaded(final BookLoadedEvent event) {
    setupPager(event.getBook());
  }

  private void setupPager(final Book contents) {
    this.adapter = new BookAdapter(this, contents);
    this.pager.setAdapter(this.adapter);

    final MaterialTabs tabs = findViewById(R.id.tabs);
    tabs.setViewPager(pager);
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
}
