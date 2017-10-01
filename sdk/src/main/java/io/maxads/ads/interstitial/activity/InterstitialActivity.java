package io.maxads.ads.interstitial.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.R;
import io.maxads.ads.base.view.HtmlWebView;
import io.maxads.ads.interstitial.presenter.InterstitialActivityPresenter;
import io.maxads.ads.interstitial.view.InterstitialActivityViewModule;
import io.maxads.ads.interstitial.view.InterstitialActivityViewModuleImpl;

public class InterstitialActivity extends Activity {
  @NonNull public static final String HTML_KEY = "html_key";
  @NonNull public static final String BROADCAST_ID_KEY = "broadcast_id_key";

  @NonNull private InterstitialActivityPresenter mInterstitialActivityPresenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_interstitial);

    // TODO (steffan): do we care about locking the orientation of the device? How do we know?

    final Intent intent = getIntent();
    final String html = intent.getStringExtra(HTML_KEY);
    final long broadcastId = intent.getLongExtra(BROADCAST_ID_KEY, -1);

    final InterstitialActivityViewModule interstitialActivityViewModule =
      new InterstitialActivityViewModuleImpl(this, (HtmlWebView) findViewById(R.id.web_view));
    mInterstitialActivityPresenter =
      new InterstitialActivityPresenter(this, interstitialActivityViewModule, html, broadcastId);
    mInterstitialActivityPresenter.show();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    mInterstitialActivityPresenter.handleBackPressed();
  }
}
