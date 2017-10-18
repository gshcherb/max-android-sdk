package io.maxads.test_app.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class TestInterstitialAdsViewModuleImpl implements TestInterstitialAdsViewModule {
  @NonNull private final RecyclerView mRecyclerView;
  @NonNull private final TestAdsAdapter mTestAdsAdapter;

  public TestInterstitialAdsViewModuleImpl(@NonNull RecyclerView recyclerView, @NonNull TestAdsAdapter testAdsAdapter) {
    mRecyclerView = recyclerView;
    final Context context = mRecyclerView.getContext();
    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
    mRecyclerView.setLayoutManager(linearLayoutManager);
    mRecyclerView.addItemDecoration(new DividerItemDecoration(context, linearLayoutManager.getOrientation()));
    mTestAdsAdapter = testAdsAdapter;
    mRecyclerView.setAdapter(mTestAdsAdapter);
  }

  @Override
  public void setAdItemListener(@Nullable TestAdViewHolder.Listener listener) {
    mTestAdsAdapter.setListener(listener);
  }

  @Override
  public void refreshTestAds() {
    mTestAdsAdapter.notifyDataSetChanged();
  }
}
