package io.maxads.ads.base;

import com.jenzz.appstate.AppState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.concurrent.TimeUnit;

import io.maxads.ads.base.util.SystemTimeHelper;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class SessionDepthManagerTest {
  @Mock private SystemTimeHelper mMockSystemTimeHelper;

  private SessionDepthManager mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mSubject = new SessionDepthManager(RuntimeEnvironment.application, mMockSystemTimeHelper);
  }

  @Test
  public void handleAppState_withTimeLT30s() {
    when(mMockSystemTimeHelper.currentTimeMillis()).thenReturn(0L).thenReturn(TimeUnit.SECONDS.toMillis(29));

    mSubject.incrementSessionDepth();
    mSubject.incrementSessionDepth();
    assertThat(mSubject.getSessionDepth()).isEqualTo(2);

    mSubject.handleAppState(AppState.BACKGROUND);
    mSubject.handleAppState(AppState.FOREGROUND);

    assertThat(mSubject.getSessionDepth()).isEqualTo(2);
  }

  @Test
  public void handleAppState_withTimeGT30s() {
    when(mMockSystemTimeHelper.currentTimeMillis()).thenReturn(0L).thenReturn(TimeUnit.SECONDS.toMillis(31));

    mSubject.incrementSessionDepth();
    mSubject.incrementSessionDepth();
    assertThat(mSubject.getSessionDepth()).isEqualTo(2);

    mSubject.handleAppState(AppState.BACKGROUND);
    mSubject.handleAppState(AppState.FOREGROUND);

    assertThat(mSubject.getSessionDepth()).isEqualTo(0);
  }
}
