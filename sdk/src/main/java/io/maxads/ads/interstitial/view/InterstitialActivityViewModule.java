package io.maxads.ads.interstitial.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface InterstitialActivityViewModule {
  interface Listener {
    void onInterstitialClicked();
    void onDismissClicked();
  }

  void setListener(@Nullable Listener listener);
  void show(@NonNull String html);
}
