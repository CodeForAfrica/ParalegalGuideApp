package org.codefortanzania.lsf.pga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import org.codefortanzania.lsf.pga.book.Contents;
import org.codefortanzania.lsf.pga.ui.HtmlFileFragment;

/**
 * Helper Activity to display html file give as a file path (assumes the path starts from assets
 * folder).
 */
public class HtmlFileActivity extends SingleFragmentActivity {

  private static final String PAGE_NAME = "org.codefortanzania.lsf.pga.page";
  private static final String MISSING_FILE_URL_MSG = "fileUrl may not be null";
  private String fileUrl;
  private String title;

  /**
   * Creates a new instance of this Activity.
   *
   * @param packageContext .
   * @param contents the application model data.
   * @param fileUrl the specific page file url to load.
   * @param title optional title to display on Activity bar.
   * @return the instance.
   */
  public static Intent instance(@NonNull final Context packageContext,
      @NonNull Contents contents, @NonNull final String fileUrl,
      @Nullable String title) {
    final Intent intent = new Intent(packageContext, HtmlFileActivity.class);
    intent.putExtra(Contents.MODEL_NAME, contents);
    intent.putExtra(HtmlFileFragment.URL_NAME, fileUrl);
    if (title != null) {
      intent.putExtra(PAGE_NAME, title);
    }
    return intent;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState != null) {
      this.initializeFromBundle(savedInstanceState);
    } else {
      this.initializeFromIntent();
    }
    if (this.fileUrl == null) {
      Log.e(this.getClass().getSimpleName(), MISSING_FILE_URL_MSG);
      throw new IllegalStateException(MISSING_FILE_URL_MSG);
    }
  }

  @Override
  protected int getContentView() {
    return R.layout.activity_html_file;
  }

  @Override
  protected void initializeFromBundle(@NonNull Bundle bundle) {
    super.initializeFromBundle(bundle);

    this.fileUrl = bundle.getString(HtmlFileFragment.URL_NAME);
    this.title = bundle.getString(PAGE_NAME);
  }

  @Override
  protected Fragment createFragment() {
    return HtmlFileFragment.instance(this.fileUrl);
  }

  @Override
  protected Bundle createFragmentArguments() {
    final Bundle args = super.createFragmentArguments();
    args.putString(HtmlFileFragment.URL_NAME, this.fileUrl);
    return args;
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (this.title != null) {
      this.updateTitle(this.title);
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    outState.putString(HtmlFileFragment.URL_NAME, this.fileUrl);
    if (this.title != null) {
      outState.putString(PAGE_NAME, this.title);
    }

    super.onSaveInstanceState(outState);
  }
}
