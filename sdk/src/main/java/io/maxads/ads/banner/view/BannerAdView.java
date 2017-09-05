package io.maxads.ads.banner.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import io.maxads.ads.banner.controller.BannerAdController;

public class BannerAdView extends FrameLayout {
  public interface BannerAdListener {
    void onAdLoaded(@NonNull BannerAdView bannerAdView);
    void onAdClicked(@NonNull BannerAdView bannerAdView);
    void onAdImpressed(@NonNull BannerAdView bannerAdView);
    void onAdError(@NonNull BannerAdView bannerAdView);
  }

  @NonNull private final BannerAdController mBannerAdController;

  public BannerAdView(@NonNull Context context) {
    this(context, null);
  }

  public BannerAdView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    mBannerAdController = new BannerAdController(context);

    setHorizontalScrollBarEnabled(false);
    setVerticalScrollBarEnabled(false);
  }

  public void setBannerAdListener(@Nullable BannerAdListener bannerAdListener) {
    mBannerAdController.setBannerAdListener(bannerAdListener);
  }

  public void load(@Nullable String adUnitId) {
    if (adUnitId == null) {
      return;
    }

    mBannerAdController.load(adUnitId, this);
  }

  public void destroy() {
    removeAllViews();
    mBannerAdController.destroy();
  }
}
