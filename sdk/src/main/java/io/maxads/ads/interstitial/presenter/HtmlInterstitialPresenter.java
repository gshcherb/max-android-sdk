package io.maxads.ads.interstitial.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.model.Ad;

public class HtmlInterstitialPresenter implements InterstitialPresenter {
  @NonNull private final Context mContext;
  @NonNull private final Ad mAd;
  @Nullable private InterstitialPresenter.Listener mListener;

  public HtmlInterstitialPresenter(@NonNull Context context, @NonNull Ad ad) {
    mContext = context;
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

  }

  @Override
  public void show() {

  }

  @Override
  public void destroy() {

  }
}
