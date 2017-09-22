package io.maxads.ads.banner.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import io.maxads.ads.banner.controller.BannerController;

public class BannerAdView extends FrameLayout {
  public interface Listener {
    void onBannerLoaded(@NonNull BannerAdView bannerAdView);
    void onBannerClicked(@NonNull BannerAdView bannerAdView);
    void onBannerError(@NonNull BannerAdView bannerAdView);
  }

  @NonNull private final BannerController mBannerController;

  public BannerAdView(@NonNull Context context) {
    this(context, null);
  }

  public BannerAdView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    mBannerController = new BannerController(context);

    setHorizontalScrollBarEnabled(false);
    setVerticalScrollBarEnabled(false);
  }

  public void setListener(@Nullable Listener listener) {
    mBannerController.setListener(listener);
  }

  public void load(@Nullable String adUnitId) {
    if (adUnitId == null) {
      return;
    }

    mBannerController.load(adUnitId, this);
  }

  public void destroy() {
    removeAllViews();
    mBannerController.destroy();
  }
}
