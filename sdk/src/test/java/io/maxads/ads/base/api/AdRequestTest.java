package io.maxads.ads.base.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Type;
import java.util.Map;

import io.maxads.ads.util.TestUtil;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AdRequestTest {
  private Gson mGson;

  private AdRequest mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mSubject = TestUtil.createTestAdRequest();
    mGson = new Gson();
  }

  @Test
  public void validateAdRequestJSON() {
    final String json = mGson.toJson(mSubject);

    final Type type = new TypeToken<Map<String, Object>>(){}.getType();
    final Map<String, Object> map = mGson.fromJson(json, type);

    assertThat(mSubject.getAdUnitId()).isEqualTo("adUnitId");
    assertThat(map.get("v")).isEqualTo("version");
    assertThat(map.get("sdk_v")).isEqualTo("sdkVersion");
    assertThat(map.get("app_v")).isEqualTo("appVersion");
    assertThat(map.get("ifa")).isEqualTo("ifa");
    assertThat(map.get("lmt")).isEqualTo(true);
    assertThat(map.get("vendor_id")).isEqualTo("vendorId");
    assertThat(map.get("tz")).isEqualTo("timeZone");
    assertThat(map.get("locale")).isEqualTo("locale");
    assertThat(map.get("orientation")).isEqualTo("orientation");
    assertThat(map.get("w")).isEqualTo(320.0);
    assertThat(map.get("h")).isEqualTo(50.0);
    assertThat(map.get("browser_agent")).isEqualTo("browserAgent");
    assertThat(map.get("connectivity")).isEqualTo("connectivity");
    assertThat(map.get("carrier")).isEqualTo("carrier");
    assertThat(map.get("session_depth")).isEqualTo(3.0);
    assertThat(map.get("latitude")).isEqualTo(123.0);
    assertThat(map.get("longitude")).isEqualTo(456.0);
    assertThat(map.get("test")).isEqualTo(true);
  }
}
