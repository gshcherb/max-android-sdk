package io.maxads.ads.interstitial.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.UrlHandlerDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.mraid.MRAIDInterstitial;
import io.maxads.mraid.MRAIDNativeFeatureListener;
import io.maxads.mraid.MRAIDView;
import io.maxads.mraid.MRAIDViewListener;

public class MraidInterstitialPresenter implements InterstitialPresenter, MRAIDViewListener, MRAIDNativeFeatureListener {

  @NonNull private final Activity mActivity;
  @NonNull private final Ad mAd;
  @NonNull private final UrlHandlerDelegate mUrlHandlerDelegate;
  @NonNull private final String[] mSupportedNativeFeatures;

  @Nullable private InterstitialPresenter.Listener mListener;
  @Nullable private MRAIDInterstitial mMRAIDInterstitial;

  public MraidInterstitialPresenter(@NonNull Activity activity, @NonNull Ad ad) {
    mActivity = activity;
    mAd = ad;
    mUrlHandlerDelegate = new UrlHandlerDelegate(activity);
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
    mMRAIDInterstitial = new MRAIDInterstitial(mActivity, "http://" + MaxAds.HOST + "/", mAd.getCreative(),
      mSupportedNativeFeatures, this, this);
  }

  @Override
  public void show() {
    if (mMRAIDInterstitial != null) {
      mMRAIDInterstitial.show(mActivity);

      if (mListener != null) {
        mListener.onInterstitialShown(this);
      }
    }
  }

  @Override
  public void destroy() {
    if (mMRAIDInterstitial != null) {
      mMRAIDInterstitial.destroy();
    }
    mListener = null;
  }

  @Override
  public void mraidViewLoaded(MRAIDView mraidView) {
    if (mListener != null) {
      mListener.onInterstitialLoaded(this);
    }
  }

  @Override
  public void mraidViewExpand(MRAIDView mraidView) {
  }

  @Override
  public void mraidViewClose(MRAIDView mraidView) {
    if (mListener != null) {
      mListener.onInterstitialDismissed(this);
    }
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
      mListener.onInterstitialClicked(this);
    }
  }

  @Override
  public void mraidNativeFeatureStorePicture(String url) {

  }

  @Override
  public void mraidNativeFeatureSendSms(String url) {

  }
}
