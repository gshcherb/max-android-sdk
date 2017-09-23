package io.maxads.ads.banner.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import io.maxads.ads.base.model.Ad;

public interface BannerPresenter {
  interface Listener {
    void onBannerLoaded(@NonNull BannerPresenter bannerPresenter, @NonNull View banner);
    void onBannerClicked(@NonNull BannerPresenter bannerPresenter);
    void onBannerError(@NonNull BannerPresenter bannerPresenter);
  }

  void setListener(@Nullable Listener listener);
  @NonNull Ad getAd();
  void load();
  void destroy();
}
