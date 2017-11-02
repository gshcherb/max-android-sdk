package io.maxads.ads.banner.presenter;

import android.view.View;

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
public class BannerPresenterDecoratorTest {
  @Mock private BannerPresenter mMockBannerPresenter;
  @Mock private AdTrackingDelegate mMockAdTrackingDelegate;
  @Mock private View mMockView;
  @Mock private BannerPresenter.Listener mMockListener;
  private Ad mTestAd;

  @InjectMocks private BannerPresenterDecorator mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mTestAd = TestUtil.createTestAd();
    when(mMockBannerPresenter.getAd()).thenReturn(mTestAd);
  }

  @Test
  public void load() {
    mSubject.load();

    verify(mMockAdTrackingDelegate).trackSelected();
    verify(mMockBannerPresenter).load();
  }

  @Test
  public void load_whenDestroyed() {
    mSubject.destroy();

    mSubject.load();

    verify(mMockAdTrackingDelegate, never()).trackSelected();
    verify(mMockBannerPresenter, never()).load();
  }

  @Test
  public void destroy() {
    mSubject.destroy();

    verify(mMockBannerPresenter).destroy();
  }

  @Test
  public void onBannerLoaded() {
    mSubject.onBannerLoaded(mMockBannerPresenter, mMockView);

    verify(mMockAdTrackingDelegate).trackImpression();
    verify(mMockListener).onBannerLoaded(mMockBannerPresenter, mMockView);
  }

  @Test
  public void onBannerLoaded_whenDestroyed() {
    mSubject.destroy();

    mSubject.onBannerLoaded(mMockBannerPresenter, mMockView);

    verify(mMockAdTrackingDelegate, never()).trackImpression();
    verify(mMockListener, never()).onBannerLoaded(any(BannerPresenter.class), any(View.class));
  }

  @Test
  public void onBannerClicked() {
    mSubject.onBannerClicked(mMockBannerPresenter);

    verify(mMockAdTrackingDelegate).trackClick();
    verify(mMockListener).onBannerClicked(mMockBannerPresenter);
  }

  @Test
  public void onBannerClicked_whenDestroyed() {
    mSubject.destroy();

    mSubject.onBannerClicked(mMockBannerPresenter);

    verify(mMockAdTrackingDelegate, never()).trackClick();
    verify(mMockListener, never()).onBannerClicked(any(BannerPresenter.class));
  }

  @Test
  public void onBannerError() {
    mSubject.onBannerError(mMockBannerPresenter);

    verify(mMockAdTrackingDelegate).trackError(anyString());
    verify(mMockListener).onBannerError(mMockBannerPresenter);
  }

  @Test
  public void onBannerError_whenDestroyed() {
    mSubject.destroy();

    mSubject.onBannerError(mMockBannerPresenter);

    verify(mMockAdTrackingDelegate, never()).trackError(anyString());
    verify(mMockListener, never()).onBannerError(any(BannerPresenter.class));
  }
}
