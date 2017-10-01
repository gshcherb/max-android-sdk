package io.maxads.ads.banner.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.MaxAdsLog;

public class BannerPresenterDecorator implements BannerPresenter, BannerPresenter.Listener {
  @NonNull private final BannerPresenter mBannerPresenter;
  @NonNull private final AdTrackingDelegate mAdTrackingDelegate;
  @NonNull private final BannerPresenter.Listener mListener;

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
    MaxAdsLog.d("Loading banner presenter for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackSelected();
    mBannerPresenter.load();
  }

  @Override
  public void destroy() {
    MaxAdsLog.d("Destroying banner presenter for ad unit id: " + getAd().getAdUnitId());
    mBannerPresenter.destroy();
  }

  @Override
  public void onBannerLoaded(@NonNull BannerPresenter bannerPresenter, @NonNull View banner) {
    MaxAdsLog.d("Banner loaded for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackImpression();
    mListener.onBannerLoaded(bannerPresenter, banner);
  }

  @Override
  public void onBannerClicked(@NonNull BannerPresenter bannerPresenter) {
    MaxAdsLog.d("Banner clicked for ad unit id: " + getAd().getAdUnitId());
    mAdTrackingDelegate.trackClick();
    mListener.onBannerClicked(bannerPresenter);
  }

  @Override
  public void onBannerError(@NonNull BannerPresenter bannerPresenter) {
    MaxAdsLog.d("Banner error for ad unit id: " + getAd().getAdUnitId());
    mListener.onBannerError(bannerPresenter);
  }
}
