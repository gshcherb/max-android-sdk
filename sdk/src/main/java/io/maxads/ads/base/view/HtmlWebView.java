package io.maxads.ads.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

public class HtmlWebView extends MaxAdsWebView {
  public HtmlWebView(@NonNull Context context) {
    this(context, null);
  }

  @SuppressLint("SetJavaScriptEnabled")
  public HtmlWebView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Disable scrolling and zoom
    setHorizontalScrollBarEnabled(false);
    setHorizontalScrollbarOverlay(false);
    setVerticalScrollBarEnabled(false);
    setVerticalScrollbarOverlay(false);
    getSettings().setSupportZoom(false);

    getSettings().setJavaScriptEnabled(true);

    enablePlugins(true);
    setBackgroundColor(Color.TRANSPARENT);
  }

  @Override
  public boolean performClick() {
    return super.performClick();
  }
}
