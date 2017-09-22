package io.maxads.ads.banner.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface BannerPresenter {
  interface Listener {
    void onBannerLoaded(@NonNull View banner);
    void onBannerClicked();
    void onBannerError();
  }

  void setListener(@Nullable Listener listener);
  void load();
  void destroy();
}
