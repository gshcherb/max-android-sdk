package io.maxads.ads.interstitial.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.Checks;
import io.maxads.ads.interstitial.vast.VASTPlayer;

public class VastInterstitialPresenter implements InterstitialPresenter, VASTPlayer.VASTPlayerListener {

  @NonNull private final Activity mActivity;
  @NonNull private final Ad mAd;

  @Nullable private VASTPlayer mVastPlayer;
  @Nullable private InterstitialPresenter.Listener mListener;
  private boolean mIsDestroyed;

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
    if (!Checks.NoThrow.checkArgument(!mIsDestroyed, "VastInterstitialPresenter is destroyed")) {
      return;
    }

    mVastPlayer = new VASTPlayer(mActivity, this);
    mVastPlayer.loadVideoWithData(mAd.getCreative());
  }

  @Override
  public void show() {
    if (!Checks.NoThrow.checkArgument(!mIsDestroyed, "VastInterstitialPresenter is destroyed")) {
      return;
    }

    if (mVastPlayer != null) {
      mVastPlayer.play();

      // TODO (steffan): ideally we fire the impression url after we confirm the video successfully plays
      if (mListener != null) {
        mListener.onInterstitialShown(this);
      }
    }
  }

  @Override
  public void destroy() {
    mListener = null;
    mIsDestroyed = true;
  }

  // VASTPlayerListener

  @Override
  public void vastReady() {
    if (mIsDestroyed) {
      return;
    }

    if (mListener != null) {
      mListener.onInterstitialLoaded(this);
    }
  }

  @Override
  public void vastError(int error) {
    if (mIsDestroyed) {
      return;
    }

    if (mListener != null) {
      mListener.onInterstitialError(this);
    }
  }

  @Override
  public void vastClick() {
    if (mIsDestroyed) {
      return;
    }

    if (mListener != null) {
      mListener.onInterstitialClicked(this);
    }
  }

  @Override
  public void vastComplete() {
  }

  @Override
  public void vastDismiss() {
    if (mIsDestroyed) {
      return;
    }

    if (mListener != null) {
      mListener.onInterstitialDismissed(this);
    }
  }
}
