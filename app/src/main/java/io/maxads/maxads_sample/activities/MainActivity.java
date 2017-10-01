package io.maxads.maxads_sample.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.interstitial.Interstitial;
import io.maxads.maxads_sample.R;

public class MainActivity extends AppCompatActivity implements BannerAdView.Listener, View.OnClickListener, Interstitial.Listener {

  private Button mLoadBannerButton;
  private Button mLoadInterstitialButton;
  private BannerAdView mBannerAdView;
  private Interstitial mInterstitial;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mLoadBannerButton = findViewById(R.id.load_banner);
    mLoadBannerButton.setOnClickListener(this);
    mLoadInterstitialButton = findViewById(R.id.load_interstitial);
    mLoadInterstitialButton.setOnClickListener(this);
    mBannerAdView = findViewById(R.id.ad);
    mBannerAdView.setListener(this);
    mInterstitial = new Interstitial(this);
    mInterstitial.setListener(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mBannerAdView.destroy();
    mInterstitial.destroy();
  }

  @Override
  public void onClick(View view) {
    if (view == mLoadBannerButton) {
      mBannerAdView.load("ag9zfm1heGFkcy0xNTY1MTlyEwsSBkFkVW5pdBiAgICAvKGCCQw");
    } else if (view == mLoadInterstitialButton) {
      mInterstitial.load("ag9zfm1heGFkcy0xNTY1MTlyEwsSBkFkVW5pdBiAgICAvKGCCQw");
    }
  }

  @Override
  public void onBannerLoaded(@NonNull BannerAdView bannerAdView) {
  }

  @Override
  public void onBannerClicked(@NonNull BannerAdView bannerAdView) {
  }

  @Override
  public void onBannerError(@NonNull BannerAdView bannerAdView) {
  }

  @Override
  public void onInterstitialLoaded(@NonNull Interstitial interstitial) {
    mInterstitial.show();
  }

  @Override
  public void onInterstitialShown(@NonNull Interstitial interstitial) {

  }

  @Override
  public void onInterstitialClicked(@NonNull Interstitial interstitial) {

  }

  @Override
  public void onInterstitialDismissed(@NonNull Interstitial interstitial) {

  }

  @Override
  public void onInterstitialError(@NonNull Interstitial interstitial) {

  }
}
