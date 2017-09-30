package io.maxads.ads.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.maxads.ads.base.UrlHandlerDelegate;

public class HtmlWebViewClient extends WebViewClient {
  @NonNull private final UrlHandlerDelegate mUrlHandlerDelegate;

  public HtmlWebViewClient(@NonNull Context context) {
    mUrlHandlerDelegate = new UrlHandlerDelegate(context);
  }

  @Override
  public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
    mUrlHandlerDelegate.handleUrl(url);
    return true;
  }
}
