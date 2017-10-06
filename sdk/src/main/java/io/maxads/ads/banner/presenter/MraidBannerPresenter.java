package io.maxads.ads.banner.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.UrlHandlerDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.mraid.MRAIDBanner;
import io.maxads.mraid.MRAIDNativeFeatureListener;
import io.maxads.mraid.MRAIDView;
import io.maxads.mraid.MRAIDViewListener;

public class MraidBannerPresenter implements BannerPresenter, MRAIDViewListener, MRAIDNativeFeatureListener {

  @NonNull private final Context mContext;
  @NonNull private final Ad mAd;
  @NonNull private final UrlHandlerDelegate mUrlHandlerDelegate;
  @NonNull private final String[] mSupportedNativeFeatures;

  @Nullable private BannerPresenter.Listener mListener;
  @Nullable private MRAIDBanner mMRAIDBanner;

  public MraidBannerPresenter(@NonNull Context context, @NonNull Ad ad) {
    mContext = context;
    mAd = ad;
    mUrlHandlerDelegate = new UrlHandlerDelegate(context);
    mSupportedNativeFeatures = new String[]{};
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
    mMRAIDBanner = new MRAIDBanner(mContext, "http://" + MaxAds.HOST + "/", mAd.getCreative(), mSupportedNativeFeatures,
      this, this);
  }

  @Override
  public void destroy() {
    if (mMRAIDBanner != null) {
      mMRAIDBanner.destroy();
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
    if (mListener != null) {
      mListener.onBannerClicked(this);
    }
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
    mUrlHandlerDelegate.handleUrl(url);
    // TODO (steffan): will this always count as a click? Are there other cases that should be considered a click?
    if (mListener != null) {
      mListener.onBannerClicked(this);
    }
  }

  @Override
  public void mraidNativeFeatureStorePicture(String url) {

  }

  @Override
  public void mraidNativeFeatureSendSms(String url) {

  }
}
