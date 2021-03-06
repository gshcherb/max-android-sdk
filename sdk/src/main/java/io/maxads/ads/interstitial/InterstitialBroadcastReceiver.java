package io.maxads.ads.interstitial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import io.maxads.ads.interstitial.presenter.InterstitialPresenter;

public class InterstitialBroadcastReceiver extends BroadcastReceiver {
  @NonNull public static final String BROADCAST_ID = "broadcastId";
  @NonNull public static final String INTERSTITIAL_SHOW = "io.maxads.ads.interstitial.show";
  @NonNull public static final String INTERSTITIAL_CLICK = "io.maxads.ads.interstitial.click";
  @NonNull public static final String INTERSTITIAL_DISMISS = "io.maxads.ads.interstitial.dismiss";
  @NonNull public static final String INTERSTITIAL_ERROR = "io.maxads.ads.interstitial.error";

  @NonNull private final Context mContext;
  @NonNull private final InterstitialPresenter mInterstitialPresenter;
  private final long mBroadcastId;
  @Nullable private InterstitialPresenter.Listener mListener;

  public InterstitialBroadcastReceiver(@NonNull Context context,
                                       @NonNull InterstitialPresenter interstitialPresenter,
                                       long broadcastId) {
    mContext = context;
    mInterstitialPresenter = interstitialPresenter;
    mBroadcastId = broadcastId;
  }

  public void setListener(@Nullable InterstitialPresenter.Listener listener) {
    mListener = listener;
  }

  public void register(@NonNull IntentFilter intentFilter) {
    LocalBroadcastManager.getInstance(mContext).registerReceiver(this, intentFilter);
  }

  public void unregister() {
    LocalBroadcastManager.getInstance(mContext).unregisterReceiver(this);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    if (mListener == null) {
      return;
    }

    final long receivedId = intent.getLongExtra(BROADCAST_ID, -1);
    if (mBroadcastId != receivedId) {
      return;
    }

    final String action = intent.getAction();
    if (INTERSTITIAL_SHOW.equals(action)) {
      mListener.onInterstitialShown(mInterstitialPresenter);
    } else if (INTERSTITIAL_CLICK.equals(action)) {
      mListener.onInterstitialClicked(mInterstitialPresenter);
    } else if (INTERSTITIAL_DISMISS.equals(action)) {
      mListener.onInterstitialDismissed(mInterstitialPresenter);
      unregister();
    } else if (INTERSTITIAL_ERROR.equals(action)) {
      mListener.onInterstitialError(mInterstitialPresenter);
    }
  }
}
