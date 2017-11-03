package io.maxads.ads.interstitial.presenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import io.maxads.ads.base.api.AdTrackingDelegate;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.util.TestUtil;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class InterstitialPresenterDecoratorTest {
  @Mock private InterstitialPresenter mMockInterstitialPresenter;
  @Mock private AdTrackingDelegate mMockAdTrackingDelegate;
  @Mock private InterstitialPresenter.Listener mMockListener;
  private Ad mTestAd;

  @InjectMocks private InterstitialPresenterDecorator mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mTestAd = TestUtil.createTestAd();
    when(mMockInterstitialPresenter.getAd()).thenReturn(mTestAd);
  }

  @Test
  public void load() {
    mSubject.load();

    verify(mMockAdTrackingDelegate).trackSelected();
    verify(mMockInterstitialPresenter).load();
  }

  @Test
  public void load_whenDestroyed() {
    mSubject.destroy();

    mSubject.load();

    verify(mMockAdTrackingDelegate, never()).trackSelected();
    verify(mMockInterstitialPresenter, never()).load();
  }

  @Test
  public void show() {
    mSubject.show();

    verify(mMockInterstitialPresenter).show();
  }

  @Test
  public void show_whenDestroyed() {
    mSubject.destroy();

    mSubject.show();

    verify(mMockInterstitialPresenter, never()).show();
  }

  @Test
  public void destroy() {
    mSubject.destroy();

    verify(mMockInterstitialPresenter).destroy();
  }

  @Test
  public void onInterstitialLoaded() {
    mSubject.onInterstitialLoaded(mMockInterstitialPresenter);

    verify(mMockListener).onInterstitialLoaded(mMockInterstitialPresenter);
  }

  @Test
  public void onInterstitialLoaded_whenDestroyed() {
    mSubject.destroy();

    mSubject.onInterstitialLoaded(mMockInterstitialPresenter);

    verify(mMockListener, never()).onInterstitialLoaded(any(InterstitialPresenter.class));
  }

  @Test
  public void onInterstitialShown() {
    mSubject.onInterstitialShown(mMockInterstitialPresenter);

    verify(mMockAdTrackingDelegate).trackImpression();
    verify(mMockListener).onInterstitialShown(mMockInterstitialPresenter);
  }

  @Test
  public void onInterstitialShown_whenDestroyed() {
    mSubject.destroy();

    mSubject.onInterstitialShown(mMockInterstitialPresenter);

    verify(mMockAdTrackingDelegate, never()).trackImpression();
    verify(mMockListener, never()).onInterstitialShown(any(InterstitialPresenter.class));
  }

  @Test
  public void onInterstitialClicked() {
    mSubject.onInterstitialClicked(mMockInterstitialPresenter);

    verify(mMockAdTrackingDelegate).trackClick();
    verify(mMockListener).onInterstitialClicked(mMockInterstitialPresenter);
  }

  @Test
  public void onInterstitialClicked_whenDestroyed() {
    mSubject.destroy();

    mSubject.onInterstitialClicked(mMockInterstitialPresenter);

    verify(mMockAdTrackingDelegate, never()).trackClick();
    verify(mMockListener, never()).onInterstitialClicked(any(InterstitialPresenter.class));
  }

  @Test
  public void onInterstitialDismissed() {
    mSubject.onInterstitialDismissed(mMockInterstitialPresenter);

    verify(mMockListener).onInterstitialDismissed(mMockInterstitialPresenter);
  }

  @Test
  public void onInterstitialDismissed_whenDestroyed() {
    mSubject.destroy();

    mSubject.onInterstitialDismissed(mMockInterstitialPresenter);

    verify(mMockListener, never()).onInterstitialDismissed(any(InterstitialPresenter.class));
  }

  @Test
  public void onInterstitialError() {
    mSubject.onInterstitialError(mMockInterstitialPresenter);

    verify(mMockAdTrackingDelegate).trackError(anyString());
    verify(mMockListener).onInterstitialError(mMockInterstitialPresenter);
  }

  @Test
  public void onInterstitialError_whenDestroyed() {
    mSubject.destroy();

    mSubject.onInterstitialError(mMockInterstitialPresenter);

    verify(mMockAdTrackingDelegate, never()).trackError(anyString());
    verify(mMockListener, never()).onInterstitialError(any(InterstitialPresenter.class));
  }
}
