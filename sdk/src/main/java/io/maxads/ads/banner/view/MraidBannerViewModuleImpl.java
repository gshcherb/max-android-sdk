package io.maxads.ads.banner.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import io.maxads.ads.base.MaxAds;
import io.maxads.mraid.MRAIDNativeFeatureListener;
import io.maxads.mraid.MRAIDView;
import io.maxads.mraid.MRAIDViewListener;

public class MraidBannerViewModuleImpl implements MraidBannerViewModule, MRAIDNativeFeatureListener, MRAIDViewListener {
  @NonNull private final Context mContext;
  @NonNull private final BannerAdView mBannerAdView;
  @Nullable private MRAIDView mMraidView;
  @Nullable private Listener mListener;

  public MraidBannerViewModuleImpl(@NonNull Context context, @NonNull BannerAdView bannerAdView) {
    mContext = context;
    mBannerAdView = bannerAdView;
  }

  @Override
  public void setListener(@Nullable Listener listener) {
    mListener = listener;
  }

  @Override
  public void show(@NonNull String html, @NonNull String[] supportedNativeFeatures) {
    mMraidView = new MRAIDView(mContext, "http://" + MaxAds.HOST + "/", html, supportedNativeFeatures, this, this,
      false);
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

  @Override
  public void mraidViewLoaded(MRAIDView mraidView) {
    if (mListener != null) {
      mListener.onLoaded(mBannerAdView);
    }
  }

  @Override
  public void mraidViewExpand(MRAIDView mraidView) {
    if (mListener != null) {
      mListener.onExpanded(mBannerAdView);
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

  }

  @Override
  public void mraidNativeFeatureStorePicture(String url) {

  }

  @Override
  public void mraidNativeFeatureSendSms(String url) {

  }
}
