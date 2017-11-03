package io.maxads.ads.base.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Type;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;
import io.maxads.ads.util.TestUtil;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AdResponseTest {
  private Gson mGson;

  private AdResponse mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mSubject = TestUtil.createTestAdResponse();
    mGson = new Gson();
  }

  @Test
  public void validateAdResponse() {
    final String json = mGson.toJson(mSubject);

    final Type type = new TypeToken<Map<String, Object>>(){}.getType();
    final Map<String, Object> map = mGson.fromJson(json, type);

    assertThat(map.get("creative")).isEqualTo("creative");
    assertThat(map.get("prebid_keywords")).isEqualTo("prebidKeywords");
    assertThat(map.get("refresh")).isEqualTo(123.0);
    assertThat(map.get("impression_urls")).isEqualTo(Collections.singletonList("impressionUrl"));
    assertThat(map.get("click_urls")).isEqualTo(Collections.singletonList("clickUrl"));
    assertThat(map.get("selected_urls")).isEqualTo(Collections.singletonList("selectedUrl"));
    assertThat(map.get("error_urls")).isEqualTo(Collections.singletonList("errorUrl"));
    assertThat(((Map) map.get("winner")).get("creative_type")).isEqualTo("html");
  }
}
