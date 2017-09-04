package io.maxads.ads.base;

import android.content.Context;
import android.support.annotation.NonNull;

import io.maxads.ads.base.api.ApiManager;

public class MaxAds {
  @NonNull private static final ApiManager sApiManager = new ApiManager();
  @NonNull public static final String HOST = "ads.maxads.io";

  public static void initialize(@NonNull Context context) {
  }

  @NonNull
  public static ApiManager getApiManager() {
    return sApiManager;
  }
}
