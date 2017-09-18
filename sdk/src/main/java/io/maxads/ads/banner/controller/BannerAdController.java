package io.maxads.ads.banner.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.banner.presenter.BannerPresenter;
import io.maxads.ads.banner.presenter.BannerPresenterFactory;
import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.api.AdRequest;
import io.maxads.ads.base.api.AdRequestFactory;
import io.maxads.ads.base.api.ApiManager;
import io.maxads.ads.base.model.Ad;
import io.reactivex.functions.Consumer;

public class BannerAdController {

  @NonNull private final ApiManager mApiManager;
  @NonNull private final AdRequestFactory mAdRequestFactory;
  @NonNull private final BannerPresenterFactory mBannerPresenterFactory;
  @Nullable private BannerAdView.BannerAdListener mBannerAdListener;
  @Nullable private BannerPresenter mBannerPresenter;

  public BannerAdController(@NonNull Context context) {
    mApiManager = MaxAds.getApiManager();
    mAdRequestFactory = new AdRequestFactory();
    mBannerPresenterFactory = new BannerPresenterFactory(context);
  }

  public void setBannerAdListener(@Nullable BannerAdView.BannerAdListener bannerAdListener) {
    mBannerAdListener = bannerAdListener;
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
    final BannerPresenter bannerPresenter = mBannerPresenterFactory.createBannerPresenter(ad, bannerAdView, mBannerAdListener);

    // I think it's fine to destroy the existing ad here since we already have the next one ready to display
    destroyBannerPresenter();

    bannerPresenter.load();
    mBannerPresenter = bannerPresenter;
  }

  public void destroy() {
    destroyBannerPresenter();
  }

  private void destroyBannerPresenter() {
    if (mBannerPresenter != null) {
      mBannerPresenter.destroy();
    }
  }
}
