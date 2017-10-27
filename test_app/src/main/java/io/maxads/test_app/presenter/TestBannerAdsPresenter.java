package io.maxads.test_app.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.api.AdResponse;
import io.maxads.ads.base.api.WinnerResponse;
import io.maxads.ads.base.model.Winner;
import io.maxads.ads.base.util.TestAdInterceptor;
import io.maxads.test_app.TestAdReader;
import io.maxads.test_app.TestApp;
import io.maxads.test_app.model.TestAdItem;
import io.maxads.test_app.view.TestAdViewHolder;
import io.maxads.test_app.view.TestBannerAdsViewModule;

public class TestBannerAdsPresenter implements TestAdViewHolder.Listener, BannerAdView.Listener {
  @NonNull private static final String TAG = TestBannerAdsPresenter.class.getSimpleName();
  @NonNull private final TestAdReader mTestAdReader;
  @NonNull private final TestBannerAdsViewModule mTestBannerAdsViewModule;
  @NonNull private final List<TestAdItem> mTestAdItems;
  @NonNull private final TestAdInterceptor mTestAdInterceptor;

  public TestBannerAdsPresenter(@NonNull TestAdReader testAdReader,
                                @NonNull TestBannerAdsViewModule testBannerAdsViewModule,
                                @NonNull List<TestAdItem> testAdItems) {
    mTestAdReader = testAdReader;
    mTestBannerAdsViewModule = testBannerAdsViewModule;
    mTestBannerAdsViewModule.setAdItemListener(this);
    mTestBannerAdsViewModule.setAdViewListener(this);
    mTestAdItems = testAdItems;
    mTestAdInterceptor = TestApp.getTestAdInterceptor();
  }

  public void showTestAds() {
    mTestAdItems.clear();
    mTestAdItems.addAll(mTestAdReader.getTestAdItems());
    mTestBannerAdsViewModule.refreshTestAds();
  }

  public void destroy() {
    mTestBannerAdsViewModule.destroy();
  }

  @Override
  public void onAdItemClicked(int position) {
    final AdResponse adResponse = new AdResponse();
    final TestAdItem testAdItem = mTestAdItems.get(position);
    adResponse.creative = testAdItem.getAdPayload();
    adResponse.refresh = 9999;
    adResponse.impressionUrls = Collections.singletonList(testAdItem.getAdName() + "/impressionUrl");
    adResponse.clickUrls = Collections.singletonList(testAdItem.getAdName() + "/clickUrl");
    adResponse.selectedUrls = Collections.singletonList(testAdItem.getAdName() + "/selectedUrl");
    adResponse.errorUrls = Collections.singletonList(testAdItem.getAdName() + "/errorUrl");

    final WinnerResponse winnerResponse = new WinnerResponse();
    winnerResponse.creativeType = Winner.CreativeType.HTML.toString().toLowerCase(Locale.ROOT);
    adResponse.winner = winnerResponse;

    mTestAdInterceptor.setAdResponse(adResponse);
    mTestBannerAdsViewModule.loadTestAd("adUnitId:" + testAdItem.getAdName());
  }

  @Override
  public void onBannerLoaded(@NonNull BannerAdView bannerAdView) {
    Log.d(TAG, "Banner loaded");
  }

  @Override
  public void onBannerClicked(@NonNull BannerAdView bannerAdView) {
    Log.d(TAG, "Banner clicked");
  }

  @Override
  public void onBannerError(@NonNull BannerAdView bannerAdView) {
    Log.d(TAG, "Banner error");
  }
}
