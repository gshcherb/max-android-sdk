package io.maxads.ads.banner.presenter;

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
public class BannerPresenterFactoryTest {
  @Mock private BannerPresenter.Listener mMockListener;
  private Ad mTestAd;

  @InjectMocks private BannerPresenterFactory mSubject;

  @Before
  public void setup() {
    initMocks(this);
  }

  @Test
  public void createBannerPresenter_withHTML() {
    mTestAd = TestUtil.createTestAd(Winner.CreativeType.HTML);

    assertThat(mSubject.createBannerPresenter(mTestAd, mMockListener)).isNotNull();
  }

  @Test
  public void createBannerPresenter_withVAST() {
    mTestAd = TestUtil.createTestAd(Winner.CreativeType.VAST3);

    assertThat(mSubject.createBannerPresenter(mTestAd, mMockListener)).isNull();
  }

  @Test
  public void createBannerPresenter_withEMPTY() {
    mTestAd = TestUtil.createTestAd(Winner.CreativeType.EMPTY);

    assertThat(mSubject.createBannerPresenter(mTestAd, mMockListener)).isNull();
  }

  @Test
  public void fromCreativeType_withHTML() {
    assertThat(mSubject.fromCreativeType(Winner.CreativeType.HTML, mTestAd))
      .isInstanceOf(MraidBannerPresenter.class);
  }

  @Test
  public void fromCreativeType_withVAST() {
    assertThat(mSubject.fromCreativeType(Winner.CreativeType.VAST3, mTestAd)).isNull();
  }

  @Test
  public void fromCreativeType_withEMPTY() {
    assertThat(mSubject.fromCreativeType(Winner.CreativeType.EMPTY, mTestAd)).isNull();
  }
}
