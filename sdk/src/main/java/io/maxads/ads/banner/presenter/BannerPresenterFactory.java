package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.model.Winner;
import io.maxads.ads.base.util.MaxAdsLog;

public class BannerPresenterFactory {
  @NonNull private final Context mContext;

  public BannerPresenterFactory(@NonNull Context context) {
    mContext = context;
  }

  @Nullable
  public BannerPresenter createBannerPresenter(@NonNull Ad ad,
                                               @NonNull BannerPresenter.Listener bannerPresenterListener) {
    BannerPresenter bannerPresenter;
    final Winner.CreativeType creativeType = ad.getWinner().getCreativeType();
    switch (creativeType) {
      case HTML: {
        bannerPresenter = new MraidBannerPresenter(mContext, ad);
        break;
      }
      case EMPTY: {
        MaxAdsLog.d("Banner creative type is empty");
        return null;
      }
      default: {
        MaxAdsLog.e("Incompatible creative type: " + creativeType + ", for banner ad format.");
        return null;
      }
    }

    final BannerPresenterDecorator bannerPresenterDecorator = new BannerPresenterDecorator(bannerPresenter,
      new AdTrackingDelegate(ad.getSelectedUrls(), ad.getImpressionUrls(), ad.getClickUrls()), bannerPresenterListener);
    bannerPresenter.setListener(bannerPresenterDecorator);
    return bannerPresenterDecorator;
  }
}
