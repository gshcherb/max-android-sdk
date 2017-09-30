package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.banner.view.MraidBannerViewModule;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.model.Ad;
import io.maxads.mraid.MRAIDNativeFeature;
import io.maxads.mraid.MRAIDNativeFeatureListener;
import io.maxads.mraid.MRAIDView;
import io.maxads.mraid.MRAIDViewListener;

public class MraidBannerPresenter implements BannerPresenter, MRAIDViewListener, MRAIDNativeFeatureListener {

  @NonNull private final Context mContext;
  @NonNull private final Ad mAd;
  @NonNull private final String[] mSupportedNativeFeatures;

  @Nullable private BannerPresenter.Listener mListener;
  @Nullable private MRAIDView mMraidView;

  public MraidBannerPresenter(@NonNull Context context, @NonNull Ad ad) {
    mContext = context;
    mAd = ad;
    mSupportedNativeFeatures = new String[]{
      MRAIDNativeFeature.CALENDAR,
      MRAIDNativeFeature.INLINE_VIDEO,
      MRAIDNativeFeature.SMS,
      MRAIDNativeFeature.STORE_PICTURE,
      MRAIDNativeFeature.TEL,
    };
  }

  @Override
  public void setListener(@Nullable Listener listener) {
    mListener = listener;
  }

  @NonNull
  @Override
  public Ad getAd() {
    return mAd;
  }

  @Override
  public void load() {
    mMraidView = new MRAIDView(mContext, "http://" + MaxAds.HOST + "/", mAd.getCreative(), mSupportedNativeFeatures,
      this, this, false);
  }

  @Override
  public void destroy() {
    if (mMraidView != null) {
      mMraidView.destroy();
    }
    mListener = null;
  }

  @Override
  public void mraidViewLoaded(MRAIDView mraidView) {
    if (mListener != null) {
      mListener.onBannerLoaded(this, mraidView);
    }
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
}
