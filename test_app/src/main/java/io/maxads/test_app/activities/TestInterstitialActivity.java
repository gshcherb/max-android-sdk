package io.maxads.test_app.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.maxads.ads.base.model.Winner;
import io.maxads.ads.base.util.Checks;
import io.maxads.ads.interstitial.Interstitial;
import io.maxads.test_app.R;
import io.maxads.test_app.TestAdReader;
import io.maxads.test_app.model.TestAdItem;
import io.maxads.test_app.presenter.TestInterstitialAdsPresenter;
import io.maxads.test_app.view.TestAdsAdapter;
import io.maxads.test_app.view.TestInterstitialAdsViewModule;
import io.maxads.test_app.view.TestInterstitialAdsViewModuleImpl;

public class TestInterstitialActivity extends AppCompatActivity {
  @NonNull private static final String TAG = TestInterstitialActivity.class.getSimpleName();
  @NonNull public static final String TEST_INTERSTITIAL_PATH = "test_interstitial_path";
  @NonNull public static final String TEST_CREATIVE_TYPE = "test_creative_type";
  @NonNull private TestInterstitialAdsPresenter mTestInterstitialAdsPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_interstitial);

    final String testInterstitialPath = getIntent().getStringExtra(TEST_INTERSTITIAL_PATH);
    final String testCreativeType = getIntent().getStringExtra(TEST_CREATIVE_TYPE);
    Checks.checkArgument(!TextUtils.isEmpty(testInterstitialPath), "Test interstitial path is empty");
    Checks.checkArgument(!TextUtils.isEmpty(testCreativeType), "Test creative type is empty");

    final RecyclerView recyclerView = findViewById(R.id.content);
    final List<TestAdItem> testAdItems = new ArrayList<>();
    final TestAdsAdapter testAdsAdapter = new TestAdsAdapter(this, testAdItems);
    final TestInterstitialAdsViewModule testInterstitialAdsViewModule
      = new TestInterstitialAdsViewModuleImpl(recyclerView, testAdsAdapter);

    mTestInterstitialAdsPresenter = new TestInterstitialAdsPresenter(
      new TestAdReader(getResources().getAssets(), testInterstitialPath),
      testInterstitialAdsViewModule,
      new Interstitial(this),
      Winner.CreativeType.from(testCreativeType),
      testAdItems);
    mTestInterstitialAdsPresenter.showTestAds();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mTestInterstitialAdsPresenter.destroy();
  }
}

