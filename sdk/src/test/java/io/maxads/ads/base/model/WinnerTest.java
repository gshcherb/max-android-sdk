package io.maxads.ads.base.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import io.maxads.ads.base.api.WinnerResponse;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class WinnerTest {
  private WinnerResponse mWinnerResponse;

  private Winner mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mWinnerResponse = new WinnerResponse();
  }

  @Test
  public void fromWinnerResponse_withHTML() {
    mWinnerResponse.creativeType = "html";

    mSubject = Winner.from(mWinnerResponse);

    assertThat(mSubject.getCreativeType()).isEqualTo(Winner.CreativeType.HTML);
  }

  @Test
  public void fromWinnerResponse_withVAST() {
    mWinnerResponse.creativeType = "vast3";

    mSubject = Winner.from(mWinnerResponse);

    assertThat(mSubject.getCreativeType()).isEqualTo(Winner.CreativeType.VAST3);
  }

  @Test
  public void fromWinnerResponse_withEMPTY() {
    mWinnerResponse.creativeType = "";

    mSubject = Winner.from(mWinnerResponse);

    assertThat(mSubject.getCreativeType()).isEqualTo(Winner.CreativeType.EMPTY);
  }

  @Test
  public void fromWinnerResponse_withNULL() {
    mSubject = Winner.from(null);

    assertThat(mSubject.getCreativeType()).isEqualTo(Winner.CreativeType.EMPTY);
  }
}
