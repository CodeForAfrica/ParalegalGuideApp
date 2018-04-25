package org.codefortanzania.lsf.pga.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Loads and displays a given html file for assets folder.
 */
public class HtmlFileFragment extends WebViewFragment {

  public static final String ASSETS_URL = "file:///android_asset/";
  public static final String URL_NAME = "org.codefortanzania.lsf.pga.ui.file";
  private static final String MISSING_FILE_URL_MSG = "fileUrl may not be null";
  String fileUrl;
  String title;

  public static HtmlFileFragment instance(final String file) {
    final Bundle args = new Bundle();
    args.putString(URL_NAME, file);
    final HtmlFileFragment fragment = new HtmlFileFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.fileUrl = this.getFileUrlFromArguments();
    if (this.fileUrl == null) {
      throw new IllegalStateException(MISSING_FILE_URL_MSG);
    }
    this.title = "";
  }

  @Nullable
  private String getFileUrlFromArguments() {
    final Bundle arguments = this.getArguments();
    if (arguments != null) {
      return arguments.getString(URL_NAME);
    }
    return null;
  }

  public String title() {
    return this.title;
  }

  @SuppressLint("SetJavaScriptEnabled")
  @NonNull
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final WebView webView
        = (WebView) super.onCreateView(inflater, container, savedInstanceState);
    final WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setSupportZoom(true);
    webSettings.setBuiltInZoomControls(true);

    webView.loadUrl(ASSETS_URL + this.fileUrl);
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageFinished(final WebView view, final String url) {
        final String newTile = view.getTitle();
        if (newTile != null) {
          HtmlFileFragment.this.title = newTile.trim();
        }
      }
    });
    return webView;
  }
}
