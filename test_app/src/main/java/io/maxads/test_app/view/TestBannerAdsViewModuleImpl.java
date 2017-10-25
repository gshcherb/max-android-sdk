package io.maxads.test_app.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.maxads.ads.banner.view.BannerAdView;

public class TestBannerAdsViewModuleImpl implements TestBannerAdsViewModule {

  @NonNull private final RecyclerView mRecyclerView;
  @NonNull private final TestAdsAdapter mTestAdsAdapter;
  @NonNull private final BannerAdView mBannerAdView;

  public TestBannerAdsViewModuleImpl(@NonNull RecyclerView recyclerView, @NonNull TestAdsAdapter testAdsAdapter,
                                     @NonNull BannerAdView bannerAdView) {
    mRecyclerView = recyclerView;
    final Context context = mRecyclerView.getContext();
    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
    mRecyclerView.setLayoutManager(linearLayoutManager);
    mRecyclerView.addItemDecoration(new DividerItemDecoration(context, linearLayoutManager.getOrientation()));
    mTestAdsAdapter = testAdsAdapter;
    mRecyclerView.setAdapter(mTestAdsAdapter);
    mBannerAdView = bannerAdView;
  }

  @Override
  public void setAdItemListener(@Nullable TestAdViewHolder.Listener listener) {
    mTestAdsAdapter.setListener(listener);
  }

  @Override
  public void setAdViewListener(@Nullable BannerAdView.Listener listener) {
    mBannerAdView.setListener(listener);
  }

  @Override
  public void destroy() {
    mBannerAdView.destroy();
  }

  @Override
  public void refreshTestAds() {
    mTestAdsAdapter.notifyDataSetChanged();
  }

  @Override
  public void loadTestAd(@NonNull String adUnitId) {
    mBannerAdView.load(adUnitId);
  }
}
