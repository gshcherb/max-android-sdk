package io.maxads.ads.interstitial.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;

public class InterstitialPresenterFactory {
  @NonNull private final Activity mActivity;

  public InterstitialPresenterFactory(@NonNull Activity activity) {
    mActivity = activity;
  }

  @NonNull
  public InterstitialPresenter createInterstitialPresenter(
    @NonNull Ad ad,
    @NonNull InterstitialPresenter.Listener interstitialPresenterListener) {
//    final HtmlInterstitialPresenter htmlBannerPresenter = new HtmlInterstitialPresenter(mActivity, ad);
//    final MraidInterstitialPresenter mraidInterstitialPresenter = new MraidInterstitialPresenter(mActivity, ad);
    final VastInterstitialPresenter vastInterstitialPresenter = new VastInterstitialPresenter(mActivity, ad);

    final InterstitialPresenterDecorator interstitialPresenterDecorator =
      new InterstitialPresenterDecorator(vastInterstitialPresenter, new AdTrackingDelegate(ad.getSelectedUrls(),
        ad.getImpressionUrls(), ad.getClickUrls()), interstitialPresenterListener);

//    htmlBannerPresenter.setListener(interstitialPresenterDecorator);
//    mraidInterstitialPresenter.setListener(interstitialPresenterDecorator);
    vastInterstitialPresenter.setListener(interstitialPresenterDecorator);

    return interstitialPresenterDecorator;
  }
}
