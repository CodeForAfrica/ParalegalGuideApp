package org.codefortanzania.lsf.pga.book;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.codefortanzania.lsf.pga.R;

/**
 * Displays the book's table of contents.
 */
public class ContentsFragment extends Fragment {

  private RecyclerView chapterRecyclerView;
  private Contents contents;

  public static ContentsFragment instance(@NonNull final Contents contents) {
    final Bundle args = new Bundle();
    args.putParcelable(Contents.MODEL_NAME, contents);
    final ContentsFragment fragment = new ContentsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.contents = this.getContentsFromArguments();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View view
        = inflater.inflate(R.layout.fragment_chapter_list, container, false);
    this.chapterRecyclerView = view.findViewById(R.id.chapter_recycler_view);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    final FragmentActivity activity = this.getActivity();
    this.chapterRecyclerView
        .setLayoutManager(new LinearLayoutManager(activity));
    this.chapterRecyclerView
        .addItemDecoration(new DividerItemDecoration(activity,
            LinearLayoutManager.VERTICAL));

    final ContentsBodyEntriesAdapter contentsEAdapter
        = new ContentsBodyEntriesAdapter(this.getActivity(), this.contents.body());
    this.chapterRecyclerView.setAdapter(contentsEAdapter);
  }

  @Nullable
  private Contents getContentsFromArguments() {
    final Bundle arguments = this.getArguments();
    if (arguments != null) {
      return arguments.getParcelable(Contents.MODEL_NAME);
    }
    return null;
  }
}
