package com.mopub.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Map;

import io.maxads.ads.banner.presenter.BannerPresenter;
import io.maxads.ads.banner.presenter.BannerPresenterFactory;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.MaxAdsLog;

public class MaxMoPubBannerCustomEvent extends CustomEventBanner
    implements BannerPresenter.Listener {

  @NonNull private static final String ADUNIT_ID_KEY = "adunit_id";
  @Nullable private String mMaxAdUnitKey;
  @Nullable private CustomEventBannerListener mBannerListener;
  @Nullable private BannerPresenter mBannerPresenter;

  @Override
  protected void loadBanner(Context context,
                            CustomEventBannerListener customEventBannerListener,
                            Map<String, Object> localExtras,
                            Map<String, String> serverExtras) {
    mBannerListener = customEventBannerListener;

    if (localExtras.containsKey(ADUNIT_ID_KEY)) {
      mMaxAdUnitKey = (String) localExtras.get(ADUNIT_ID_KEY);
    } else if (serverExtras.containsKey(ADUNIT_ID_KEY)) {
      mMaxAdUnitKey = serverExtras.get(ADUNIT_ID_KEY);
    } else {
      MaxAdsLog.e("Couldn't find max adunit_id value in CustomEvent localExtras or serverExtras");
      mBannerListener.onBannerFailed(MoPubErrorCode.INTERNAL_ERROR);
      return;
    }

    final Ad ad = MaxAds.getAdCache().remove(mMaxAdUnitKey);
    if (ad != null) {
      mBannerPresenter = new BannerPresenterFactory(context).createBannerPresenter(ad, this);
      mBannerPresenter.load();
    } else {
      MaxAdsLog.e("Couldn't find an ad in the cache for adunit with key " + mMaxAdUnitKey);
      mBannerListener.onBannerFailed(MoPubErrorCode.INTERNAL_ERROR);
    }
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
}
