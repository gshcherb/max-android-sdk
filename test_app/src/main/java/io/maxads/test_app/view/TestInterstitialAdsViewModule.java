package io.maxads.test_app.view;

import android.support.annotation.Nullable;

public interface TestInterstitialAdsViewModule {
  void setAdItemListener(@Nullable TestAdViewHolder.Listener listener);
  void refreshTestAds();
}
