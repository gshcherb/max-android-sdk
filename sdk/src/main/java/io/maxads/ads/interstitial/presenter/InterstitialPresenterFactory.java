package io.maxads.ads.interstitial.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;

public class InterstitialPresenterFactory {
  @NonNull private final Context mContext;

  public InterstitialPresenterFactory(@NonNull Context context) {
    mContext = context;
  }

  @NonNull
  public InterstitialPresenter createInterstitialPresenter(
    @NonNull Ad ad,
    @NonNull InterstitialPresenter.Listener interstitialPresenterListener) {
    final HtmlInterstitialPresenter htmlBannerPresenter = new HtmlInterstitialPresenter(mContext, ad);

    final InterstitialPresenterDecorator interstitialPresenterDecorator =
      new InterstitialPresenterDecorator(htmlBannerPresenter, new AdTrackingDelegate(ad.getImpressionUrls(),
        ad.getClickUrls()), interstitialPresenterListener);

    htmlBannerPresenter.setListener(interstitialPresenterDecorator);

    return interstitialPresenterDecorator;
  }
}
