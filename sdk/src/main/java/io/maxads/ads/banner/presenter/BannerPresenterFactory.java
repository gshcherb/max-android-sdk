package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;

public class BannerPresenterFactory {
  @NonNull private final Context mContext;

  public BannerPresenterFactory(@NonNull Context context) {
    mContext = context;
  }

  public BannerPresenter createBannerPresenter(@NonNull Ad ad,
                                               @NonNull BannerPresenter.Listener bannerPresenterListener) {
//    final MraidBannerPresenter mraidBannerPresenter = new MraidBannerPresenter(mContext, ad);
    final HtmlBannerPresenter htmlBannerPresenter = new HtmlBannerPresenter(mContext, ad);

    final BannerPresenterDecorator bannerPresenterDecorator = new BannerPresenterDecorator(htmlBannerPresenter,
      new AdTrackingDelegate(ad.getImpressionUrls(), ad.getClickUrls()), bannerPresenterListener);

//    mraidBannerPresenter.setListener(bannerPresenterDecorator);
    htmlBannerPresenter.setListener(bannerPresenterDecorator);

    return bannerPresenterDecorator;
  }
}
