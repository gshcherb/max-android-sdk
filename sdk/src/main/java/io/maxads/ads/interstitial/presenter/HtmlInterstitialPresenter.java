package io.maxads.ads.interstitial.presenter;

import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Random;

import io.maxads.ads.base.model.Ad;
import io.maxads.ads.interstitial.InterstitialBroadcastReceiver;

public class HtmlInterstitialPresenter implements InterstitialPresenter {
  @NonNull private final Context mContext;
  @NonNull private final Ad mAd;
  private final long mBroadcastId;

  @Nullable private InterstitialBroadcastReceiver mInterstitialBroadcastReceiver;
  @Nullable private InterstitialPresenter.Listener mListener;

  public HtmlInterstitialPresenter(@NonNull Context context, @NonNull Ad ad) {
    mContext = context;
    mAd = ad;
    mBroadcastId = new Random().nextLong();
  }

  @Override
  public void setListener(@Nullable Listener listener) {
    mListener = listener;
  }

  @NonNull
  @Override
  public Ad getAd() {
    return mAd;
  }

  @Override
  public void load() {
    mInterstitialBroadcastReceiver = new InterstitialBroadcastReceiver(mContext, this, mBroadcastId);
    mInterstitialBroadcastReceiver.setListener(mListener);

    final IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(InterstitialBroadcastReceiver.INTERSTITIAL_SHOW);
    intentFilter.addAction(InterstitialBroadcastReceiver.INTERSTITIAL_CLICK);
    intentFilter.addAction(InterstitialBroadcastReceiver.INTERSTITIAL_DISMISS);
    intentFilter.addAction(InterstitialBroadcastReceiver.INTERSTITIAL_ERROR);

    mInterstitialBroadcastReceiver.register(intentFilter);
  }

  @Override
  public void show() {
  }

  @Override
  public void destroy() {
    if (mInterstitialBroadcastReceiver != null) {
      mInterstitialBroadcastReceiver.unregister();
    }
  }
}
