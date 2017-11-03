package io.maxads.ads.banner.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.Checks;
import io.maxads.ads.base.util.MaxAdsLog;

public class BannerPresenterDecorator implements BannerPresenter, BannerPresenter.Listener {
  @NonNull private static final String TAG = BannerPresenterDecorator.class.getSimpleName();
  @NonNull private final BannerPresenter mBannerPresenter;
  @NonNull private final AdTrackingDelegate mAdTrackingDelegate;
  @NonNull private final BannerPresenter.Listener mListener;
  private boolean mIsDestroyed;

  public BannerPresenterDecorator(@NonNull BannerPresenter bannerPresenter,
                                  @NonNull AdTrackingDelegate adTrackingDelegate,
                                  @NonNull BannerPresenter.Listener listener) {
    mBannerPresenter = bannerPresenter;
    mAdTrackingDelegate = adTrackingDelegate;
    mListener = listener;
  }

  @Override
  public void setListener(@Nullable Listener listener) {
    // We set the listener in the constructor instead
  }

  @NonNull
  @Override
  public Ad getAd() {
    return mBannerPresenter.getAd();
  }

  @Override
  public void load() {
    if (!Checks.NoThrow.checkArgument(!mIsDestroyed, "BannerPresenterDecorator is destroyed")) {
      return;
    }

    MaxAdsLog.d(TAG, "Loading banner presenter for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackSelected();
    mBannerPresenter.load();
  }

  @Override
  public void destroy() {
    MaxAdsLog.d(TAG, "Destroying banner presenter for ad unit id: " + getAd().getAdUnitId());
    mBannerPresenter.destroy();
    mIsDestroyed = true;
  }

  @Override
  public void onBannerLoaded(@NonNull BannerPresenter bannerPresenter, @NonNull View banner) {
    if (mIsDestroyed) {
      return;
    }

    MaxAdsLog.d(TAG, "Banner loaded for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackImpression();
    mListener.onBannerLoaded(bannerPresenter, banner);
  }

  @Override
  public void onBannerClicked(@NonNull BannerPresenter bannerPresenter) {
    if (mIsDestroyed) {
      return;
    }

    MaxAdsLog.d(TAG, "Banner clicked for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackClick();
    mListener.onBannerClicked(bannerPresenter);
  }

  @Override
  public void onBannerError(@NonNull BannerPresenter bannerPresenter) {
    if (mIsDestroyed) {
      return;
    }

    String errorMessage = "Banner error for ad unit id: " + getAd().getAdUnitId();
    MaxAdsLog.d(TAG, errorMessage);
    mAdTrackingDelegate.trackError(errorMessage);
    mListener.onBannerError(bannerPresenter);
  }
}
