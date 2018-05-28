package org.codefortanzania.lsf.pga;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import com.google.firebase.analytics.FirebaseAnalytics;
import org.codefortanzania.lsf.pga.book.Contents;
import org.codefortanzania.lsf.pga.book.Contents.Entry;

/**
 * Base activity for all activities in the app.
 *
 * <p>Responsible for:<ul>
 *   <li>Handling saving and loading of {@link Contents} as well as current chapter number</li>
 *   <li>Initializing FireBase analytics</li>
 *   <li>Navigating back/up to the {@link MainActivity}</li>
 *   <li>Animations when transiting between activities</li>
 *   </ul>
 */
public abstract class BaseActivity extends AppCompatActivity {

  private static final String MISSING_BUNDLE_MSG = "bundle may not be null";
  private static final String MISSING_CONTENTS_MSG = "contents may not be null";

  private Contents contents;
  private String chapterNumber;
  private Toolbar toolbar;
  private FirebaseAnalytics analytics;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState != null) {
      this.initializeFromBundle(savedInstanceState);
    } else {
      this.initializeFromIntent();
    }
    if (this.contents == null) {
      Log.e(this.getClass().getSimpleName(), MISSING_CONTENTS_MSG);
      throw new IllegalStateException(MISSING_CONTENTS_MSG);
    }

    this.setContentView(this.getContentView());
    this.toolbar = this.findViewById(R.id.activity_toolbar);
    this.setSupportActionBar(this.toolbar);
    final ActionBar actionbar = this.getSupportActionBar();
    if (actionbar != null) {
      actionbar.setDisplayHomeAsUpEnabled(true);
    }
    this.analytics = FirebaseAnalytics.getInstance(this);
  }

  /**
   * Placeholder for overriding Activities to set their content layout view id.
   *
   * @return content view layout id
   * @see AppCompatActivity#setContentView(int)
   */
  @LayoutRes
  protected abstract int getContentView();

  /**
   * Initializes {@link AppCompatActivity}'s data model (contents and current chapter number, if
   * any) from the saved bundle.
   *
   * <p>This method is called only when there is a saved bundle.
   *
   * @param bundle .
   */
  protected void initializeFromBundle(@NonNull Bundle bundle) {
    this.contents = bundle.getParcelable(Contents.MODEL_NAME);
    this.chapterNumber = bundle.getString(Entry.MODEL_NAME);
  }

  @NonNull
  protected Bundle initializeFromIntent() {
    final Bundle bundle = this.getIntent().getExtras();
    if (bundle == null) {
      Log.e(this.getClass().getSimpleName(), MISSING_BUNDLE_MSG);
      throw new IllegalStateException(MISSING_BUNDLE_MSG);
    }
    this.initializeFromBundle(bundle);
    return bundle;
  }

  @NonNull
  protected Contents getContents() {
    return this.contents;
  }

  @Nullable
  protected String getChapterNumber() {
    return this.chapterNumber;
  }

  protected Toolbar getToolbar() {
    return this.toolbar;
  }

  protected FirebaseAnalytics analytics() {
    return this.analytics;
  }

  /**
   * The save/restore is too complex to keep up. Just ensure the contents are
   * passed around to each activity.
   *
   * @param item .
   * @return .
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        final Intent intent
            = MainActivity.instance(this, this.contents);
        NavUtils.navigateUpTo(this, intent);
        this.finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void startActivity(Intent intent) {
    super.startActivity(intent);
    this.overridePendingTransitionEnter();
  }

  /**
   * Overrides the pending Activity transition by performing the "Enter" animation.
   */
  protected void overridePendingTransitionEnter() {
    this.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putParcelable(Contents.MODEL_NAME, this.contents);
    if (this.chapterNumber != null) {
      outState.putString(Contents.Entry.MODEL_NAME, this.chapterNumber);
    }

    super.onSaveInstanceState(outState);
  }

  @Override
  public void finish() {
    // Activities with WebView with zoom controls "leak" these controls when
    // user navigates away from the activity.
    // see: https://stackoverflow.com/questions/27254570/android-view-windowleaked-activity-has-leaked-window-android-widget-zoombuttons
    super.finish();
    this.overridePendingTransitionExit();
  }

  /**
   * Overrides the pending Activity transition by performing the "Exit" animation.
   */
  protected void overridePendingTransitionExit() {
    this.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
  }
}
