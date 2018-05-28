package org.codefortanzania.lsf.pga;

import android.content.Context;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import org.codefortanzania.lsf.pga.book.Contents;
import org.codefortanzania.lsf.pga.book.ContentsLoadedEvent;
import org.codefortanzania.lsf.pga.db.DatabaseTable;
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
    new BookLoaderThread(this).start();
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

    private Context context;

    BookLoaderThread(@NonNull final Context context) {
      super();

      this.context = context;
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
      final DatabaseTable db = new DatabaseTable(this.context);
      final Contents contents = db.currentContents();
      EventBus.getDefault().post(new ContentsLoadedEvent(contents));
    }
  }
}
