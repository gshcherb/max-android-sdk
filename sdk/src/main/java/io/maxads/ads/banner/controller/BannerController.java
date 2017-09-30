package io.maxads.ads.banner.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import io.maxads.ads.banner.presenter.BannerPresenter;
import io.maxads.ads.banner.presenter.BannerPresenterFactory;
import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.api.RequestManager;
import io.maxads.ads.base.model.Ad;

public class BannerController implements RequestManager.RequestListener, RequestManager.TimerListener,
  BannerPresenter.Listener {
  private static final int DEFAULT_REFRESH_TIME_SECONDS = 60;

  @NonNull private final BannerPresenterFactory mBannerPresenterFactory;
  @NonNull private final RequestManager mRequestManager;

  @Nullable private String mAdUnitId;
  @Nullable private BannerAdView mBannerAdView;
  @Nullable private BannerPresenter mCurrentBannerPresenter;
  @Nullable private BannerPresenter mNextBannerPresenter;
  @Nullable private BannerAdView.Listener mListener;

  public BannerController(@NonNull Context context) {
    mBannerPresenterFactory = new BannerPresenterFactory(context);
    mRequestManager = new RequestManager();
    mRequestManager.setRequestListener(this);
    mRequestManager.setTimerListener(this);
  }

  public void setListener(@Nullable BannerAdView.Listener listener) {
    mListener = listener;
  }

  public void load(@Nullable String adUnitId, @Nullable final BannerAdView bannerAdView) {
    if (adUnitId == null || bannerAdView == null) {
      return;
    }

    mAdUnitId = adUnitId;
    mBannerAdView = bannerAdView;
    mRequestManager.requestAd(adUnitId);
  }

  private void showAd(@NonNull Ad ad) {
    if (mBannerAdView == null) {
      return;
    }

    mNextBannerPresenter = mBannerPresenterFactory.createBannerPresenter(mBannerAdView, ad, this);
    mNextBannerPresenter.load();
  }

  public void destroy() {
    // TODO (steffan): null out values here
    mRequestManager.destroy();
    mAdUnitId = null;
    mBannerAdView = null;
    destroyBannerPresenter(mCurrentBannerPresenter);
    mCurrentBannerPresenter = null;
    destroyBannerPresenter(mNextBannerPresenter);
    mNextBannerPresenter = null;
    mListener = null;
  }

  private void destroyBannerPresenter(@Nullable BannerPresenter bannerPresenter) {
    if (bannerPresenter != null) {
      bannerPresenter.destroy();
    }
  }

  @Override
  public void onRequestSuccess(@NonNull Ad ad) {
    showAd(ad);
  }

  @Override
  public void onRequestFail(@NonNull Throwable throwable) {
    // TODO (steffan): start request timer here?
    if (mListener != null && mBannerAdView != null) {
      mListener.onBannerError(mBannerAdView);
    }
  }

  @Override
  public void onTimerComplete() {
    load(mAdUnitId, mBannerAdView);
  }

  @Override
  public void onBannerLoaded(@NonNull BannerPresenter bannerPresenter, @NonNull View banner) {
    destroyBannerPresenter(mCurrentBannerPresenter);
    mCurrentBannerPresenter = mNextBannerPresenter;
    mNextBannerPresenter = null;

    final long refreshTimeSeconds = bannerPresenter.getAd().getRefreshTimeSeconds();
    mRequestManager.startTimer(refreshTimeSeconds > 0 ? refreshTimeSeconds : DEFAULT_REFRESH_TIME_SECONDS);

    if (mListener != null && mBannerAdView != null) {
      mListener.onBannerLoaded(mBannerAdView);
    }
  }

  @Override
  public void onBannerClicked(@NonNull BannerPresenter bannerPresenter) {
    if (mListener != null && mBannerAdView != null) {
      mListener.onBannerClicked(mBannerAdView);
    }
  }

  @Override
  public void onBannerError(@NonNull BannerPresenter bannerPresenter) {
    // TODO (steffan): start request timer here?
    if (mListener != null && mBannerAdView != null) {
      mListener.onBannerError(mBannerAdView);
    }
  }
}
