package org.codefortanzania.lsf.pga.book;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import org.codefortanzania.lsf.pga.R;
import org.codefortanzania.lsf.pga.ui.IconTextView;
import org.greenrobot.eventbus.EventBus;

/**
 * Displays a single contents entry on the entry list.
 */
class ContentsIconifiedEntryViewHolder extends RecyclerView.ViewHolder
    implements OnClickListener {

  private static final String ICON_TYPE = "string";

  private final IconTextView iconView;
  private final TextView numberView;
  private final TextView titleView;

  ContentsIconifiedEntryViewHolder(@NonNull View entryView) {
    super(entryView);

    this.iconView = entryView.findViewById(R.id.entry_icon);
    this.numberView = entryView.findViewById(R.id.entry_number);
    this.titleView = entryView.findViewById(R.id.entry_title);
    entryView.setOnClickListener(this);
    this.fixClickRippleEffect(entryView);
  }

  private void fixClickRippleEffect(final View entryView) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      entryView.setOnTouchListener(
          new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
              view.findViewById(R.id.entry)
                  .getBackground()
                  .setHotspot(event.getX(), event.getY());
              return false; // Event is passed to super for 'clickable'
            }
          });
    }
  }

  void setEntry(final Context context, final Contents.IconifiedEntry entry) {
    final String packageName = context.getPackageName();
    final int iconId = context.getResources()
        .getIdentifier(entry.icon(), ICON_TYPE, packageName);
    this.iconView.setText(context.getString(iconId));
    this.numberView.setText(entry.number());
    this.titleView.setText(entry.title());
  }

  @Override
  public void onClick(final View view) {
    final int position = this.getAdapterPosition();
    final String number
        = ((TextView) view.findViewById(R.id.entry_number)).getText().toString();
    final String title
        = ((TextView) view.findViewById(R.id.entry_title)).getText().toString();
    final ContentsEntrySelectedEvent event
        = new ContentsEntrySelectedEvent(position, number, title);
    EventBus.getDefault().post(event);
  }
}
