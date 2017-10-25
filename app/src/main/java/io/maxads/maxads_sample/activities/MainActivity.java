package io.maxads.maxads_sample.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.api.RequestManager;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.interstitial.Interstitial;
import io.maxads.maxads_sample.R;

public class MainActivity extends AppCompatActivity implements BannerAdView.Listener, View.OnClickListener, Interstitial.Listener, MoPubView.BannerAdListener, MoPubInterstitial.InterstitialAdListener {

  private Ad mBannerAd;
  private RequestManager mBannerRequestManager;
  private RequestManager mInterstitialRequestManager;
  private Button mLoadBannerButton;
  private Button mLoadBannerMoPubButton;
  private Button mLoadInterstitialButton;
  private Button mLoadInterstitialMoPubButton;
  private MoPubView mMoPubView;
  private BannerAdView mBannerAdView;
  private MoPubInterstitial mMoPubInterstitial;
  private Interstitial mInterstitial;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mBannerRequestManager = new RequestManager();
    mInterstitialRequestManager = new RequestManager();

    mLoadBannerButton = findViewById(R.id.load_banner);
    mLoadBannerButton.setOnClickListener(this);
    mLoadBannerMoPubButton = findViewById(R.id.load_banner_mopub);
    mLoadBannerMoPubButton.setOnClickListener(this);
    mLoadInterstitialButton = findViewById(R.id.load_interstitial);
    mLoadInterstitialButton.setOnClickListener(this);
    mLoadInterstitialMoPubButton = findViewById(R.id.load_interstitial_mopub);
    mLoadInterstitialMoPubButton.setOnClickListener(this);
    mMoPubView = findViewById(R.id.mopub_ad);
    mMoPubView.setBannerAdListener(this);
    mMoPubView.setAutorefreshEnabled(false);
    mBannerAdView = findViewById(R.id.ad);
    mBannerAdView.setListener(this);
    mMoPubInterstitial = new MoPubInterstitial(this, "MOPUB_INTERSTITIAL_ADUNIT_ID");
    mMoPubInterstitial.setInterstitialAdListener(this);
    mInterstitial = new Interstitial(this);
    mInterstitial.setListener(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mMoPubView.destroy();
    mBannerAdView.destroy();
    mInterstitial.destroy();
  }

  @Override
  public void onClick(View view) {
    if (view == mLoadBannerButton) {
      mBannerAdView.load("ag9zfm1heGFkcy0xNTY1MTlyEwsSBkFkVW5pdBiAgICAvKGCCQw");
    } else if (view == mLoadBannerMoPubButton) {
      loadBannerMoPub();
    } else if (view == mLoadInterstitialButton) {
      mInterstitial.load("ag9zfm1heGFkcy0xNTY1MTlyEwsSBkFkVW5pdBiAgICAvKGCCQw");
    } else if (view == mLoadInterstitialMoPubButton) {
      loadInterstitialMoPub();
    }
  }

  private void loadBannerMoPub() {
    mBannerRequestManager.setRequestListener(new RequestManager.RequestListener() {
      @Override
      public void onRequestSuccess(@NonNull Ad ad) {
        mBannerAd = ad;
        mMoPubView.setAdUnitId("MOPUB_BANNER_ADUNIT_ID");
        mMoPubView.setKeywords(ad.getPrebidKeywords());
        mMoPubView.loadAd();
      }

      @Override
      public void onRequestFail(@NonNull Throwable throwable) {
        mBannerRequestManager.startRefreshTimer(RequestManager.DEFAULT_REFRESH_TIME_SECONDS);
      }
    });

    mBannerRequestManager.setAdUnitId("MAX_BANNER_ADUNIT_ID");
    mBannerRequestManager.requestAd();
  }

  private void loadInterstitialMoPub() {
    mInterstitialRequestManager.setRequestListener(new RequestManager.RequestListener() {
      @Override
      public void onRequestSuccess(@NonNull Ad ad) {
        mBannerAd = ad;
        mMoPubInterstitial.setKeywords(ad.getPrebidKeywords());
        mMoPubInterstitial.load();
      }

      @Override
      public void onRequestFail(@NonNull Throwable throwable) {
        mInterstitialRequestManager.startRefreshTimer(RequestManager.DEFAULT_REFRESH_TIME_SECONDS);
      }
    });

    mInterstitialRequestManager.setAdUnitId("MAX_INTERSTITIAL_ADUNIT_ID");
    mInterstitialRequestManager.requestAd();
  }

  // BannerAdView.Listener
  @Override
  public void onBannerLoaded(@NonNull BannerAdView bannerAdView) {
  }

  @Override
  public void onBannerClicked(@NonNull BannerAdView bannerAdView) {
  }

  @Override
  public void onBannerError(@NonNull BannerAdView bannerAdView) {
  }

  // Interstitial.Listener
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

  // MoPubView.BannerAdListener
  @Override
  public void onBannerLoaded(MoPubView banner) {
    mBannerRequestManager.startRefreshTimer(mBannerAd.getRefreshTimeSeconds());
  }

  @Override
  public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
    mBannerRequestManager.startRefreshTimer(mBannerAd.getRefreshTimeSeconds());
  }

  @Override
  public void onBannerClicked(MoPubView banner) {

  }

  @Override
  public void onBannerExpanded(MoPubView banner) {

  }

  @Override
  public void onBannerCollapsed(MoPubView banner) {

  }

  // MoPubInterstitial.InterstitialAdListener
  @Override
  public void onInterstitialLoaded(MoPubInterstitial interstitial) {
    mMoPubInterstitial.show();
  }

  @Override
  public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {

  }

  @Override
  public void onInterstitialShown(MoPubInterstitial interstitial) {

  }

  @Override
  public void onInterstitialClicked(MoPubInterstitial interstitial) {

  }

  @Override
  public void onInterstitialDismissed(MoPubInterstitial interstitial) {

  }
}
