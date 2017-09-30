package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.api.ApiClient;
import io.maxads.ads.base.model.Ad;

public class BannerPresenterFactory {
  @NonNull private final Context mContext;

  public BannerPresenterFactory(@NonNull Context context) {
    mContext = context;
  }

  public BannerPresenter createBannerPresenter(@NonNull BannerAdView bannerAdView,
                                               @NonNull Ad ad,
                                               @NonNull BannerPresenter.Listener bannerPresenterListener,
                                               @Nullable BannerAdView.Listener bannerAdViewListener) {
//    final MraidBannerPresenter mraidBannerPresenter = new MraidBannerPresenter(mContext, ad);
    final HtmlBannerPresenter htmlBannerPresenter = new HtmlBannerPresenter(mContext, ad);

    final BannerPresenterDecorator bannerPresenterDecorator = new BannerPresenterDecorator(htmlBannerPresenter,
      bannerAdView, ad, new AdTrackingDelegate(ad.getImpressionUrls(), ad.getClickUrls()),
      bannerPresenterListener, bannerAdViewListener);

//    mraidBannerPresenter.setListener(bannerPresenterDecorator);
    htmlBannerPresenter.setListener(bannerPresenterDecorator);

    return bannerPresenterDecorator;
  }
}
