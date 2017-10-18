package io.maxads.test_app.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.maxads.test_app.R;
import io.maxads.test_app.model.TestAdItem;

public class TestAdsAdapter extends RecyclerView.Adapter<TestAdViewHolder> {
  @NonNull private final LayoutInflater mLayoutInflater;
  @NonNull private final List<TestAdItem> mTestAdItems;
  @Nullable private TestAdViewHolder.Listener mListener;

  public TestAdsAdapter(@NonNull Context context, @NonNull List<TestAdItem> testAdItems) {
    mLayoutInflater = LayoutInflater.from(context);
    mTestAdItems = testAdItems;
  }

  public void setListener(@Nullable TestAdViewHolder.Listener listener) {
    mListener = listener;
  }

  @Override
  public TestAdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final TestAdViewHolder testAdViewHolder
      = new TestAdViewHolder(mLayoutInflater.inflate(R.layout.test_ad_item, parent, false));
    testAdViewHolder.setListener(mListener);
    return testAdViewHolder;
  }

  @Override
  public void onBindViewHolder(TestAdViewHolder holder, int position) {
    holder.title.setText(mTestAdItems.get(position).getAdName());
  }

  @Override
  public int getItemCount() {
    return mTestAdItems.size();
  }
}
