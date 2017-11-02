package io.maxads.ads.util;

import java.util.Collections;

import io.maxads.ads.base.api.AdRequest;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.model.Winner;

public class TestUtil {
  public static AdRequest createTestAdRequest() {
    return new AdRequest.Builder("adUnitId", "version", "sdkVersion", "appVersion", "ifa", true, "vendorId",
      "timeZone", "locale", "orientation", 320, 50, "browserAgent", "model", "connectivity", "carrier", 3)
      .withLatitude(123)
      .withLongitude(456)
      .build();
  }

  public static Ad createTestAd() {
    return new Ad("adUnitId", "creative", "prebidKeywords", 123, Collections.singletonList("impressionUrl"),
      Collections.singletonList("clickUrl"), Collections.singletonList("selectedUrl"),
      Collections.singletonList("errorUrl"), new Winner(Winner.CreativeType.HTML));
  }
}
