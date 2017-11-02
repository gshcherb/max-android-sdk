package io.maxads.ads.util;

import java.util.Collections;

import io.maxads.ads.base.api.AdRequest;
import io.maxads.ads.base.api.AdResponse;
import io.maxads.ads.base.api.WinnerResponse;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.model.Winner;

public class TestUtil {
  public static AdRequest createTestAdRequest() {
    return new AdRequest.Builder("adUnitId", "version", "sdkVersion", "appVersion", "ifa", true, "vendorId",
      "timeZone", "locale", "orientation", 320, 50, "browserAgent", "model", "connectivity", "carrier", 3)
      .withLatitude(123)
      .withLongitude(456)
      .withTest(true)
      .build();
  }

  public static AdResponse createTestAdResponse() {
    final AdResponse adResponse = new AdResponse();
    adResponse.creative = "creative";
    adResponse.prebidKeywords = "prebidKeywords";
    adResponse.refresh = 123;
    adResponse.impressionUrls = Collections.singletonList("impressionUrl");
    adResponse.clickUrls = Collections.singletonList("clickUrl");
    adResponse.selectedUrls = Collections.singletonList("selectedUrl");
    adResponse.errorUrls = Collections.singletonList("errorUrl");
    final WinnerResponse winnerResponse = new WinnerResponse();
    winnerResponse.creativeType = "html";
    adResponse.winnerResponse = winnerResponse;

    return adResponse;
  }

  public static Ad createTestAd() {
    return createTestAd(Winner.CreativeType.HTML);
  }

  public static Ad createTestAd(Winner.CreativeType creativeType) {
    return new Ad("adUnitId", "creative", "prebidKeywords", 123, Collections.singletonList("impressionUrl"),
      Collections.singletonList("clickUrl"), Collections.singletonList("selectedUrl"),
      Collections.singletonList("errorUrl"), new Winner(creativeType));
  }
}
