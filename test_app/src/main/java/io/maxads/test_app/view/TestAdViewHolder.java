package io.maxads.test_app.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.maxads.test_app.R;

public class TestAdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  public interface Listener {
    void onAdItemClicked(int position);
  }

  @NonNull public final TextView title;
  @Nullable private Listener mListener;

  public TestAdViewHolder(View itemView) {
    super(itemView);
    title = itemView.findViewById(R.id.title);
    itemView.setOnClickListener(this);
  }

  public void setListener(@Nullable Listener listener) {
    mListener = listener;
  }

  @Override
  public void onClick(View view) {
    if (mListener == null || getAdapterPosition() == RecyclerView.NO_POSITION) {
      return;
    }

    mListener.onAdItemClicked(getAdapterPosition());
  }
}
