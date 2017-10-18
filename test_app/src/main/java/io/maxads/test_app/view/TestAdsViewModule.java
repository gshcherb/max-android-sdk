package io.maxads.test_app.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.banner.view.BannerAdView;

public interface TestAdsViewModule {
  void setAdItemListener(@Nullable TestAdViewHolder.Listener listener);
  void setAdViewListener(@Nullable BannerAdView.Listener listener);
  void refreshTestAds();
  void loadTestAd(@NonNull String adUnitId);
}
