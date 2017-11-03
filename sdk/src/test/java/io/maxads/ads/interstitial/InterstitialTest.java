package io.maxads.ads.interstitial;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import io.maxads.ads.base.api.RequestManager;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.InitializationHelper;
import io.maxads.ads.interstitial.presenter.InterstitialPresenter;
import io.maxads.ads.interstitial.presenter.InterstitialPresenterFactory;
import io.maxads.ads.util.TestUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class InterstitialTest {
  @Mock private InterstitialPresenterFactory mMockInterstitialPresenterFactory;
  @Mock private RequestManager mMockRequestManager;
  @Mock private InitializationHelper mMockInitializationHelper;
  @Mock private InterstitialPresenter mMockInterstitialPresenter;
  @Mock private Interstitial.Listener mMockListener;
  private Ad mTestAd;

  @InjectMocks private Interstitial mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mTestAd = TestUtil.createTestAd();
  }

  @Test
  public void load() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);

    mSubject.load(mTestAd.getAdUnitId());

    verify(mMockRequestManager).setAdUnitId(mTestAd.getAdUnitId());
    verify(mMockRequestManager).requestAd();
  }

  @Test
  public void load_withSDKUninitialized() {
    mSubject.load(mTestAd.getAdUnitId());

    verify(mMockRequestManager, never()).setAdUnitId(anyString());
    verify(mMockRequestManager, never()).requestAd();
  }

  @Test
  public void load_withNullAdUnitId() {
    mSubject.load(null);

    verify(mMockRequestManager, never()).setAdUnitId(anyString());
    verify(mMockRequestManager, never()).requestAd();
  }

  @Test
  public void load_whenDestroyed() {
    mSubject.destroy();

    mSubject.load(mTestAd.getAdUnitId());

    verify(mMockRequestManager, never()).setAdUnitId(anyString());
    verify(mMockRequestManager, never()).requestAd();
  }

  @Test
  public void loadInterstitial() {
    when(mMockInterstitialPresenterFactory.createInterstitialPresenter(mTestAd, mSubject))
      .thenReturn(mMockInterstitialPresenter);

    mSubject.loadInterstitial(mTestAd);

    verify(mMockInterstitialPresenter).load();
  }

  @Test
  public void loadInterstitial_withNullPresenter() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);
    when(mMockInterstitialPresenterFactory.createInterstitialPresenter(mTestAd, mSubject))
      .thenReturn(null);
    mSubject.setListener(mMockListener);
    mSubject.load(mTestAd.getAdUnitId());

    mSubject.loadInterstitial(mTestAd);

    verify(mMockListener).onInterstitialError(mSubject);
    verify(mMockInterstitialPresenter, never()).load();
  }

  @Test
  public void loadInterstitial_whenDestroyed() {
    mSubject.destroy();

    mSubject.loadInterstitial(mTestAd);

    verify(mMockInterstitialPresenterFactory, never())
      .createInterstitialPresenter(any(Ad.class), any(InterstitialPresenter.Listener.class));
    verify(mMockListener, never()).onInterstitialError(mSubject);
    verify(mMockInterstitialPresenter, never()).load();
  }

  @Test
  public void destroy() {
    mSubject.destroy();

    verify(mMockRequestManager).destroy();
  }

  @Test
  public void onRequestFail() {
    mSubject.setListener(mMockListener);

    mSubject.onRequestFail(new Exception());

    verify(mMockListener).onInterstitialError(mSubject);
  }

  @Test
  public void onRequestFail_whenDestroyed() {
    mSubject.destroy();

    mSubject.onRequestFail(new Exception());

    verify(mMockListener, never()).onInterstitialError(any(Interstitial.class));
  }

  @Test
  public void onInterstitialLoaded() {
    mSubject.setListener(mMockListener);

    mSubject.onInterstitialLoaded(mMockInterstitialPresenter);

    verify(mMockListener).onInterstitialLoaded(mSubject);
  }

  @Test
  public void onInterstitialLoaded_whenDestroyed() {
    mSubject.destroy();

    mSubject.onInterstitialLoaded(mMockInterstitialPresenter);

    verify(mMockListener, never()).onInterstitialLoaded(any(Interstitial.class));
  }

  @Test
  public void onInterstitialClicked() {
    mSubject.setListener(mMockListener);

    mSubject.onInterstitialClicked(mMockInterstitialPresenter);

    verify(mMockListener).onInterstitialClicked(mSubject);
  }

  @Test
  public void onInterstitialClicked_whenDestroyed() {
    mSubject.destroy();

    mSubject.onInterstitialClicked(mMockInterstitialPresenter);

    verify(mMockListener, never()).onInterstitialClicked(any(Interstitial.class));
  }

  @Test
  public void onInterstitialDismissed() {
    when(mMockInterstitialPresenterFactory.createInterstitialPresenter(mTestAd, mSubject))
      .thenReturn(mMockInterstitialPresenter);
    mSubject.setListener(mMockListener);
    mSubject.loadInterstitial(mTestAd);

    mSubject.onInterstitialDismissed(mMockInterstitialPresenter);

    verify(mMockInterstitialPresenter).destroy();
    verify(mMockListener).onInterstitialDismissed(mSubject);
  }

  @Test
  public void onInterstitialDismissed_whenDestroyed() {
    mSubject.destroy();

    mSubject.onInterstitialDismissed(mMockInterstitialPresenter);

    verify(mMockInterstitialPresenter, never()).destroy();
    verify(mMockListener, never()).onInterstitialDismissed(any(Interstitial.class));
  }

  @Test
  public void onInterstitialError() {
    when(mMockInterstitialPresenterFactory.createInterstitialPresenter(mTestAd, mSubject))
      .thenReturn(mMockInterstitialPresenter);
    mSubject.setListener(mMockListener);
    mSubject.loadInterstitial(mTestAd);

    mSubject.onInterstitialError(mMockInterstitialPresenter);

    verify(mMockInterstitialPresenter).destroy();
    verify(mMockListener).onInterstitialError(mSubject);
  }

  @Test
  public void onInterstitialError_whenDestroyed() {
    mSubject.destroy();

    mSubject.onInterstitialError(mMockInterstitialPresenter);

    verify(mMockInterstitialPresenter, never()).destroy();
    verify(mMockListener, never()).onInterstitialError(any(Interstitial.class));
  }
}
