package com.mopub.mobileads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mopub.common.Preconditions;

import java.util.Locale;
import java.util.Map;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.api.RequestManager;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.MaxAdsLog;
import io.maxads.ads.interstitial.presenter.InterstitialPresenter;
import io.maxads.ads.interstitial.presenter.InterstitialPresenterFactory;

public class MaxMoPubInterstitialCustomEvent extends CustomEventInterstitial implements InterstitialPresenter.Listener {
  @NonNull private static final String TAG = MaxMoPubInterstitialCustomEvent.class.getSimpleName();

  @NonNull private static final String ADUNIT_ID_KEY = "adunit_id";
  @NonNull private static final String REQUESTMANAGER_ID = "requestmanager_id";

  @Nullable private CustomEventInterstitialListener mInterstitialListener;
  @Nullable private InterstitialPresenter mInterstitialPresenter;

  @Override
  protected void loadInterstitial(Context context,
                                  CustomEventInterstitialListener customEventInterstitialListener,
                                  Map<String, Object> localExtras,
                                  Map<String, String> serverExtras) {

    if (customEventInterstitialListener == null) {
      MaxAdsLog.e(TAG, "customEventInterstitialListener is null");
      return;
    }
    mInterstitialListener = customEventInterstitialListener;

    if (!(context instanceof Activity)) {
      MaxAdsLog.e(TAG, "MAX interstitial ad can only be rendered with an Activity context");
      mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
      return;
    }
    final Activity activity = (Activity) context;

    String maxAdUnitKey;
    if (localExtras.containsKey(ADUNIT_ID_KEY)) {
      maxAdUnitKey = (String) localExtras.get(ADUNIT_ID_KEY);
    } else if (serverExtras.containsKey(ADUNIT_ID_KEY)) {
      maxAdUnitKey = serverExtras.get(ADUNIT_ID_KEY);
    } else {
      MaxAdsLog.e(TAG, "Could not find MAX adunit_id value in CustomEventInterstitial localExtras or serverExtras");
      mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
      return;
    }

    String requestManagerId = null;
    if (localExtras.containsKey(REQUESTMANAGER_ID)) {
      requestManagerId = (String) localExtras.get(REQUESTMANAGER_ID);
    }

    String adCacheKey;
    if (TextUtils.isEmpty(requestManagerId)) {
      adCacheKey = maxAdUnitKey;
    } else {
      adCacheKey = String.format(Locale.US, "%s_%s", maxAdUnitKey, requestManagerId);
    }

    final Ad ad = MaxAds.getAdCache().remove(adCacheKey);
    if (ad == null) {
      MaxAdsLog.e(TAG, "Could not find an ad in the cache for adunit with key " + maxAdUnitKey);
      mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
      return;
    }

    mInterstitialPresenter = new InterstitialPresenterFactory(activity).createInterstitialPresenter(ad, this);
    if (mInterstitialPresenter == null) {
      MaxAdsLog.e(TAG, "Could not create valid interstitial presenter");
      mInterstitialListener.onInterstitialFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
      return;
    }

    mInterstitialPresenter.load();
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

  public static void addRequestManagerExtras(@NonNull Map<String, Object> localExtras, RequestManager requestManager) {
    Preconditions.checkNotNull(localExtras);
    localExtras.put(REQUESTMANAGER_ID, String.valueOf(requestManager.hashCode()));
  }

}
