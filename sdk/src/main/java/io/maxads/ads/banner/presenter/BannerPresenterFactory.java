package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.model.Winner;
import io.maxads.ads.base.util.MaxAdsLog;

public class BannerPresenterFactory {
  @NonNull private static final String TAG = BannerPresenterFactory.class.getSimpleName();
  @NonNull private final Context mContext;

  public BannerPresenterFactory(@NonNull Context context) {
    mContext = context;
  }

  @Nullable
  public BannerPresenter createBannerPresenter(@NonNull Ad ad,
                                               @NonNull BannerPresenter.Listener bannerPresenterListener) {
    final BannerPresenter bannerPresenter = fromCreativeType(ad.getWinner().getCreativeType(), ad);
    if (bannerPresenter == null) {
      return null;
    }

    final BannerPresenterDecorator bannerPresenterDecorator = new BannerPresenterDecorator(bannerPresenter,
      new AdTrackingDelegate(ad.getSelectedUrls(), ad.getImpressionUrls(), ad.getClickUrls()), bannerPresenterListener);
    bannerPresenter.setListener(bannerPresenterDecorator);
    return bannerPresenterDecorator;
  }

  @Nullable
  @VisibleForTesting
  BannerPresenter fromCreativeType(@NonNull Winner.CreativeType creativeType, @NonNull Ad ad) {
    switch (creativeType) {
      case HTML: {
        return new MraidBannerPresenter(mContext, ad);
      }
      case EMPTY: {
        MaxAdsLog.d(TAG, "Banner creative type is empty");
        return null;
      }
      default: {
        MaxAdsLog.e(TAG, "Incompatible creative type: " + creativeType + ", for banner ad format.");
        return null;
      }
    }
  }
}
