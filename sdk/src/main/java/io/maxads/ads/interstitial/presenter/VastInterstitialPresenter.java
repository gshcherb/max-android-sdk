package io.maxads.ads.interstitial.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.model.Ad;
import io.maxads.vast.VASTPlayer;

public class VastInterstitialPresenter implements InterstitialPresenter, VASTPlayer.VASTPlayerListener {

  @NonNull private final Activity mActivity;
  @NonNull private final Ad mAd;

  @Nullable private VASTPlayer mVastPlayer;
  @Nullable private InterstitialPresenter.Listener mListener;

  public VastInterstitialPresenter(@NonNull Activity activity, @NonNull Ad ad) {
    mActivity = activity;
    mAd = ad;
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
    mVastPlayer = new VASTPlayer(mActivity, this);
    mVastPlayer.loadVideoWithData(mAd.getCreative());
  }

  @Override
  public void show() {
    if (mVastPlayer != null) {
      mVastPlayer.play();
    }
  }

  @Override
  public void destroy() {
  }

  // VASTPlayerListener

  @Override
  public void vastReady() {
    if (mListener != null) {
      mListener.onInterstitialLoaded(this);
    }
  }

  @Override
  public void vastError(int error) {
    if (mListener != null) {
      mListener.onInterstitialError(this);
    }
  }

  @Override
  public void vastClick() {
    if (mListener != null) {
      mListener.onInterstitialClicked(this);
    }
  }

  @Override
  public void vastComplete() {
  }

  @Override
  public void vastDismiss() {
    if (mListener != null) {
      mListener.onInterstitialDismissed(this);
    }
  }
}
