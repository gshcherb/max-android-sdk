package io.maxads.ads.interstitial.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.MaxAdsLog;

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
    MaxAdsLog.d("Loading interstitial presenter for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackSelected();
    mInterstitialPresenter.load();
  }

  @Override
  public void show() {
    MaxAdsLog.d("Showing interstitial presenter for ad unit id: " + getAd().getAdUnitId());
    mInterstitialPresenter.show();
  }

  @Override
  public void destroy() {
    MaxAdsLog.d("Destroying interstitial presenter for ad unit id: " + getAd().getAdUnitId());
    mInterstitialPresenter.destroy();
  }

  @Override
  public void onInterstitialLoaded(@NonNull InterstitialPresenter interstitialPresenter) {
    MaxAdsLog.d("Interstitial loaded for ad unit id: " + getAd().getAdUnitId());
    mListener.onInterstitialLoaded(interstitialPresenter);
  }

  @Override
  public void onInterstitialShown(@NonNull InterstitialPresenter interstitialPresenter) {
    MaxAdsLog.d("Interstitial shown for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackImpression();
    mListener.onInterstitialShown(interstitialPresenter);
  }

  @Override
  public void onInterstitialClicked(@NonNull InterstitialPresenter interstitialPresenter) {
    MaxAdsLog.d("Interstitial clicked for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackClick();
    mListener.onInterstitialClicked(interstitialPresenter);
  }

  @Override
  public void onInterstitialDismissed(@NonNull InterstitialPresenter interstitialPresenter) {
    MaxAdsLog.d("Interstitial dismissed for ad unit id: " + getAd().getAdUnitId());
    mListener.onInterstitialDismissed(interstitialPresenter);
  }

  @Override
  public void onInterstitialError(@NonNull InterstitialPresenter interstitialPresenter) {
    MaxAdsLog.d("Interstitial error for ad unit id: " + getAd().getAdUnitId());
    mListener.onInterstitialError(interstitialPresenter);
  }
}
