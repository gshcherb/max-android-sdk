package io.maxads.ads.interstitial.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.Checks;
import io.maxads.ads.base.util.MaxAdsLog;

public class InterstitialPresenterDecorator implements InterstitialPresenter, InterstitialPresenter.Listener {
  @NonNull private static final String TAG = InterstitialPresenterDecorator.class.getSimpleName();
  @NonNull private final InterstitialPresenter mInterstitialPresenter;
  @NonNull private final AdTrackingDelegate mAdTrackingDelegate;
  @NonNull private final InterstitialPresenter.Listener mListener;
  private boolean mIsDestroyed;

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
    if (!Checks.NoThrow.checkArgument(!mIsDestroyed, "InterstitialPresenterDecorator is destroyed")) {
      return;
    }

    MaxAdsLog.d(TAG, "Loading interstitial presenter for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackSelected();
    mInterstitialPresenter.load();
  }

  @Override
  public void show() {
    if (!Checks.NoThrow.checkArgument(!mIsDestroyed, "InterstitialPresenterDecorator is destroyed")) {
      return;
    }

    MaxAdsLog.d(TAG, "Showing interstitial presenter for ad unit id: " + getAd().getAdUnitId());
    mInterstitialPresenter.show();
  }

  @Override
  public void destroy() {
    MaxAdsLog.d(TAG, "Destroying interstitial presenter for ad unit id: " + getAd().getAdUnitId());
    mInterstitialPresenter.destroy();
    mIsDestroyed = true;
  }

  @Override
  public void onInterstitialLoaded(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mIsDestroyed) {
      return;
    }

    MaxAdsLog.d(TAG, "Interstitial loaded for ad unit id: " + getAd().getAdUnitId());
    mListener.onInterstitialLoaded(interstitialPresenter);
  }

  @Override
  public void onInterstitialShown(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mIsDestroyed) {
      return;
    }

    MaxAdsLog.d(TAG, "Interstitial shown for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackImpression();
    mListener.onInterstitialShown(interstitialPresenter);
  }

  @Override
  public void onInterstitialClicked(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mIsDestroyed) {
      return;
    }

    MaxAdsLog.d(TAG, "Interstitial clicked for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackClick();
    mListener.onInterstitialClicked(interstitialPresenter);
  }

  @Override
  public void onInterstitialDismissed(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mIsDestroyed) {
      return;
    }

    MaxAdsLog.d(TAG, "Interstitial dismissed for ad unit id: " + getAd().getAdUnitId());
    mListener.onInterstitialDismissed(interstitialPresenter);
  }

  @Override
  public void onInterstitialError(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mIsDestroyed) {
      return;
    }

    String errorMessage = "Interstitial error for ad unit id: " + getAd().getAdUnitId();
    MaxAdsLog.d(TAG, errorMessage);
    mAdTrackingDelegate.trackError(errorMessage);
    mListener.onInterstitialError(interstitialPresenter);
  }
}
