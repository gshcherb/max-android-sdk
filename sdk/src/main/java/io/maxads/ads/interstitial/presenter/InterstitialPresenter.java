package io.maxads.ads.interstitial.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.model.Ad;

public interface InterstitialPresenter {
  interface Listener {
    void onInterstitialLoaded(@NonNull InterstitialPresenter interstitialPresenter);
    void onInterstitialShown(@NonNull InterstitialPresenter interstitialPresenter);
    void onInterstitialClicked(@NonNull InterstitialPresenter interstitialPresenter);
    void onInterstitialDismissed(@NonNull InterstitialPresenter interstitialPresenter);
    void onInterstitialError(@NonNull InterstitialPresenter interstitialPresenter);
  }

  void setListener(@Nullable Listener listener);
  @NonNull Ad getAd();
  void load();
  void show();
  void destroy();
}
