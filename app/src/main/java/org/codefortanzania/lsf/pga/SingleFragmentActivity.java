package org.codefortanzania.lsf.pga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import org.codefortanzania.lsf.pga.book.Contents;

/**
 * Utility activity that simplifies handling of a single fragment view.
 */
public abstract class SingleFragmentActivity extends BaseActivity {

  @Override
  public void onStart() {
    super.onStart();

    final FragmentManager fragmentManager = this.getSupportFragmentManager();
    final Fragment existingFragment
        = fragmentManager.findFragmentById(R.id.activity_fragment_container);
    if (existingFragment == null) {
      fragmentManager.beginTransaction()
          .add(R.id.activity_fragment_container, this.createFragment())
          .commit();
    } else {
      final Bundle args = this.createFragmentArguments();
      existingFragment.setArguments(args);
      fragmentManager.beginTransaction()
          .replace(R.id.activity_fragment_container, existingFragment)
          .commit();
    }
  }

  /**
   * Placeholder for overriding Activities to create the main {@link Fragment} for the activity.
   *
   * @return fragment to be attached to this activity.
   */
  protected abstract Fragment createFragment();

  /**
   * Main arguments to be passed to the main {@link Fragment} if one already exists i.e. not created
   * using {@link #createFragment()}.
   *
   * <p>By default table of contents is tracked.
   * @return arguments for the fragment.
   */
  protected Bundle createFragmentArguments() {
    final Bundle args = new Bundle();
    args.putParcelable(Contents.MODEL_NAME, this.getContents());
    return args;
  }
}
