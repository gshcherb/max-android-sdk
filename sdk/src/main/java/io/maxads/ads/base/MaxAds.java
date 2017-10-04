package io.maxads.ads.base;

import android.app.Application;
import android.support.annotation.NonNull;

import io.maxads.ads.base.api.ApiClient;

public class MaxAds {
  @NonNull public static final String API_VERSION = "1";
  @NonNull public static final String SDK_VERSION = "0.5.0";
  @NonNull public static final String HOST = "ads.maxads.io";

  @NonNull private static ApiClient sApiClient;
  @NonNull private static DeviceInfo sDeviceInfo;
  @NonNull private static SessionDepthManager sSessionDepthManager;
  @NonNull private static AdCache sAdCache;

  public static void initialize(@NonNull Application application) {
    sApiClient = new ApiClient();
    sDeviceInfo = new DeviceInfo(application.getApplicationContext());
    sSessionDepthManager = new SessionDepthManager(application);
    sAdCache = new AdCache();
  }

  @NonNull
  public static ApiClient getApiManager() {
    return sApiClient;
  }

  @NonNull
  public static DeviceInfo getDeviceInfo() {
    return sDeviceInfo;
  }

  @NonNull
  public static SessionDepthManager getSessionDepthManager() {
    return sSessionDepthManager;
  }

  @NonNull
  public static AdCache getAdCache() {
    return sAdCache;
  }
}
