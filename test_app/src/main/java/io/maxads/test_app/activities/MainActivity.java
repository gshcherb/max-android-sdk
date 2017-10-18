package io.maxads.test_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.maxads.test_app.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  @NonNull private static final String TAG = MainActivity.class.getSimpleName();
  @NonNull private static final String TEST_MRAID_BANNER_PATH = "mraid_banner";
  @NonNull private static final String TEST_MRAID_INTERSTITIAL_PATH = "mraid_interstitial";
  @NonNull private static final String TEST_VAST_PATH = "vast";

  @NonNull private Button mMraidBanners;
  @NonNull private Button mMraidInterstitial;
  @NonNull private Button mVast;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mMraidBanners = findViewById(R.id.mraid_banners);
    mMraidBanners.setOnClickListener(this);
    mMraidInterstitial = findViewById(R.id.mraid_interstitials);
    mMraidInterstitial.setOnClickListener(this);
    mMraidInterstitial.setVisibility(View.GONE);
    mVast = findViewById(R.id.vast);
    mVast.setOnClickListener(this);
    mVast.setVisibility(View.GONE);
  }

  @Override
  public void onClick(View view) {
    final Intent intent = new Intent();
    if (view == mMraidBanners) {
      intent.setClass(this, TestBannerActivity.class)
        .putExtra(TestBannerActivity.TEST_BANNER_PATH, TEST_MRAID_BANNER_PATH);
      startActivity(intent);
    } else if (view == mMraidInterstitial) {
      intent.setClass(this, TestBannerActivity.class)
        .putExtra(TestBannerActivity.TEST_BANNER_PATH, TEST_MRAID_INTERSTITIAL_PATH);
      startActivity(intent);
    } else if (view == mVast) {
      intent.setClass(this, TestBannerActivity.class)
        .putExtra(TestBannerActivity.TEST_BANNER_PATH, TEST_VAST_PATH);
      startActivity(intent);
    }
  }
}

