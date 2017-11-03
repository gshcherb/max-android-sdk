package io.maxads.ads.interstitial.presenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.model.Winner;
import io.maxads.ads.util.TestUtil;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class InterstitialPresenterFactoryTest {
  @Mock private InterstitialPresenter.Listener mMockListener;
  private Ad mTestAd;

  @InjectMocks private InterstitialPresenterFactory mSubject;

  @Before
  public void setup() {
    initMocks(this);
  }

  @Test
  public void createInterstitialPresenter_withHTML() {
    mTestAd = TestUtil.createTestAd(Winner.CreativeType.HTML);

    assertThat(mSubject.createInterstitialPresenter(mTestAd, mMockListener)).isNotNull();
  }

  @Test
  public void createInterstitialPresenter_withVAST() {
    mTestAd = TestUtil.createTestAd(Winner.CreativeType.VAST3);

    assertThat(mSubject.createInterstitialPresenter(mTestAd, mMockListener)).isNotNull();
  }

  @Test
  public void createInterstitialPresenter_withEMPTY() {
    mTestAd = TestUtil.createTestAd(Winner.CreativeType.EMPTY);

    assertThat(mSubject.createInterstitialPresenter(mTestAd, mMockListener)).isNull();
  }

  @Test
  public void fromCreativeType_withHTML() {
    assertThat(mSubject.fromCreativeType(Winner.CreativeType.HTML, mTestAd))
      .isInstanceOf(MraidInterstitialPresenter.class);
  }

  @Test
  public void fromCreativeType_withVAST() {
    assertThat(mSubject.fromCreativeType(Winner.CreativeType.VAST3, mTestAd))
      .isInstanceOf(VastInterstitialPresenter.class);
  }

  @Test
  public void fromCreativeType_withEMPTY() {
    assertThat(mSubject.fromCreativeType(Winner.CreativeType.EMPTY, mTestAd)).isNull();
  }

}
