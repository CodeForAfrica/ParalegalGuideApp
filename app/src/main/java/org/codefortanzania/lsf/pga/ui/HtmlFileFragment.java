package org.codefortanzania.lsf.pga.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class HtmlFileFragment extends WebViewFragment {

  private static final String KEY_FILE = "file";

  public static HtmlFileFragment instance(final String file) {
    final Bundle args = new Bundle();
    args.putString(KEY_FILE, file);
    final HtmlFileFragment fragment = new HtmlFileFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setRetainInstance(true);
  }

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View view
        = super.onCreateView(inflater, container, savedInstanceState);

    final WebView webView = this.getWebView();
    final WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setSupportZoom(true);
    webSettings.setBuiltInZoomControls(true);

    final String url = this.getArguments().getString(KEY_FILE);
    webView.loadUrl(url);
    return view;
  }
}
