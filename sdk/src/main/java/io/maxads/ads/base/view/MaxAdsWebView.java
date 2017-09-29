package io.maxads.ads.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import io.maxads.ads.base.util.Views;

public class MaxAdsWebView extends WebView {
  private static boolean sDeadlockCleared = false;
  private boolean mIsDestroyed;

  public MaxAdsWebView(Context context) {
    this(context, null);
  }

  public MaxAdsWebView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Don't allow ad creatives to detect or read files on the device's filesystem
    getSettings().setAllowFileAccess(false);
    getSettings().setAllowContentAccess(false);
    getSettings().setAllowFileAccessFromFileURLs(false);
    getSettings().setAllowUniversalAccessFromFileURLs(false);

    enablePlugins(false);
    // TODO (steffan): disable JS chrome client?

    if (!sDeadlockCleared) {
      clearWebViewDeadlock(getContext());
      sDeadlockCleared = true;
    }
  }

  @Override
  public void destroy() {
    mIsDestroyed = true;

    // Fixes issue: https://code.google.com/p/android/issues/detail?id=65833.
    // https://stackoverflow.com/questions/11995270/error-webview-destroy-called-while-still-attached
    Views.removeFromParent(this);
    removeAllViews();

    super.destroy();
  }

  public void enablePlugins(boolean enabled) {
    // Plugins don't apply above Jelly Bean MR2
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return;
    }

    getSettings().setPluginState(enabled ? WebSettings.PluginState.ON : WebSettings.PluginState.OFF);
  }

  /**
   * Intended to be used with dummy WebViews to precache WebView javascript and assets.
   */
  @SuppressLint("SetJavaScriptEnabled")
  protected void enableJavascriptCaching() {
    getSettings().setJavaScriptEnabled(true);
    getSettings().setDomStorageEnabled(true);
    getSettings().setAppCacheEnabled(true);
    // Required for the Application Caches API to be enabled
    // See: http://developer.android.com/reference/android/webkit/WebSettings.html#setAppCachePath(java.lang.String)
    getSettings().setAppCachePath(getContext().getCacheDir().getAbsolutePath());
  }

  /**
   * Fixes issue: https://code.google.com/p/android/issues/detail?id=63754
   *
   * When a WebView with HTML5 video is is destroyed it can deadlock the WebView thread until another hardware
   * accelerated WebView is added to the view hierarchy and restores the GL context.
   */
  private void clearWebViewDeadlock(@NonNull Context context) {
    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT) {
      return;
    }

    // Create an invisible WebView
    final WebView webView = new WebView(context.getApplicationContext());
    webView.setBackgroundColor(Color.TRANSPARENT);

    // For the deadlock to be cleared, we must load content and add to the view hierarchy. Since
    // we don't have an activity context, we'll use a system window.
    webView.loadDataWithBaseURL(null, "", "text/html", "UTF-8", null);
    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    params.width = 1;
    params.height = 1;

    // Unlike other system window types TYPE_TOAST doesn't require extra permissions
    params.type = WindowManager.LayoutParams.TYPE_TOAST;
    params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
      | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
    params.format = PixelFormat.TRANSPARENT;
    params.gravity = Gravity.START | Gravity.TOP;

    final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    windowManager.addView(webView, params);
  }
}
