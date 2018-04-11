package org.codefortanzania.lsf.pga.book;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;
import org.greenrobot.eventbus.EventBus;

public class BookFragment extends Fragment {

  private static final String CONTENTS_FILE_NAME = "book/contents.json";

  private static class LoadThread extends Thread {

    private AssetManager assets;
    private BookFragment fragment;

    LoadThread(AssetManager assets, BookFragment fragment) {
      super();

      this.assets = assets;
      this.fragment = fragment;
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
      final Gson gson = new Gson();
      try {
        InputStream is = this.assets.open(CONTENTS_FILE_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        this.fragment.contents.set(gson.fromJson(reader, Book.class));
        EventBus.getDefault()
            .post(new BookLoadedEvent(this.fragment.getBook()));
      } catch (IOException e) {
        Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
      }
    }
  }

  private final AtomicReference<Book> contents = new AtomicReference<>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setRetainInstance(true);
  }

  @Override
  public void onAttach(Context host) {
    super.onAttach(host);

    if (this.contents.get() == null) {
      new LoadThread(host.getAssets(), this).start();
    }
  }

  public Book getBook() {
    return this.contents.get();
  }
}
