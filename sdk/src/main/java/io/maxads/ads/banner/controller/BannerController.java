package io.maxads.ads.banner.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
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
import io.maxads.ads.base.util.InitializationHelper;

public class BannerController implements RequestManager.RequestListener, BannerPresenter.Listener {

  @NonNull private final BannerPresenterFactory mBannerPresenterFactory;
  @NonNull private final RequestManager mRequestManager;
  @NonNull private final InitializationHelper mInitializationHelper;

  @Nullable private BannerAdView mBannerAdView;
  @Nullable private BannerPresenter mCurrentBannerPresenter;
  @Nullable private BannerPresenter mNextBannerPresenter;
  @Nullable private BannerAdView.Listener mListener;
  private boolean mIsDestroyed;

  public BannerController(@NonNull Context context) {
    this(new BannerPresenterFactory(context), new RequestManager(), new InitializationHelper());
  }

  @VisibleForTesting
  BannerController(@NonNull BannerPresenterFactory bannerPresenterFactory,
                   @NonNull RequestManager requestManager, @NonNull InitializationHelper initializationHelper) {
    mBannerPresenterFactory = bannerPresenterFactory;
    mRequestManager = requestManager;
    mRequestManager.setRequestListener(this);
    mInitializationHelper = initializationHelper;
  }

  public void setListener(@Nullable BannerAdView.Listener listener) {
    mListener = listener;
  }

  public void load(@NonNull String adUnitId, @NonNull BannerAdView bannerAdView) {
    if (!Checks.NoThrow.checkArgument(mInitializationHelper.isInitialized(), "MaxAds SDK has not been initialized. " +
      "Please call MaxAds#initialize in your application's onCreate method.")) {
      return;
    }

    if (!Checks.NoThrow.checkNotNull(adUnitId, "adUnitId cannot be null")) {
      return;
    }

    if (!Checks.NoThrow.checkNotNull(bannerAdView, "bannerAdView cannot be null")) {
      return;
    }

    if (!Checks.NoThrow.checkArgument(!mIsDestroyed, "BannerController is destroyed")) {
      return;
    }

    mBannerAdView = bannerAdView;
    mRequestManager.setAdUnitId(adUnitId);
    mRequestManager.requestAd();
    mRequestManager.stopRefreshTimer();
  }

  @VisibleForTesting
  void showAd(@NonNull Ad ad) {
    if (mIsDestroyed) {
      return;
    }

    // TODO (steffan): there is a low probability bug here if ads are requested rapidly that the mNextBannerPresenter
    // will continue to change before it can be loaded into the view. This means that there will be BannerPresenters
    // without a strong reference to them attempting to be loaded. It's possible for them to be garbage collected before
    // being displayed
    mNextBannerPresenter = mBannerPresenterFactory.createBannerPresenter(ad, this);
    if (mNextBannerPresenter == null) {
      mRequestManager.startRefreshTimer(ad.getRefreshTimeSeconds());

      if (mListener != null && mBannerAdView != null) {
        mListener.onBannerError(mBannerAdView);
      }
      return;
    }
    mNextBannerPresenter.load();
  }

  public void destroy() {
    mRequestManager.destroy();
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

    mRequestManager.startRefreshTimer(RequestManager.DEFAULT_REFRESH_TIME_SECONDS);

    if (mListener != null && mBannerAdView != null) {
      mListener.onBannerError(mBannerAdView);
    }
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

    mRequestManager.startRefreshTimer(bannerPresenter.getAd().getRefreshTimeSeconds());

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

    mRequestManager.startRefreshTimer(bannerPresenter.getAd().getRefreshTimeSeconds());

    if (mListener != null && mBannerAdView != null) {
      mListener.onBannerError(mBannerAdView);
    }
  }
}
