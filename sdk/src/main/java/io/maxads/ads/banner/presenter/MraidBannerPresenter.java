package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.model.Ad;
import io.maxads.mraid.MRAIDNativeFeature;
import io.maxads.mraid.MRAIDNativeFeatureListener;
import io.maxads.mraid.MRAIDView;
import io.maxads.mraid.MRAIDViewListener;

public class MraidBannerPresenter implements BannerPresenter, MRAIDNativeFeatureListener, MRAIDViewListener {

  @NonNull private Context mContext;
  @NonNull private final Ad mAd;
  @NonNull private final BannerAdView mBannerAdView;
  @NonNull private final String[] mSupportedNativeFeatures;

  @Nullable private final BannerAdView.BannerAdListener mBannerAdListener;
  @Nullable private MRAIDView mMraidView;

  public MraidBannerPresenter(@NonNull Context context, @NonNull Ad ad, @NonNull BannerAdView bannerAdView,
                             @Nullable BannerAdView.BannerAdListener bannerAdListener) {
    mContext = context;
    mAd = ad;
    mBannerAdView = bannerAdView;
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
    mMraidView = new MRAIDView(mContext, "http://" + MaxAds.HOST + "/", mAd.getCreative(), mSupportedNativeFeatures,
      this, this, false);
    mMraidView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
      ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
    mBannerAdView.addView(mMraidView);
  }

  @Override
  public void destroy() {
    if (mMraidView != null) {
      mMraidView.destroy();
    }
  }

  // MRAIDNativeFeatureListener
  @Override
  public void mraidNativeFeatureCallTel(String url) {

  }

  @Override
  public void mraidNativeFeatureCreateCalendarEvent(String eventJSON) {

  }

  @Override
  public void mraidNativeFeaturePlayVideo(String url) {

  }

  @Override
  public void mraidNativeFeatureOpenBrowser(String url) {

  }

  @Override
  public void mraidNativeFeatureStorePicture(String url) {

  }

  @Override
  public void mraidNativeFeatureSendSms(String url) {

  }

  // MRAIDViewListener
  @Override
  public void mraidViewLoaded(MRAIDView mraidView) {
  }

  @Override
  public void mraidViewExpand(MRAIDView mraidView) {
  }

  @Override
  public void mraidViewClose(MRAIDView mraidView) {
  }

  @Override
  public boolean mraidViewResize(MRAIDView mraidView, int width, int height, int offsetX, int offsetY) {
    return false;
  }
}
