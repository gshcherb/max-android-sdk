package io.maxads.ads.base;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

import io.maxads.ads.base.util.IntentHandlerDelegate;
import io.maxads.ads.base.util.MaxAdsLog;

public class UrlHandlerDelegate {
  @NonNull private static final String TAG = UrlHandlerDelegate.class.getSimpleName();
  @NonNull private final IntentHandlerDelegate mIntentHandlerDelegate;

  public UrlHandlerDelegate(@NonNull Context context) {
    mIntentHandlerDelegate = new IntentHandlerDelegate(context);
  }

  /**
   * https://developer.android.com/distribute/marketing-tools/linking-to-google-play.html
   */
  public void handleUrl(@Nullable String url) {
    if (url == null) {
      return;
    }

    // TODO (steffan): follow redirects first before handling url
    MaxAdsLog.d(TAG, "Handling url: " + url);

    final Uri uri = Uri.parse(url);
    final String scheme = uri.getScheme();
    final String host = uri.getHost();
    final String uriLower = uri.toString().toLowerCase(Locale.ROOT);

    // NOTE: currently these all handle the same, but we might want different behavior in the future

    // Play store deep links
    if ("play.google.com".equalsIgnoreCase(host)
      || "market.android.com".equalsIgnoreCase(host)
      || "market".equalsIgnoreCase(scheme)
      || uriLower.startsWith("play.google.com")
      || uriLower.startsWith("market.android.com/")) {
      mIntentHandlerDelegate.handleDeepLink(uri);
    }

    // Device browser
    else if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
      mIntentHandlerDelegate.handleDeepLink(uri);
    }

    // App deep links
    else if (!scheme.isEmpty()) {
      mIntentHandlerDelegate.handleDeepLink(uri);
    }
  }
}
