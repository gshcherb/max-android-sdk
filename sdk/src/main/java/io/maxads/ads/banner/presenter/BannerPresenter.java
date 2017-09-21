package io.maxads.ads.banner.presenter;

public interface BannerPresenter {
  interface Listener {
    void onBannerLoaded();
    void onBannerClicked();
    void onBannerError();
  }

  void load();
  void destroy();
}
