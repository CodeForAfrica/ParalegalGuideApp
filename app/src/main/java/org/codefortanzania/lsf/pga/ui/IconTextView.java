package org.codefortanzania.lsf.pga.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * {@code TextView} for font-icons.
 */
public class IconTextView extends AppCompatTextView {

  public static final String FONTS_ROOT = "fonts/";
  public static final String FONT_AWESOME
      = FONTS_ROOT + "Font-Awesome-5-Solid-900.otf";

  public IconTextView(Context context) {
    super(context);

    this.setAppearance();
  }

  public IconTextView(Context context, AttributeSet attrs) {
    super(context, attrs);

    this.setAppearance();
  }

  private void setAppearance() {
    this.setGravity(Gravity.CENTER);
    this.setTypeface(
        Typeface.createFromAsset(this.getContext().getAssets(), FONT_AWESOME));
  }
}
