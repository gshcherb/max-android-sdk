package io.maxads.ads.banner.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import io.maxads.ads.banner.presenter.BannerPresenter;
import io.maxads.ads.banner.presenter.BannerPresenterFactory;
import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.api.AdRequest;
import io.maxads.ads.base.api.AdRequestFactory;
import io.maxads.ads.base.api.ApiManager;
import io.maxads.ads.base.model.Ad;
import io.reactivex.functions.Consumer;

public class BannerController implements BannerPresenter.Listener {

  @NonNull private final ApiManager mApiManager;
  @NonNull private final AdRequestFactory mAdRequestFactory;
  @NonNull private final BannerPresenterFactory mBannerPresenterFactory;
  @Nullable private BannerAdView.Listener mListener;
  @Nullable private BannerPresenter mCurrentBannerPresenter;
  @Nullable private BannerPresenter mNextBannerPresenter;

  public BannerController(@NonNull Context context) {
    mApiManager = MaxAds.getApiManager();
    mAdRequestFactory = new AdRequestFactory();
    mBannerPresenterFactory = new BannerPresenterFactory(context);
  }

  public void setListener(@Nullable BannerAdView.Listener listener) {
    mListener = listener;
  }

  public void load(@NonNull String adUnitId, @NonNull final BannerAdView bannerAdView) {
    mAdRequestFactory.createAdRequest(adUnitId).subscribe(new Consumer<AdRequest>() {
      @Override
      public void accept(AdRequest adRequest) throws Exception {
        loadFromApi(adRequest, bannerAdView);
      }
    });
  }

  private void loadFromApi(@NonNull AdRequest adRequest, @NonNull final BannerAdView bannerAdView) {
    mApiManager.getAd(adRequest).subscribe(new Consumer<Ad>() {
      @Override
      public void accept(@NonNull Ad ad) throws Exception {
        showAd(ad, bannerAdView);
      }
    }, new Consumer<Throwable>() {
      @Override
      public void accept(Throwable throwable) throws Exception {
      }
    });
  }

  private void showAd(@NonNull Ad ad, @NonNull BannerAdView bannerAdView) {
    mNextBannerPresenter = mBannerPresenterFactory.createBannerPresenter(mApiManager, bannerAdView, ad, this, mListener);
    mNextBannerPresenter.load();
  }

  public void destroy() {
    destroyBannerPresenter(mCurrentBannerPresenter);
    destroyBannerPresenter(mNextBannerPresenter);
  }

  private void destroyBannerPresenter(@Nullable BannerPresenter bannerPresenter) {
    if (bannerPresenter != null) {
      bannerPresenter.destroy();
    }
  }

  @Override
  public void onBannerLoaded(@NonNull View banner) {
    destroyBannerPresenter(mCurrentBannerPresenter);
    mCurrentBannerPresenter = mNextBannerPresenter;
    mNextBannerPresenter = null;
  }

  @Override
  public void onBannerClicked() {
  }

  @Override
  public void onBannerError() {
    // Load new ad
  }
}
