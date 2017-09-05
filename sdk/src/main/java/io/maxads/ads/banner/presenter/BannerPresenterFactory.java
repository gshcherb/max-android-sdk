package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.model.Ad;

public class BannerPresenterFactory {
  @NonNull private final Context mContext;

  public BannerPresenterFactory(@NonNull Context context) {
    mContext = context;
  }

  public BannerPresenter getBannerPresenter(@NonNull Ad ad, @NonNull BannerAdView bannerAdView,
                                            @Nullable BannerAdView.BannerAdListener bannerAdListener) {
//    return new HtmlBannerPresenter(mContext, ad, bannerAdView, bannerAdListener);
    return new MraidBannerPresenter(mContext, ad, bannerAdView, bannerAdListener);
  }
}
