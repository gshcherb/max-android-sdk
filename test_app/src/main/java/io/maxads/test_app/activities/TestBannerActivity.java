package io.maxads.test_app.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.util.Checks;
import io.maxads.test_app.R;
import io.maxads.test_app.TestAdReader;
import io.maxads.test_app.model.TestAdItem;
import io.maxads.test_app.presenter.TestBannerAdsPresenter;
import io.maxads.test_app.view.TestAdsAdapter;
import io.maxads.test_app.view.TestBannerAdsViewModule;
import io.maxads.test_app.view.TestBannerAdsViewModuleImpl;

public class TestBannerActivity extends AppCompatActivity {
  @NonNull private static final String TAG = TestBannerActivity.class.getSimpleName();
  @NonNull public static final String TEST_BANNER_PATH = "test_banner_path";
  @NonNull private TestBannerAdsPresenter mTestBannerAdsPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_banner);

    final String testBannerPath = getIntent().getStringExtra(TEST_BANNER_PATH);
    Checks.checkArgument(!TextUtils.isEmpty(testBannerPath), "Test interstitial path is empty");

    final RecyclerView recyclerView = findViewById(R.id.content);
    final List<TestAdItem> testAdItems = new ArrayList<>();
    final TestAdsAdapter testAdsAdapter = new TestAdsAdapter(this, testAdItems);
    final BannerAdView bannerAdView = findViewById(R.id.banner_ad);
    final TestBannerAdsViewModule testBannerAdsViewModule
      = new TestBannerAdsViewModuleImpl(recyclerView, testAdsAdapter, bannerAdView);

    mTestBannerAdsPresenter = new TestBannerAdsPresenter(
      new TestAdReader(getResources().getAssets(), testBannerPath),
      testBannerAdsViewModule,
      testAdItems);
    mTestBannerAdsPresenter.showTestAds();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mTestBannerAdsPresenter.destroy();
  }
}
