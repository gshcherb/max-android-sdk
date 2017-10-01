package io.maxads.ads.interstitial.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.view.HtmlWebView;
import io.maxads.ads.base.view.HtmlWebViewClient;

public class InterstitialActivityViewModuleImpl implements InterstitialActivityViewModule, View.OnClickListener {
  @NonNull private final HtmlWebView mHtmlWebView;
  @Nullable private Listener mListener;

  public InterstitialActivityViewModuleImpl(@NonNull Context context, @NonNull HtmlWebView htmlWebView) {
    mHtmlWebView = htmlWebView;
    mHtmlWebView.setWebViewClient(new HtmlWebViewClient(context));
    mHtmlWebView.setOnClickListener(this);
  }

  @Override
  public void setListener(@Nullable Listener listener) {
    mListener = listener;
  }

  @Override
  public void show(@NonNull String html) {
    mHtmlWebView.loadDataWithBaseURL("http://" + MaxAds.HOST + "/", html, "text/html", "utf-8", null);
  }

  @Override
  public void onClick(View view) {
    if (mListener == null) {
      return;
    }

    if (view == mHtmlWebView) {
      mListener.onInterstitialClicked();
    }
  }
}
