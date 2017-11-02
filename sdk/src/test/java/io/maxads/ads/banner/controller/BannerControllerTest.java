package io.maxads.ads.banner.controller;

import android.view.View;
import android.widget.FrameLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import io.maxads.ads.banner.presenter.BannerPresenter;
import io.maxads.ads.banner.presenter.BannerPresenterFactory;
import io.maxads.ads.banner.view.BannerAdView;
import io.maxads.ads.base.api.RequestManager;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.InitializationHelper;
import io.maxads.ads.util.TestUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class BannerControllerTest {
  @Mock private BannerPresenterFactory mMockBannerPresenterFactory;
  @Mock private RequestManager mMockRequestManager;
  @Mock private InitializationHelper mMockInitializationHelper;
  @Mock private BannerAdView mMockBannerAdView;
  @Mock private BannerPresenter mMockBannerPresenter;
  @Mock private View mMockView;
  @Mock private BannerAdView.Listener mMockListener;
  private Ad mTestAd;

  @InjectMocks private BannerController mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mTestAd = TestUtil.createTestAd();
  }

  @Test
  public void load() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);

    mSubject.load(mTestAd.getAdUnitId(), mMockBannerAdView);

    verify(mMockRequestManager).setAdUnitId(mTestAd.getAdUnitId());
    verify(mMockRequestManager).requestAd();
    verify(mMockRequestManager).stopRefreshTimer();
  }

  @Test
  public void load_withSDKUninitialized() {
    mSubject.load(mTestAd.getAdUnitId(), mMockBannerAdView);

    verify(mMockRequestManager, never()).setAdUnitId(anyString());
    verify(mMockRequestManager, never()).requestAd();
    verify(mMockRequestManager, never()).stopRefreshTimer();
  }

  @Test
  public void load_withNullAdUnitId() {
    mSubject.load(null, mMockBannerAdView);

    verify(mMockRequestManager, never()).setAdUnitId(anyString());
    verify(mMockRequestManager, never()).requestAd();
    verify(mMockRequestManager, never()).stopRefreshTimer();
  }

  @Test
  public void load_withNullBannerAdView() {
    mSubject.load(mTestAd.getAdUnitId(), null);

    verify(mMockRequestManager, never()).setAdUnitId(anyString());
    verify(mMockRequestManager, never()).requestAd();
    verify(mMockRequestManager, never()).stopRefreshTimer();
  }

  @Test
  public void load_whenDestroyed() {
    mSubject.destroy();

    mSubject.load(mTestAd.getAdUnitId(), mMockBannerAdView);

    verify(mMockRequestManager, never()).setAdUnitId(anyString());
    verify(mMockRequestManager, never()).requestAd();
    verify(mMockRequestManager, never()).stopRefreshTimer();
  }

  @Test
  public void showAd() {
    when(mMockBannerPresenterFactory.createBannerPresenter(mTestAd, mSubject))
      .thenReturn(mMockBannerPresenter);

    mSubject.showAd(mTestAd);

    verify(mMockBannerPresenter).load();
  }

  @Test
  public void destroy() {
    mSubject.destroy();

    verify(mMockRequestManager).destroy();
  }

  @Test
  public void showAd_withNullPresenter() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);
    when(mMockBannerPresenterFactory.createBannerPresenter(mTestAd, mSubject))
      .thenReturn(null);
    mSubject.setListener(mMockListener);
    mSubject.load(mTestAd.getAdUnitId(), mMockBannerAdView);

    mSubject.showAd(mTestAd);

    verify(mMockRequestManager).startRefreshTimer(mTestAd.getRefreshTimeSeconds());
    verify(mMockListener).onBannerError(mMockBannerAdView);
    verify(mMockBannerPresenter, never()).load();
  }

  @Test
  public void showAd_whenDestroyed() {
    mSubject.destroy();

    mSubject.showAd(mTestAd);

    verify(mMockBannerPresenterFactory, never()).createBannerPresenter(any(Ad.class), any(BannerPresenter.Listener.class));
    verify(mMockRequestManager, never()).startRefreshTimer(mTestAd.getRefreshTimeSeconds());
    verify(mMockListener, never()).onBannerError(mMockBannerAdView);
    verify(mMockBannerPresenter, never()).load();
  }

  @Test
  public void onRequestFail() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);
    mSubject.setListener(mMockListener);
    mSubject.load(mTestAd.getAdUnitId(), mMockBannerAdView);

    mSubject.onRequestFail(new Exception());

    verify(mMockRequestManager).startRefreshTimer(RequestManager.DEFAULT_REFRESH_TIME_SECONDS);
    verify(mMockListener).onBannerError(mMockBannerAdView);
  }

  @Test
  public void onRequestFail_whenDestroyed() {
    mSubject.destroy();

    mSubject.onRequestFail(new Exception());

    verify(mMockRequestManager, never()).startRefreshTimer(anyLong());
    verify(mMockListener, never()).onBannerError(any(BannerAdView.class));
  }

  @Test
  public void onBannerLoaded() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);
    when(mMockBannerPresenter.getAd()).thenReturn(mTestAd);
    mSubject.setListener(mMockListener);
    mSubject.load(mTestAd.getAdUnitId(), mMockBannerAdView);

    mSubject.onBannerLoaded(mMockBannerPresenter, mMockView);

    verify(mMockRequestManager).startRefreshTimer(mTestAd.getRefreshTimeSeconds());
    verify(mMockBannerAdView).removeAllViews();
    verify(mMockView).setLayoutParams(any(FrameLayout.LayoutParams.class));
    verify(mMockBannerAdView).addView(mMockView);
    verify(mMockListener).onBannerLoaded(mMockBannerAdView);
  }

  @Test
  public void onBannerLoaded_whenDestroyed() {
    mSubject.destroy();

    mSubject.onBannerLoaded(mMockBannerPresenter, mMockView);

    verify(mMockRequestManager, never()).startRefreshTimer(anyLong());
    verify(mMockBannerAdView, never()).removeAllViews();
    verify(mMockView, never()).setLayoutParams(any(FrameLayout.LayoutParams.class));
    verify(mMockBannerAdView, never()).addView(any(View.class));
    verify(mMockListener, never()).onBannerLoaded(any(BannerAdView.class));
  }

  @Test
  public void onBannerClicked() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);
    mSubject.setListener(mMockListener);
    mSubject.load(mTestAd.getAdUnitId(), mMockBannerAdView);

    mSubject.onBannerClicked(mMockBannerPresenter);

    verify(mMockListener).onBannerClicked(mMockBannerAdView);
  }

  @Test
  public void onBannerClicked_whenDestroyed() {
    mSubject.destroy();

    mSubject.onBannerClicked(mMockBannerPresenter);

    verify(mMockListener, never()).onBannerClicked(any(BannerAdView.class));
  }

  @Test
  public void onBannerError() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);
    when(mMockBannerPresenter.getAd()).thenReturn(mTestAd);
    mSubject.setListener(mMockListener);
    mSubject.load(mTestAd.getAdUnitId(), mMockBannerAdView);

    mSubject.onBannerError(mMockBannerPresenter);

    verify(mMockRequestManager).startRefreshTimer(mTestAd.getRefreshTimeSeconds());
    verify(mMockListener).onBannerError(mMockBannerAdView);
  }

  @Test
  public void onBannerError_whenDestroyed() {
    mSubject.destroy();

    mSubject.onBannerError(mMockBannerPresenter);

    verify(mMockRequestManager, never()).startRefreshTimer(anyLong());
    verify(mMockListener, never()).onBannerError(any(BannerAdView.class));
  }
}
