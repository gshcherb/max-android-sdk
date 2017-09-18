package io.maxads.ads.banner.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface MraidBannerViewModule {
  interface Listener {
    void onLoaded(@NonNull BannerAdView bannerAdView);
    void onExpanded(@NonNull BannerAdView bannerAdView);
  }

  void setListener(@Nullable Listener listener);
  void show(@NonNull String html, @NonNull String[] supportedNativeFeatures);
  void destroy();
}
