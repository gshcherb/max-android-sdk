package io.maxads.ads.banner.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.maxads.ads.banner.presenter.BannerPresenter;
import io.maxads.ads.banner.presenter.BannerPresenterFactory;
import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.api.RequestManager;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.Checks;

public class BannerController implements RequestManager.RequestListener, RequestManager.TimerListener,
  BannerPresenter.Listener {

  @NonNull private final BannerPresenterFactory mBannerPresenterFactory;
  @NonNull private final RequestManager mRequestManager;

  @Nullable private String mAdUnitId;
  @Nullable private BannerAdView mBannerAdView;
  @Nullable private BannerPresenter mCurrentBannerPresenter;
  @Nullable private BannerPresenter mNextBannerPresenter;
  @Nullable private BannerAdView.Listener mListener;
  private boolean mIsDestroyed;

  public BannerController(@NonNull Context context) {
    mBannerPresenterFactory = new BannerPresenterFactory(context);
    mRequestManager = new RequestManager();
    mRequestManager.setRequestListener(this);
    mRequestManager.setTimerListener(this);
  }

  public void setListener(@Nullable BannerAdView.Listener listener) {
    mListener = listener;
  }

  public void load(@NonNull String adUnitId, @NonNull BannerAdView bannerAdView) {
    Checks.checkNotNull(adUnitId, "adUnitId cannot be null");
    Checks.checkNotNull(bannerAdView, "bannerAdView cannot be null");
    Checks.checkArgument(!mIsDestroyed, "BannerController is destroyed");

    mAdUnitId = adUnitId;
    mBannerAdView = bannerAdView;
    mRequestManager.requestAd(adUnitId);
    mRequestManager.stopTimer();
  }

  private void showAd(@NonNull Ad ad) {
    if (mIsDestroyed) {
      return;
    }

    mNextBannerPresenter = mBannerPresenterFactory.createBannerPresenter(ad, this);
    if (mNextBannerPresenter == null) {
      mRequestManager.startTimer(ad.getRefreshTimeSeconds());

      if (mListener != null && mBannerAdView != null) {
        mListener.onBannerError(mBannerAdView);
      }
      return;
    }
    mNextBannerPresenter.load();
  }

  public void destroy() {
    mRequestManager.destroy();
    mAdUnitId = null;
    mBannerAdView = null;
    destroyBannerPresenter(mCurrentBannerPresenter);
    mCurrentBannerPresenter = null;
    destroyBannerPresenter(mNextBannerPresenter);
    mNextBannerPresenter = null;
    mListener = null;
    mIsDestroyed = true;
  }

  private void destroyBannerPresenter(@Nullable BannerPresenter bannerPresenter) {
    if (bannerPresenter != null) {
      bannerPresenter.destroy();
    }
  }

  // RequestManager.RequestListener
  @Override
  public void onRequestSuccess(@NonNull Ad ad) {
    if (mIsDestroyed) {
      return;
    }

    showAd(ad);
  }

  @Override
  public void onRequestFail(@NonNull Throwable throwable) {
    if (mIsDestroyed) {
      return;
    }

    mRequestManager.startTimer(RequestManager.DEFAULT_REFRESH_TIME_SECONDS);

    if (mListener != null && mBannerAdView != null) {
      mListener.onBannerError(mBannerAdView);
    }
  }

  // RequestManager.TimerListener
  @Override
  public void onTimerComplete() {
    if (mIsDestroyed || mAdUnitId == null || mBannerAdView == null) {
      return;
    }

    load(mAdUnitId, mBannerAdView);
  }

  // BannerPresenter.Listener
  @Override
  public void onBannerLoaded(@NonNull BannerPresenter bannerPresenter, @NonNull View banner) {
    if (mIsDestroyed) {
      return;
    }

    destroyBannerPresenter(mCurrentBannerPresenter);
    mCurrentBannerPresenter = mNextBannerPresenter;
    mNextBannerPresenter = null;

    mRequestManager.startTimer(bannerPresenter.getAd().getRefreshTimeSeconds());

    if (mBannerAdView != null) {
      mBannerAdView.removeAllViews();
      banner.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
      mBannerAdView.addView(banner);

      if (mListener != null) {
        mListener.onBannerLoaded(mBannerAdView);
      }
    }
  }

  @Override
  public void onBannerClicked(@NonNull BannerPresenter bannerPresenter) {
    if (mIsDestroyed) {
      return;
    }

    if (mListener != null && mBannerAdView != null) {
      mListener.onBannerClicked(mBannerAdView);
    }
  }

  @Override
  public void onBannerError(@NonNull BannerPresenter bannerPresenter) {
    if (mIsDestroyed) {
      return;
    }

    mRequestManager.startTimer(bannerPresenter.getAd().getRefreshTimeSeconds());

    if (mListener != null && mBannerAdView != null) {
      mListener.onBannerError(mBannerAdView);
    }
  }
}
