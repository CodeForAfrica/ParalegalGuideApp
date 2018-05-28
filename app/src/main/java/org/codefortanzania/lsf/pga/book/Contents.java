package org.codefortanzania.lsf.pga.book;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Contents implements Parcelable {

  public static final String FILE_LOCATION = "book/contents.json";
  public static final String MODEL_NAME
      = "org.codefortanzania.lsf.pga.book.contents";
  public static final Creator<Contents> CREATOR = new Creator<Contents>() {
    @Override
    public Contents createFromParcel(Parcel in) {
      return new Contents(in);
    }

    @Override
    public Contents[] newArray(int size) {
      return new Contents[size];
    }
  };
  private String title;
  private List<Entry> frontMatter;
  private List<IconifiedEntry> body;
  private List<Entry> backMatter;

  private Contents(@NonNull String title, @NonNull List<Entry> frontMatter,
      @NonNull List<IconifiedEntry> body, @NonNull List<Entry> backMatter) {
    this.title = title;
    this.frontMatter = new ArrayList<>(frontMatter);
    this.body = new ArrayList<>(body);
    this.backMatter = new ArrayList<>(backMatter);
  }

  private Contents(final Parcel in) {
    this.title = in.readString();
    this.frontMatter = in.createTypedArrayList(Entry.CREATOR);
    this.body = in.createTypedArrayList(IconifiedEntry.CREATOR);
    this.backMatter = in.createTypedArrayList(Entry.CREATOR);
  }

  public String title() {
    return this.title;
  }

  public List<Entry> frontMatter() {
    return Collections.unmodifiableList(this.frontMatter);
  }

  public List<IconifiedEntry> body() {
    return Collections.unmodifiableList(this.body);
  }

  public List<Entry> backMatter() {
    return Collections.unmodifiableList(this.backMatter);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.title);
    dest.writeTypedList(this.frontMatter);
    dest.writeTypedList(this.body);
    dest.writeTypedList(this.backMatter);
  }

  public Contents filter(@NonNull final List<String> files) {
    final List<IconifiedEntry> filteredBody = new ArrayList<>(files.size());
    for (final IconifiedEntry entry : this.body) {
      if (files.contains(entry.file())) {
        filteredBody.add(entry);
      }
    }
    return new Contents(this.title, this.frontMatter, filteredBody, this.backMatter);
  }

  public static class Entry implements Parcelable {

    public static final String MODEL_NAME
        = "org.codefortanzania.lsf.pga.book.contents.content";

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
      @Override
      public Entry createFromParcel(Parcel in) {
        return new Entry(in);
      }

      @Override
      public Entry[] newArray(int size) {
        return new Entry[size];
      }
    };

    private String file;
    private String number;
    private String title;

    protected Entry(final String file, String number, String title) {
      this.file = file;
      this.number = number;
      this.title = title;
    }

    private Entry(@NonNull final Parcel in) {
      this.file = in.readString();
      this.number = in.readString();
      this.title = in.readString();
    }

    public static Entry instance(@NonNull final String file,
        @NonNull final String number, @NonNull final String title) {
      return new Entry(file, number, title);
    }

    public String file() {
      return this.file;
    }

    public String number() {
      return this.number;
    }

    public String title() {
      return this.title;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.file);
      dest.writeString(this.number);
      dest.writeString(this.title);
    }

    @Override
    public int describeContents() {
      return 0;
    }
  }

  public static class IconifiedEntry extends Entry {

    public static final Creator<IconifiedEntry> CREATOR = new Creator<IconifiedEntry>() {
      @Override
      public IconifiedEntry createFromParcel(Parcel in) {
        return new IconifiedEntry(in);
      }

      @Override
      public IconifiedEntry[] newArray(int size) {
        return new IconifiedEntry[size];
      }
    };

    private String icon;

    private IconifiedEntry(final String file, final String number,
        final String title, final String icon) {
      super(file, number, title);
      this.icon = icon;
    }

    private IconifiedEntry(@NonNull final Parcel in) {
      super(in);
      this.icon = in.readString();
    }

    public static IconifiedEntry instance(@NonNull final String file,
        @NonNull final String number, @NonNull final String title,
        @NonNull final String icon) {
      return new IconifiedEntry(file, number, title, icon);
    }

    public String icon() {
      return this.icon;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      super.writeToParcel(dest, flags);
      dest.writeString(this.icon);
    }

    @Override
    public int describeContents() {
      return 0;
    }
  }
}
