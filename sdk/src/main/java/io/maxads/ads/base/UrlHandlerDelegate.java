package io.maxads.ads.base;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import io.maxads.ads.base.util.IntentHandlerDelegate;

public class UrlHandlerDelegate {

  @NonNull private final IntentHandlerDelegate mIntentHandlerDelegate;

  public UrlHandlerDelegate(@NonNull Context context) {
    mIntentHandlerDelegate = new IntentHandlerDelegate(context);
  }

  public void handleUrl(@NonNull String url) {
    final Uri uri = Uri.parse(url);
    final String scheme = uri.getScheme();
    final String host = uri.getHost();
    final String uriLower = uri.toString().toLowerCase();

    // Play store deep links
    if ("play.google.com".equalsIgnoreCase(host)
      || "market.android.com".equalsIgnoreCase(host)
      || "market".equalsIgnoreCase(scheme)
      || uriLower.startsWith("play.google.com")
      || uriLower.startsWith("market.android.com/")) {
      mIntentHandlerDelegate.handleDeepLink(uri);
    }

    // In app browser
    else if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
      // open browser
    }

    // App deep links
    else if (!scheme.isEmpty()) {
      mIntentHandlerDelegate.handleDeepLink(uri);
    }
  }
}
