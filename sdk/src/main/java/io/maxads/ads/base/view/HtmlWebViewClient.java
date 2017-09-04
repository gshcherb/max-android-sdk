package io.maxads.ads.base.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.WebViewClient;

public class HtmlWebViewClient extends WebViewClient {
  @NonNull private final Context mContext;

  public HtmlWebViewClient(@NonNull Context context) {
    mContext = context;
  }
}
