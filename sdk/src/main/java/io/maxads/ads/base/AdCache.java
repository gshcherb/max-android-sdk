package io.maxads.ads.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.MaxAdsLog;

public class AdCache {

  @NonNull private Map<String, Ad> mAdsMap;

  public AdCache() {
    mAdsMap = new HashMap<>();
  }

  @Nullable
  public Ad remove(@NonNull String adUnitKey) {
    return mAdsMap.remove(adUnitKey);
  }

  public void put(@NonNull String adUnitKey, @NonNull Ad response) {
    MaxAdsLog.d("AdCache putting response value for adUnitKey " + adUnitKey);
    mAdsMap.put(adUnitKey, response);
  }
}
