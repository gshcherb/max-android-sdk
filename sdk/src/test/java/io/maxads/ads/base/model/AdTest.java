package io.maxads.ads.base.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;

import io.maxads.ads.util.TestUtil;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AdTest {
  private Ad mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mSubject = Ad.from("adUnitId", TestUtil.createTestAdResponse());
  }

  @Test
  public void validateAd() {
    assertThat(mSubject.getAdUnitId()).isEqualTo("adUnitId");
    assertThat(mSubject.getCreative()).isEqualTo("creative");
    assertThat(mSubject.getPrebidKeywords()).isEqualTo("prebidKeywords");
    assertThat(mSubject.getRefreshTimeSeconds()).isEqualTo(123);
    assertThat(mSubject.getImpressionUrls()).isEqualTo(Collections.singletonList("impressionUrl"));
    assertThat(mSubject.getClickUrls()).isEqualTo(Collections.singletonList("clickUrl"));
    assertThat(mSubject.getSelectedUrls()).isEqualTo(Collections.singletonList("selectedUrl"));
    assertThat(mSubject.getErrorUrls()).isEqualTo(Collections.singletonList("errorUrl"));
    assertThat(mSubject.getWinner().getCreativeType()).isEqualTo(Winner.CreativeType.HTML);
  }
}
