package io.maxads.ads.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.MaxAdsLog;

public class AdCache {
  @NonNull private static final String TAG = AdCache.class.getSimpleName();
  @NonNull private final Map<String, Ad> mAdMap;

  public AdCache() {
    mAdMap = new HashMap<>();
  }

  @Nullable
  public Ad remove(@NonNull String adUnitKey) {
    return mAdMap.remove(adUnitKey);
  }

  public void put(@NonNull String adUnitKey, @NonNull Ad ad) {
    MaxAdsLog.d(TAG, "AdCache putting ad for adUnitKey: " + adUnitKey);
    mAdMap.put(adUnitKey, ad);
  }
}
