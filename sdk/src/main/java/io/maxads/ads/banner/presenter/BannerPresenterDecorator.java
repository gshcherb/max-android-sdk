package io.maxads.ads.banner.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.model.Ad;

public class BannerPresenterDecorator implements BannerPresenter, BannerPresenter.Listener {
  @NonNull private final BannerPresenter mBannerPresenter;
  @NonNull private final BannerAdView mBannerAdView;
  @NonNull private final Ad mAd;
  @NonNull private final BannerPresenter.Listener mBannerPresenterListener;
  @Nullable private final BannerAdView.Listener mBannerAdViewListener;

  public BannerPresenterDecorator(@NonNull BannerPresenter bannerPresenter, @NonNull BannerAdView bannerAdView,
                                  @NonNull Ad ad, @NonNull BannerPresenter.Listener bannerPresenterListener,
                                  @Nullable BannerAdView.Listener bannerAdViewListener) {
    mBannerPresenter = bannerPresenter;
    mBannerAdView = bannerAdView;
    mAd = ad;
    mBannerPresenterListener = bannerPresenterListener;
    mBannerAdViewListener = bannerAdViewListener;
  }

  @Override
  public void setListener(@Nullable Listener listener) {
    // We set the listener in the constructor instead
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
  public void onBannerLoaded(@NonNull View banner) {
    // load view
    mBannerAdView.removeAllViews();
    banner.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
    mBannerAdView.addView(banner);

    // track impression

    mBannerPresenterListener.onBannerLoaded(banner);
    if (mBannerAdViewListener != null) {
      mBannerAdViewListener.onBannerLoaded(mBannerAdView);
    }
  }

  @Override
  public void onBannerClicked() {
    // track click and open click url if needed

    mBannerPresenterListener.onBannerClicked();
    if (mBannerAdViewListener != null) {
      mBannerAdViewListener.onBannerClicked(mBannerAdView);
    }
  }

  @Override
  public void onBannerError() {
    mBannerPresenterListener.onBannerError();
    if (mBannerAdViewListener != null) {
      mBannerAdViewListener.onBannerError(mBannerAdView);
    }
  }
}