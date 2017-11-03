package io.maxads.ads.interstitial.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.model.Winner;
import io.maxads.ads.base.util.MaxAdsLog;

public class InterstitialPresenterFactory {
  @NonNull private static final String TAG = InterstitialPresenterFactory.class.getSimpleName();
  @NonNull private final Activity mActivity;

  public InterstitialPresenterFactory(@NonNull Activity activity) {
    mActivity = activity;
  }

  @Nullable
  public InterstitialPresenter createInterstitialPresenter(
    @NonNull Ad ad,
    @NonNull InterstitialPresenter.Listener interstitialPresenterListener) {

    final InterstitialPresenter interstitialPresenter = fromCreativeType(ad.getWinner().getCreativeType(), ad);
    if (interstitialPresenter == null) {
      return null;
    }

    final InterstitialPresenterDecorator interstitialPresenterDecorator =
      new InterstitialPresenterDecorator(interstitialPresenter, new AdTrackingDelegate(ad.getSelectedUrls(),
        ad.getImpressionUrls(), ad.getClickUrls()), interstitialPresenterListener);
    interstitialPresenter.setListener(interstitialPresenterDecorator);
    return interstitialPresenterDecorator;
  }

  @Nullable
  @VisibleForTesting
  InterstitialPresenter fromCreativeType(@NonNull Winner.CreativeType creativeType, @NonNull Ad ad) {
    switch (creativeType) {
      case HTML: {
        return new MraidInterstitialPresenter(mActivity, ad);
      }
      case VAST3: {
        return new VastInterstitialPresenter(mActivity, ad);
      }
      case EMPTY: {
        MaxAdsLog.d(TAG, "Interstitial creative type is empty");
        return null;
      }
      default: {
        MaxAdsLog.e(TAG, "Incompatible creative type: " + creativeType + ", for interstitial ad format.");
        return null;
      }
    }
  }
}
