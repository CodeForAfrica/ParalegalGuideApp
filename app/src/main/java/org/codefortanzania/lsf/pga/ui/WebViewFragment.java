/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codefortanzania.lsf.pga.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * A fragment that displays a WebView. <p> The WebView is automatically paused and/or resumed when
 * the Fragment is paused or resumed.
 */
public class WebViewFragment extends Fragment {

  private WebView webView;

  /**
   * Called to instantiate the view. Creates and returns the WebView.
   */
  @NonNull
  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
      final ViewGroup container, final Bundle savedInstanceState) {
    if (this.webView != null) {
      this.webView.destroy();
      this.webView = null;
    }
    this.webView = this.createWebView();
    if (this.webView == null) {
      throw new IllegalStateException("createWebView() may not return null");
    }
    return this.webView;
  }

  /**
   * Point for child Fragments to instantiate their own web view (if desired).
   *
   * @return new web view instance.
   */
  protected WebView createWebView() {
    return new WebView(this.getContext());
  }

  /**
   * Called when the fragment is visible to the user and actively running. Resumes the WebView.
   */
  @Override
  public void onResume() {
    this.webView.onResume();
    super.onResume();
  }

  /**
   * Called when the fragment is no longer resumed. Pauses the WebView.
   */
  @Override
  public void onPause() {
    super.onPause();
    this.webView.onPause();
  }

  /**
   * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
   */
  @Override
  public void onDestroy() {
    if (this.webView != null) {
      this.webView.destroy();
      this.webView = null;
    }
    super.onDestroy();
  }
}
