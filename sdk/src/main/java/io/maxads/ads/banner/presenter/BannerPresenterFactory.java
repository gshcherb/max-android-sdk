package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.banner.view.MraidBannerViewModule;
import io.maxads.ads.banner.view.MraidBannerViewModuleImpl;
import io.maxads.ads.base.model.Ad;

public class BannerPresenterFactory {
  @NonNull private final Context mContext;

  public BannerPresenterFactory(@NonNull Context context) {
    mContext = context;
  }

  public BannerPresenter createBannerPresenter(@NonNull Ad ad, @NonNull BannerAdView bannerAdView,
                                               @Nullable BannerAdView.BannerAdListener bannerAdListener) {
    final MraidBannerViewModule mraidBannerViewModule = new MraidBannerViewModuleImpl(mContext, bannerAdView);
    final MraidBannerPresenter mraidBannerPresenter
      = new MraidBannerPresenter(mContext, ad, mraidBannerViewModule, bannerAdListener);
    mraidBannerViewModule.setListener(mraidBannerPresenter);
    return mraidBannerPresenter;
  }
}
