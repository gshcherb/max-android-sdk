package io.maxads.ads.base;

import android.content.Context;
import android.support.annotation.NonNull;

import io.maxads.ads.base.api.ApiManager;

public class MaxAds {
  @NonNull private static ApiManager sApiManager;
  @NonNull private static DeviceInfo sDeviceInfo;
  @NonNull public static final String API_VERSION = "1";
  @NonNull public static final String SDK_VERSION = "0.5.0";
  @NonNull public static final String HOST = "ads.maxads.io";

  public static void initialize(@NonNull Context context) {
    sApiManager = new ApiManager();
    sDeviceInfo = new DeviceInfo(context);
  }

  @NonNull
  public static ApiManager getApiManager() {
    return sApiManager;
  }

  @NonNull
  public static DeviceInfo getsDeviceInfo() {
    return sDeviceInfo;
  }
}
