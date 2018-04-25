package org.codefortanzania.lsf.pga.book;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import org.codefortanzania.lsf.pga.R;

/**
 * Creates views for all contents' body entries on the contents list.
 */
public class ContentsBodyEntriesAdapter
    extends RecyclerView.Adapter<ContentsIconifiedEntryViewHolder> {

  private static final String ICON_TYPE = "string";

  private final FragmentActivity context;
  private final List<Contents.IconifiedEntry> body;

  ContentsBodyEntriesAdapter(final FragmentActivity context,
      final List<Contents.IconifiedEntry> body) {
    this.context = context;
    this.body = body;
  }


  @NonNull
  @Override
  public ContentsIconifiedEntryViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    final LayoutInflater layoutInflater = LayoutInflater.from(this.context);
    final View view = layoutInflater
        .inflate(R.layout.view_contents_iconified_entry, parent, false);
    return new ContentsIconifiedEntryViewHolder(view);
  }

  @Override
  public void onBindViewHolder(
      @NonNull ContentsIconifiedEntryViewHolder holder, int position) {
    holder.setEntry(this.context, this.body.get(position));
  }

  @Override
  public int getItemCount() {
    return this.body.size();
  }
}
