package org.codefortanzania.lsf.pga;

import android.content.res.AssetManager;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.codefortanzania.lsf.pga.book.Contents;
import org.codefortanzania.lsf.pga.book.ContentsLoadedEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Activity responsible for showing the splash screen while the app is loading book's table of
 * contents in the background.
 */
public class SplashActivity extends AppCompatActivity {

  @Override
  public void onStart() {
    super.onStart();

    EventBus.getDefault().register(this);
    new BookLoaderThread(this.getAssets()).start();
  }

  @SuppressWarnings("unused")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onContentsLoaded(final ContentsLoadedEvent event) {
    this.startActivity(MainActivity.instance(this, event.contents()));
    this.finish();
  }

  @Override
  public void onStop() {
    EventBus.getDefault().unregister(this);

    super.onStop();
  }

  private static class BookLoaderThread extends Thread {

    private AssetManager assets;

    BookLoaderThread(@NonNull final AssetManager assets) {
      super();

      this.assets = assets;
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
      final Gson gson = new Gson();
      try {
        final InputStream is = this.assets.open(Contents.FILE_LOCATION);
        final BufferedReader reader
            = new BufferedReader(new InputStreamReader(is));
        final Contents contents = gson.fromJson(reader, Contents.class);
        EventBus.getDefault().post(new ContentsLoadedEvent(contents));
      } catch (IOException e) {
        Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
      }
    }
  }
}
