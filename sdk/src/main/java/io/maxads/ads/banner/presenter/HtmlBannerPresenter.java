package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.view.HtmlWebViewClient;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.view.HtmlWebView;

public class HtmlBannerPresenter implements BannerPresenter {

  @NonNull private final HtmlWebView mHtmlWebView;
  @NonNull private final Ad mAd;
  @NonNull private final BannerAdView mBannerAdView;
  @Nullable private final BannerAdView.BannerAdListener mBannerAdListener;

  public HtmlBannerPresenter(@NonNull Context context,
                             @NonNull Ad ad,
                             @NonNull BannerAdView bannerAdView,
                             @Nullable BannerAdView.BannerAdListener bannerAdListener) {
    mAd = ad;
    mBannerAdView = bannerAdView;
    mBannerAdListener = bannerAdListener;
    mHtmlWebView = new HtmlWebView(context);
    mHtmlWebView.setWebViewClient(new HtmlWebViewClient(context));
  }

  @Override
  public void load() {
    mHtmlWebView.loadDataWithBaseURL("http://" + MaxAds.HOST + "/", mAd.getCreative(), "text/html", "utf-8", null);
    mBannerAdView.addView(mHtmlWebView);
  }

  @Override
  public void destroy() {
    mHtmlWebView.destroy();
  }
}