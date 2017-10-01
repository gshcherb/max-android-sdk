package io.maxads.ads.interstitial;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.api.RequestManager;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.interstitial.presenter.InterstitialPresenter;
import io.maxads.ads.interstitial.presenter.InterstitialPresenterFactory;

public class Interstitial implements RequestManager.RequestListener, InterstitialPresenter.Listener {
  public interface Listener {
    void onInterstitialLoaded(@NonNull Interstitial interstitial);
    void onInterstitialShown(@NonNull Interstitial interstitial);
    void onInterstitialClicked(@NonNull Interstitial interstitial);
    void onInterstitialDismissed(@NonNull Interstitial interstitial);
    void onInterstitialError(@NonNull Interstitial interstitial);
  }

  @NonNull private final InterstitialPresenterFactory mInterstitialPresenterFactory;
  @NonNull private final RequestManager mRequestManager;
  @Nullable private InterstitialPresenter mInterstitialPresenter;
  @Nullable private Listener mListener;

  public Interstitial(@NonNull Context context) {
    mInterstitialPresenterFactory = new InterstitialPresenterFactory(context);
    mRequestManager = new RequestManager();
    mRequestManager.setRequestListener(this);
  }

  public void setListener(@Nullable Listener listener) {
    mListener = listener;
  }

  public void load(@Nullable String adUnitId) {
    if (adUnitId == null) {
      return;
    }

    mRequestManager.requestAd(adUnitId);
  }

  private void loadInterstitial(@NonNull Ad ad) {
    mInterstitialPresenter = mInterstitialPresenterFactory.createInterstitialPresenter(ad, this);
    mInterstitialPresenter.load();
  }

  public void show() {
    if (mInterstitialPresenter == null) {
      return;
    }

    mInterstitialPresenter.show();
  }

  public void destroy() {
    mRequestManager.destroy();
    if (mInterstitialPresenter != null) {
      mInterstitialPresenter.destroy();
    }
    mInterstitialPresenter = null;
    mListener = null;
  }

  @Override
  public void onRequestSuccess(@NonNull Ad ad) {
    loadInterstitial(ad);
  }

  @Override
  public void onRequestFail(@NonNull Throwable throwable) {

  }

  @Override
  public void onInterstitialLoaded(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mListener != null) {
      mListener.onInterstitialLoaded(this);
    }
  }

  @Override
  public void onInterstitialShown(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mListener != null) {
      mListener.onInterstitialShown(this);
    }
  }

  @Override
  public void onInterstitialClicked(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mListener != null) {
      mListener.onInterstitialClicked(this);
    }
  }

  @Override
  public void onInterstitialDismissed(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mInterstitialPresenter != null) {
      mInterstitialPresenter.destroy();
      mInterstitialPresenter = null;
    }

    if (mListener != null) {
      mListener.onInterstitialDismissed(this);
    }
  }

  @Override
  public void onInterstitialError(@NonNull InterstitialPresenter interstitialPresenter) {
    if (mListener != null) {
      mListener.onInterstitialError(this);
    }
  }
}