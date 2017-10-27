package io.maxads.ads.interstitial.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.model.Winner;
import io.maxads.ads.base.util.MaxAdsLog;

public class InterstitialPresenterFactory {
  @NonNull private final Activity mActivity;

  public InterstitialPresenterFactory(@NonNull Activity activity) {
    mActivity = activity;
  }

  @Nullable
  public InterstitialPresenter createInterstitialPresenter(
    @NonNull Ad ad,
    @NonNull InterstitialPresenter.Listener interstitialPresenterListener) {

    InterstitialPresenter interstitialPresenter;
    final Winner.CreativeType creativeType = ad.getWinner().getCreativeType();
    switch (creativeType) {
      case HTML: {
        interstitialPresenter = new MraidInterstitialPresenter(mActivity, ad);
        break;
      }
      case VAST3: {
        interstitialPresenter = new VastInterstitialPresenter(mActivity, ad);
        break;
      }
      case EMPTY: {
        MaxAdsLog.d("Interstitial creative type is empty");
        return null;
      }
      default: {
        MaxAdsLog.e("Incompatible creative type: " + creativeType + ", for interstitial ad format.");
        return null;
      }
    }

    final InterstitialPresenterDecorator interstitialPresenterDecorator =
      new InterstitialPresenterDecorator(interstitialPresenter, new AdTrackingDelegate(ad.getSelectedUrls(),
        ad.getImpressionUrls(), ad.getClickUrls()), interstitialPresenterListener);
    interstitialPresenter.setListener(interstitialPresenterDecorator);
    return interstitialPresenterDecorator;
  }
}
