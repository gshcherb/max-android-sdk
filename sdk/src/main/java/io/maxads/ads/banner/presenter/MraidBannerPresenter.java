package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.banner.view.MraidBannerViewModule;
import io.maxads.ads.base.model.Ad;
import io.maxads.mraid.MRAIDNativeFeature;

public class MraidBannerPresenter implements BannerPresenter, MraidBannerViewModule.Listener {

  @NonNull private final Context mContext;
  @NonNull private final Ad mAd;
  @NonNull private final MraidBannerViewModule mMraidBannerViewModule;
  @NonNull private final String[] mSupportedNativeFeatures;
  @Nullable private final BannerAdView.BannerAdListener mBannerAdListener;

  public MraidBannerPresenter(@NonNull Context context, @NonNull Ad ad,
                              @NonNull MraidBannerViewModule mraidBannerViewModule,
                              @Nullable BannerAdView.BannerAdListener bannerAdListener) {
    mContext = context;
    mAd = ad;
    mMraidBannerViewModule = mraidBannerViewModule;
    mBannerAdListener = bannerAdListener;
    mSupportedNativeFeatures = new String[]{
      MRAIDNativeFeature.CALENDAR,
      MRAIDNativeFeature.INLINE_VIDEO,
      MRAIDNativeFeature.SMS,
      MRAIDNativeFeature.STORE_PICTURE,
      MRAIDNativeFeature.TEL,
    };
  }

  @Override
  public void load() {
    mMraidBannerViewModule.show(mAd.getCreative(), mSupportedNativeFeatures);
  }

  @Override
  public void destroy() {
    mMraidBannerViewModule.destroy();
  }

  @Override
  public void onLoaded(@NonNull BannerAdView bannerAdView) {
    if (mBannerAdListener != null) {
      mBannerAdListener.onAdLoaded(bannerAdView);
    }
  }

  @Override
  public void onExpanded(@NonNull BannerAdView bannerAdView) {
    if (mBannerAdListener != null) {
      mBannerAdListener.onAdClicked(bannerAdView);
    }
  }
}
