package io.maxads.ads.banner.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;

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
    mAdTrackingDelegate.trackSelected();

    mBannerPresenter.load();
  }

  @Override
  public void destroy() {
    mBannerPresenter.destroy();
  }

  @Override
  public void onBannerLoaded(@NonNull BannerPresenter bannerPresenter, @NonNull View banner) {
    mAdTrackingDelegate.trackImpression();

    mListener.onBannerLoaded(bannerPresenter, banner);
  }

  @Override
  public void onBannerClicked(@NonNull BannerPresenter bannerPresenter) {
    mAdTrackingDelegate.trackClick();

    mListener.onBannerClicked(bannerPresenter);
  }

  @Override
  public void onBannerError(@NonNull BannerPresenter bannerPresenter) {
    mListener.onBannerError(bannerPresenter);
  }
}
