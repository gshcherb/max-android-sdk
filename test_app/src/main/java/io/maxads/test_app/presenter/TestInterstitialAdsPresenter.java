package io.maxads.test_app.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.maxads.ads.base.api.AdResponse;
import io.maxads.ads.base.api.WinnerResponse;
import io.maxads.ads.base.model.Winner;
import io.maxads.ads.base.util.TestAdInterceptor;
import io.maxads.ads.interstitial.Interstitial;
import io.maxads.test_app.TestAdReader;
import io.maxads.test_app.TestApp;
import io.maxads.test_app.model.TestAdItem;
import io.maxads.test_app.view.TestAdViewHolder;
import io.maxads.test_app.view.TestInterstitialAdsViewModule;

public class TestInterstitialAdsPresenter implements TestAdViewHolder.Listener, Interstitial.Listener {
  @NonNull private static final String TAG = TestBannerAdsPresenter.class.getSimpleName();
  @NonNull private final TestAdReader mTestAdReader;
  @NonNull private final TestInterstitialAdsViewModule mTestInterstitialAdsViewModule;
  @NonNull private final Interstitial mInterstitial;
  @NonNull private final Winner.CreativeType mCreativeType;
  @NonNull private final List<TestAdItem> mTestAdItems;
  @NonNull private final TestAdInterceptor mTestAdInterceptor;

  public TestInterstitialAdsPresenter(@NonNull TestAdReader testAdReader,
                                      @NonNull TestInterstitialAdsViewModule testInterstitialAdsViewModule,
                                      @NonNull Interstitial interstitial,
                                      @NonNull Winner.CreativeType creativeType,
                                      @NonNull List<TestAdItem> testAdItems) {
    mTestAdReader = testAdReader;
    mTestInterstitialAdsViewModule = testInterstitialAdsViewModule;
    mInterstitial = interstitial;
    mInterstitial.setListener(this);
    mCreativeType = creativeType;
    mTestInterstitialAdsViewModule.setAdItemListener(this);
    mTestAdItems = testAdItems;
    mTestAdInterceptor = TestApp.getTestAdInterceptor();
  }

  public void showTestAds() {
    mTestAdItems.clear();
    mTestAdItems.addAll(mTestAdReader.getTestAdItems());
    mTestInterstitialAdsViewModule.refreshTestAds();
  }

  public void destroy() {
    mInterstitial.destroy();
  }

  @Override
  public void onAdItemClicked(int position) {
    final AdResponse adResponse = new AdResponse();
    final TestAdItem testAdItem = mTestAdItems.get(position);
    adResponse.creative = testAdItem.getAdPayload();
    adResponse.refresh = 0;
    adResponse.impressionUrls = Collections.singletonList(testAdItem.getAdName() + "/impressionUrl");
    adResponse.clickUrls = Collections.singletonList(testAdItem.getAdName() + "/clickUrl");
    adResponse.selectedUrls = Collections.singletonList(testAdItem.getAdName() + "/selectedUrl");
    adResponse.errorUrls = Collections.singletonList(testAdItem.getAdName() + "/errorUrl");

    final WinnerResponse winnerResponse = new WinnerResponse();
    winnerResponse.creativeType = mCreativeType.toString().toLowerCase(Locale.ROOT);
    adResponse.winner = winnerResponse;

    mTestAdInterceptor.setAdResponse(adResponse);
    mInterstitial.load("adUnitId:" + testAdItem.getAdName());
  }

  @Override
  public void onInterstitialLoaded(@NonNull Interstitial interstitial) {
    Log.d(TAG, "Interstitial loaded");
    mInterstitial.show();
  }

  @Override
  public void onInterstitialShown(@NonNull Interstitial interstitial) {
    Log.d(TAG, "Interstitial shown");
  }

  @Override
  public void onInterstitialClicked(@NonNull Interstitial interstitial) {
    Log.d(TAG, "Interstitial clicked");
  }

  @Override
  public void onInterstitialDismissed(@NonNull Interstitial interstitial) {
    Log.d(TAG, "Interstitial dismissed");
  }

  @Override
  public void onInterstitialError(@NonNull Interstitial interstitial) {
    Log.d(TAG, "Interstitial error");
  }
}
