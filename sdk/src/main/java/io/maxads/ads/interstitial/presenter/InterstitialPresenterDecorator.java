package io.maxads.ads.interstitial.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;

public class InterstitialPresenterDecorator implements InterstitialPresenter, InterstitialPresenter.Listener {
  @NonNull private final InterstitialPresenter mInterstitialPresenter;
  @NonNull private final AdTrackingDelegate mAdTrackingDelegate;
  @NonNull private final InterstitialPresenter.Listener mListener;

  public InterstitialPresenterDecorator(@NonNull InterstitialPresenter interstitialPresenter,
                                        @NonNull AdTrackingDelegate adTrackingDelegate,
                                        @NonNull InterstitialPresenter.Listener listener) {
    mInterstitialPresenter = interstitialPresenter;
    mAdTrackingDelegate = adTrackingDelegate;
    mListener = listener;
  }

  @Override
  public void setListener(@Nullable InterstitialPresenter.Listener listener) {
    // We set the listener in the constructor instead
  }

  @NonNull
  @Override
  public Ad getAd() {
    return mInterstitialPresenter.getAd();
  }

  @Override
  public void load() {
    mInterstitialPresenter.load();
  }

  @Override
  public void show() {
    mInterstitialPresenter.show();
  }

  @Override
  public void destroy() {
    mInterstitialPresenter.destroy();
  }

  @Override
  public void onInterstitialLoaded(@NonNull InterstitialPresenter interstitialPresenter) {
    mListener.onInterstitialLoaded(interstitialPresenter);
  }

  @Override
  public void onInterstitialShown(@NonNull InterstitialPresenter interstitialPresenter) {
    mAdTrackingDelegate.trackImpression();

    mListener.onInterstitialShown(interstitialPresenter);
  }

  @Override
  public void onInterstitialClicked(@NonNull InterstitialPresenter interstitialPresenter) {
    mAdTrackingDelegate.trackClick();

    mListener.onInterstitialClicked(interstitialPresenter);
  }

  @Override
  public void onInterstitialDismissed(@NonNull InterstitialPresenter interstitialPresenter) {
    mListener.onInterstitialDismissed(interstitialPresenter);
  }

  @Override
  public void onInterstitialError(@NonNull InterstitialPresenter interstitialPresenter) {
    mListener.onInterstitialError(interstitialPresenter);
  }
}
