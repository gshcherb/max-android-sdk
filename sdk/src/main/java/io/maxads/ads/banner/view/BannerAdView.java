package io.maxads.ads.banner.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import io.maxads.ads.banner.controller.BannerController;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.util.Checks;

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
    if (!Checks.NoThrow.checkArgument(MaxAds.isInitialized(), "MaxAds SDK has not been initialized. " +
      "Please call MaxAds#initialize in your application's onCreate method.")) {
      return;
    }

    if (!Checks.NoThrow.checkNotNull(adUnitId, "adUnitId cannot be null")) {
      return;
    }

    mBannerController.load(adUnitId, this);
  }

  public void destroy() {
    removeAllViews();
    mBannerController.destroy();
  }
}
