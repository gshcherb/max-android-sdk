package io.maxads.test_app.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.test_app.R;
import io.maxads.test_app.TestAdReader;
import io.maxads.test_app.model.TestAdItem;
import io.maxads.test_app.presenter.BannerTestAdsPresenter;
import io.maxads.test_app.view.TestAdsAdapter;
import io.maxads.test_app.view.TestAdsViewModule;
import io.maxads.test_app.view.TestAdsViewModuleImpl;

public class MainActivity extends AppCompatActivity {

  public static final String TEST_MRAID_BANNER_PATH = "mraid_banner";
  @NonNull private BannerTestAdsPresenter mBannerTestAdsPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final RecyclerView recyclerView = findViewById(R.id.content);
    final List<TestAdItem> testAdItems = new ArrayList<>();
    final TestAdsAdapter testAdsAdapter = new TestAdsAdapter(this, testAdItems);
    final BannerAdView bannerAdView = findViewById(R.id.banner_ad);
    final TestAdsViewModule testAdsViewModule = new TestAdsViewModuleImpl(recyclerView, testAdsAdapter, bannerAdView);

    mBannerTestAdsPresenter = new BannerTestAdsPresenter(
      new TestAdReader(getResources().getAssets(), TEST_MRAID_BANNER_PATH),
      testAdsViewModule,
      testAdItems);
    mBannerTestAdsPresenter.showTestAds();
  }
}
