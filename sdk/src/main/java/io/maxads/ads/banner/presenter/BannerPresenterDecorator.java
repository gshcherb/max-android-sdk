package io.maxads.ads.banner.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;

public class BannerPresenterDecorator implements BannerPresenter, BannerPresenter.Listener {
  @NonNull private final BannerPresenter mBannerPresenter;
  @NonNull private final AdTrackingDelegate mAdTrackingDelegate;
  @NonNull private final BannerPresenter.Listener mBannerPresenterListener;

  public BannerPresenterDecorator(@NonNull BannerPresenter bannerPresenter,
                                  @NonNull AdTrackingDelegate adTrackingDelegate,
                                  @NonNull BannerPresenter.Listener bannerPresenterListener) {
    mBannerPresenter = bannerPresenter;
    mAdTrackingDelegate = adTrackingDelegate;
    mBannerPresenterListener = bannerPresenterListener;
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
    mBannerPresenter.load();
  }

  @Override
  public void destroy() {
    mBannerPresenter.destroy();
  }

  @Override
  public void onBannerLoaded(@NonNull BannerPresenter bannerPresenter, @NonNull View banner) {
    // track impression
    mAdTrackingDelegate.trackImpression();

    mBannerPresenterListener.onBannerLoaded(bannerPresenter, banner);
  }

  @Override
  public void onBannerClicked(@NonNull BannerPresenter bannerPresenter) {
    // track click and open click url if needed
    mAdTrackingDelegate.trackClick();

    mBannerPresenterListener.onBannerClicked(bannerPresenter);
  }

  @Override
  public void onBannerError(@NonNull BannerPresenter bannerPresenter) {
    mBannerPresenterListener.onBannerError(bannerPresenter);
  }
}
