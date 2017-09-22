package io.maxads.ads.banner.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.banner.presenter.BannerPresenter;

public interface MraidBannerViewModule {
  void setListener(@Nullable BannerPresenter.Listener listener);
  void load(@NonNull String html, @NonNull String[] supportedNativeFeatures);
  void destroy();
}
