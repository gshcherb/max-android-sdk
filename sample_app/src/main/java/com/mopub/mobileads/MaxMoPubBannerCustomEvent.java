package com.mopub.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.mopub.common.Preconditions;

import java.util.Locale;
import java.util.Map;

import io.maxads.ads.banner.presenter.BannerPresenter;
import io.maxads.ads.banner.presenter.BannerPresenterFactory;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.api.RequestManager;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.MaxAdsLog;

public class MaxMoPubBannerCustomEvent extends CustomEventBanner implements BannerPresenter.Listener {
  @NonNull private static final String TAG = MaxMoPubBannerCustomEvent.class.getSimpleName();

  @NonNull private static final String ADUNIT_ID_KEY = "adunit_id";
  @NonNull private static final String REQUESTMANAGER_ID = "requestmanager_id";
  @Nullable private CustomEventBannerListener mBannerListener;
  @Nullable private BannerPresenter mBannerPresenter;

  @Override
  protected void loadBanner(Context context,
                            CustomEventBannerListener customEventBannerListener,
                            Map<String, Object> localExtras,
                            Map<String, String> serverExtras) {

    if (customEventBannerListener == null) {
      MaxAdsLog.e(TAG, "customEventBannerListener is null");
      return;
    }
    mBannerListener = customEventBannerListener;

    String maxAdUnitKey;
    if (localExtras.containsKey(ADUNIT_ID_KEY)) {
      maxAdUnitKey = (String) localExtras.get(ADUNIT_ID_KEY);
    } else if (serverExtras.containsKey(ADUNIT_ID_KEY)) {
      maxAdUnitKey = serverExtras.get(ADUNIT_ID_KEY);
    } else {
      MaxAdsLog.e(TAG, "Could not find MAX adunit_id value in CustomEventBanner localExtras or serverExtras");
      mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
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
      MaxAdsLog.e(TAG, "Could not find an ad in the cache for adunit with key: " + maxAdUnitKey);
      mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
      return;
    }

    mBannerPresenter = new BannerPresenterFactory(context).createBannerPresenter(ad, this);
    if (mBannerPresenter == null) {
      MaxAdsLog.e(TAG, "Could not create valid banner presenter");
      mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
      return;
    }

    mBannerPresenter.load();
  }

  @Override
  protected void onInvalidate() {
    if (mBannerPresenter != null) {
      mBannerPresenter.destroy();
      mBannerPresenter = null;
    }
  }

  @Override
  public void onBannerLoaded(@NonNull BannerPresenter bannerPresenter, @NonNull View banner) {
    if (mBannerListener != null) {
      mBannerListener.onBannerLoaded(banner);
    }
  }

  @Override
  public void onBannerClicked(@NonNull BannerPresenter bannerPresenter) {
    if (mBannerListener != null) {
      mBannerListener.onBannerClicked();
    }
  }

  @Override
  public void onBannerError(@NonNull BannerPresenter bannerPresenter) {
    if (mBannerListener != null) {
      mBannerListener.onBannerFailed(MoPubErrorCode.INTERNAL_ERROR);
    }
  }

  public static void addRequestManagerExtras(@NonNull Map<String, Object> localExtras, RequestManager requestManager) {
    Preconditions.checkNotNull(localExtras);
    localExtras.put(MaxMoPubBannerCustomEvent.REQUESTMANAGER_ID, String.valueOf(requestManager.hashCode()));
  }
}
