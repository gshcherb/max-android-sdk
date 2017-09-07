package io.maxads.maxads_sample.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.maxads_sample.R;

public class MainActivity extends AppCompatActivity implements BannerAdView.BannerAdListener, View.OnClickListener {

  private Button mLoadAdButton;
  private BannerAdView mBannerAdView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mLoadAdButton = findViewById(R.id.load_ad);
    mLoadAdButton.setOnClickListener(this);
    mBannerAdView = findViewById(R.id.ad);
    mBannerAdView.setBannerAdListener(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mBannerAdView.destroy();
  }

  @Override
  public void onClick(View view) {
    mBannerAdView.load("ag9zfm1heGFkcy0xNTY1MTlyEwsSBkFkVW5pdBiAgICAvKGCCQw");
  }

  @Override
  public void onAdLoaded(@NonNull BannerAdView bannerAdView) {
  }

  @Override
  public void onAdClicked(@NonNull BannerAdView bannerAdView) {
  }

  @Override
  public void onAdImpressed(@NonNull BannerAdView bannerAdView) {
  }

  @Override
  public void onAdError(@NonNull BannerAdView bannerAdView) {
  }
}