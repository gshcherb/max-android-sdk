package com.mopub.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.AdCache;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.MaxAdsLog;
import io.maxads.ads.interstitial.presenter.InterstitialPresenter;
import io.maxads.ads.interstitial.presenter.InterstitialPresenterFactory;

import java.util.Map;


public class MaxMoPubInterstitialCustomEvent extends CustomEventInterstitial
    implements InterstitialPresenter.Listener {

  @NonNull private static final String ADUNIT_ID_KEY = "adunit_id";
  @Nullable private String mMaxAdUnitKey;
  @Nullable private CustomEventInterstitialListener mInterstitialListener;
  @Nullable private InterstitialPresenter mInterstitialPresenter;

  @Override
  protected void loadInterstitial(Context context,
                                  CustomEventInterstitialListener customEventInterstitialListener,
                                  Map<String, Object> localExtras,
                                  Map<String, String> serverExtras) {
    mInterstitialListener = customEventInterstitialListener;

    if (localExtras.containsKey(ADUNIT_ID_KEY)) {
      mMaxAdUnitKey = (String) localExtras.get(ADUNIT_ID_KEY);
    } else if (serverExtras.containsKey(ADUNIT_ID_KEY)) {
      mMaxAdUnitKey = serverExtras.get(ADUNIT_ID_KEY);
    } else {
      MaxAdsLog.e("Couldn't find max adunit_id value in CustomEvent localExtras or serverExtras");
      mInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
      return;
    }

    final Ad ad = MaxAds.getAdCache().remove(mMaxAdUnitKey);
    if (ad != null) {
      mInterstitialPresenter = new InterstitialPresenterFactory(context)
          .createInterstitialPresenter(ad, this);
      mInterstitialPresenter.load();
    } else {
      MaxAdsLog.e("Couldn't find an ad in the cache for adunit with key " + mMaxAdUnitKey);
      mInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
    }
  }

  @Override
  protected void showInterstitial() {
    if (mInterstitialPresenter != null) {
      mInterstitialPresenter.show();
    }
  }

  @Override
  protected void onInvalidate() {
    if (mInterstitialPresenter != null) {
      mInterstitialPresenter.destroy();
      mInterstitialPresenter = null;
    }
  }

  @Override
  public void onInterstitialLoaded(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialLoaded();
    }
  }

  @Override
  public void onInterstitialShown(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialShown();
    }
  }

  @Override
  public void onInterstitialClicked(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialClicked();
    }
  }

  @Override
  public void onInterstitialDismissed(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialDismissed();
    }
  }

  @Override
  public void onInterstitialError(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mInterstitialListener != null) {
      mInterstitialListener.onInterstitialFailed(MoPubErrorCode.INTERNAL_ERROR);
    }
  }
}
