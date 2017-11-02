package io.maxads.ads.base.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.Callable;

import io.maxads.ads.base.AdCache;
import io.maxads.ads.base.RefreshTimer;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.InitializationHelper;
import io.maxads.ads.util.TestUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static io.reactivex.Observable.just;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class RequestManagerTest {
  @Mock private ApiClient mMockApiClient;
  @Mock private AdCache mMockAdCache;
  @Mock private AdRequestFactory mMockAdRequestFactory;
  @Mock private RefreshTimer mMockRefreshTimer;
  @Mock private InitializationHelper mMockInitializationHelper;
  @Mock private RequestManager.RequestListener mMockRequestListener;
  private AdRequest mTestAdRequest;
  private Ad mTestAd;

  @InjectMocks private RequestManager mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mTestAdRequest = TestUtil.createTestAdRequest();
    mTestAd = TestUtil.createTestAd();
    when(mMockAdRequestFactory.createAdRequest(mTestAd.getAdUnitId())).thenReturn(Observable.<AdRequest>empty());
    when(mMockApiClient.getAd(mTestAdRequest)).thenReturn(Observable.<Ad>empty());
  }

  @Test
  public void requestAd_createsAdRequest() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);
    mSubject.setAdUnitId(mTestAd.getAdUnitId());

    mSubject.requestAd();

    verify(mMockAdRequestFactory).createAdRequest(mTestAd.getAdUnitId());
  }

  @Test
  public void requestAd_withSDKUninitialized() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(false);

    mSubject.requestAd();

    verify(mMockAdRequestFactory, never()).createAdRequest(anyString());
  }

  @Test
  public void requestAd_withNullAdUnitId() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);

    mSubject.requestAd();

    verify(mMockAdRequestFactory, never()).createAdRequest(anyString());
  }

  @Test
  public void requestAd_whenDestroyed() {
    when(mMockInitializationHelper.isInitialized()).thenReturn(true);
    mSubject.setAdUnitId(mTestAd.getAdUnitId());
    mSubject.destroy();

    mSubject.requestAd();

    verify(mMockAdRequestFactory, never()).createAdRequest(anyString());
  }

  @Test
  public void requestAdFromApi() {
    when(mMockApiClient.getAd(mTestAdRequest)).thenReturn(just(mTestAd));
    mSubject.setRequestListener(mMockRequestListener);

    mSubject.requestAdFromApi(mTestAdRequest);

    verify(mMockApiClient).getAd(mTestAdRequest);
    verify(mMockAdCache).put(mTestAd.getAdUnitId(), mTestAd);
    verify(mMockRequestListener).onRequestSuccess(mTestAd);
  }

  @Test
  public void requestAdFromApi_withNullListener() {
    when(mMockApiClient.getAd(mTestAdRequest)).thenReturn(just(mTestAd));

    mSubject.requestAdFromApi(mTestAdRequest);

    verify(mMockApiClient).getAd(mTestAdRequest);
    verify(mMockAdCache).put(anyString(), any(Ad.class));
    verify(mMockRequestListener, never()).onRequestSuccess(any(Ad.class));
  }

  @Test
  public void requestAdFromApi_whenDestroyed() {
    when(mMockApiClient.getAd(mTestAdRequest)).thenReturn(just(mTestAd));
    mSubject.setRequestListener(mMockRequestListener);
    mSubject.destroy();

    mSubject.requestAdFromApi(mTestAdRequest);

    verify(mMockApiClient).getAd(mTestAdRequest);
    verify(mMockAdCache, never()).put(anyString(), any(Ad.class));
    verify(mMockRequestListener, never()).onRequestSuccess(any(Ad.class));
  }

  @Test
  public void requestAdFromApi_withFailedRequest() {
    final Exception exception = new RuntimeException();
    when(mMockApiClient.getAd(mTestAdRequest)).thenReturn(Observable.fromCallable(new Callable<Ad>() {
      @Override
      public Ad call() throws Exception {
        throw exception;
      }
    }));
    mSubject.setRequestListener(mMockRequestListener);

    mSubject.requestAdFromApi(mTestAdRequest);

    verify(mMockApiClient).getAd(mTestAdRequest);
    verify(mMockAdCache, never()).put(anyString(), any(Ad.class));
    verify(mMockRequestListener).onRequestFail(exception);
  }

  @Test
  public void requestAdFromApi_withFailedRequest_withNullListener() {
    final Exception exception = new RuntimeException();
    when(mMockApiClient.getAd(mTestAdRequest)).thenReturn(Observable.fromCallable(new Callable<Ad>() {
      @Override
      public Ad call() throws Exception {
        throw exception;
      }
    }));

    mSubject.requestAdFromApi(mTestAdRequest);

    verify(mMockApiClient).getAd(mTestAdRequest);
    verify(mMockAdCache, never()).put(anyString(), any(Ad.class));
    verify(mMockRequestListener, never()).onRequestFail(exception);
  }

  @Test
  public void requestAdFromApi_withFailedRequest_whenDestroyed() {
    final Exception exception = new RuntimeException();
    when(mMockApiClient.getAd(mTestAdRequest)).thenReturn(Observable.fromCallable(new Callable<Ad>() {
      @Override
      public Ad call() throws Exception {
        throw exception;
      }
    }));
    mSubject.setRequestListener(mMockRequestListener);
    mSubject.destroy();

    mSubject.requestAdFromApi(mTestAdRequest);

    verify(mMockApiClient).getAd(mTestAdRequest);
    verify(mMockAdCache, never()).put(anyString(), any(Ad.class));
    verify(mMockRequestListener, never()).onRequestFail(any(Throwable.class));
  }

  @Test
  public void startRefreshTimer_withDelayGT0() {
    mSubject.startRefreshTimer(1);

    verify(mMockRefreshTimer).start(eq(1L), any(Consumer.class));
  }

  @Test
  public void startRefreshTimer_withDelayLT0() {
    mSubject.startRefreshTimer(-1);

    verify(mMockRefreshTimer).start(eq((long) RequestManager.DEFAULT_REFRESH_TIME_SECONDS), any(Consumer.class));
  }

  @Test
  public void stopTimer() {
    mSubject.stopRefreshTimer();

    verify(mMockRefreshTimer).stop();
  }

  @Test
  public void destroy() {
    mSubject.destroy();

    verify(mMockRefreshTimer).stop();
  }
}
