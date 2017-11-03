package io.maxads.ads.base.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AdTrackingDelegateTest {
  @Mock private ApiClient mMockApiClient;

  private AdTrackingDelegate mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mSubject = new AdTrackingDelegate(mMockApiClient, Collections.singletonList("selectedUrl"),
      Collections.singletonList("impressionUrl"), Collections.singletonList("clickUrl"));
  }

  @Test
  public void trackSelected() {
    mSubject.trackSelected();
    mSubject.trackSelected();

    verify(mMockApiClient, times(1)).trackUrl("selectedUrl");
  }

  @Test
  public void trackImpression() {
    mSubject.trackImpression();
    mSubject.trackImpression();

    verify(mMockApiClient, times(1)).trackUrl("impressionUrl");
  }

  @Test
  public void trackClick() {
    mSubject.trackClick();
    mSubject.trackClick();

    verify(mMockApiClient, times(1)).trackUrl("clickUrl");
  }

  @Test
  public void trackError() {
    mSubject.trackError("1");
    mSubject.trackError("2");

    verify(mMockApiClient).trackError("1");
    verify(mMockApiClient).trackError("2");
  }
}
