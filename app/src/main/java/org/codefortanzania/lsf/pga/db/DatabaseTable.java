package org.codefortanzania.lsf.pga.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.codefortanzania.lsf.pga.book.Contents;
import org.codefortanzania.lsf.pga.book.Contents.Entry;

/**
 * .
 */
public class DatabaseTable {

  private static final String TAG = "ParalegalGuideDatabase";
  private static final String DATABASE_NAME = "paralegal_guide.db";
  private static final String TABLE_NAME = "guide_file";
  private static final String TABLE_COL_FILE_NAME = "file_name";
  private static final String TABLE_COL_FILE_CONTENTS = "file_contents";
  private static final int DATABASE_VERSION = 1;

  private final DatabaseOpenHelper databaseOpenHelper;

  public DatabaseTable(Context context) {
    this.databaseOpenHelper = new DatabaseOpenHelper(context);
  }

  /**
   * This method may take a long time to return, so you should not call it from the application main
   * thread.
   *
   * @return contents.
   */
  public Contents currentContents() {
    return this.databaseOpenHelper.currentContents();
  }

  @Nullable
  public Cursor findChapterFilesMatching(final String query) {
    final String[] selectionArgs = new String[]{query + "*"};

    return this.query(selectionArgs);
  }

  @Nullable
  private Cursor query(String[] selectionArgs) {
    final String selection = TABLE_COL_FILE_CONTENTS + " MATCH ?";
    final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
    builder.setTables(TABLE_NAME);

    final Cursor cursor = builder.query(this.databaseOpenHelper.getReadableDatabase(),
        null, selection, selectionArgs, null, null, null);
    if (cursor == null) {
      return null;
    } else if (!cursor.moveToFirst()) {
      cursor.close();
      return null;
    }
    return cursor;
  }

  private static class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String CREATE_VIRTUAL_TABLE_USING_FTS =
        "CREATE VIRTUAL TABLE " + TABLE_NAME + " USING fts3 (" +
            TABLE_COL_FILE_NAME + ", " + TABLE_COL_FILE_CONTENTS + ")";
    private final Context context;
    private SQLiteDatabase database;
    private Contents contents;

    DatabaseOpenHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
      this.context = context;
      this.contents = this.readContents();
    }

    private Contents readContents() {
      final Gson gson = new Gson();
      try {
        final BufferedReader contentsReader = new BufferedReader(
            new InputStreamReader(this.context.getAssets().open(Contents.FILE_LOCATION)));
        final Contents contents = gson.fromJson(contentsReader, Contents.class);
        contentsReader.close();
        return contents;
      } catch (final IOException e) {
        Log.e(TAG, "exception loading contents", e);
        throw new IllegalStateException("exception loading contents", e);
      }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      this.database = db;
      this.database.execSQL(CREATE_VIRTUAL_TABLE_USING_FTS);
      this.loadGuide();
    }

    private void loadGuide() {
      final AssetManager assetManager = this.context.getAssets();
      try {
        for (final Entry entry : this.contents.body()) {
          final String chapterFile = entry.file();
          final BufferedReader reader = new BufferedReader(
              new InputStreamReader(assetManager.open(chapterFile)));
          final StringBuilder builder = new StringBuilder();
          String currentLine = reader.readLine();
          while (currentLine != null) {
            builder.append(currentLine);
            builder.append("n");
            currentLine = reader.readLine();
          }
          reader.close();
          final String chapterContents = builder.toString();
          long id = addChapter(chapterFile, chapterContents);
          if (id < 0) {
            Log.e(TAG, "unable to add file: " + chapterFile);
          }
        }
      } catch (final IOException e) {
        Log.e(TAG, "exception loading guide", e);
        throw new IllegalStateException("exception loading guide", e);
      }
    }

    private long addChapter(String chapterFile, String chapterContents) {
      final ContentValues initialValues = new ContentValues();
      initialValues.put(TABLE_COL_FILE_NAME, chapterFile);
      initialValues.put(TABLE_COL_FILE_CONTENTS, chapterContents);

      return this.database.insert(TABLE_NAME, null, initialValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
          + newVersion + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

      this.onCreate(db);
    }

    /**
     * This method may take a long time to return, so you should not call it from the application
     * main thread.
     *
     * @return contents.
     */
    Contents currentContents() {
      // Ensure we have a searchable database
      this.getWritableDatabase();

      return this.contents;
    }
  }
}
