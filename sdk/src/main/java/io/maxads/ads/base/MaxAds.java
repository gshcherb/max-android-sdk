package io.maxads.ads.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import io.maxads.ads.base.api.ApiManager;

public class MaxAds {
  @NonNull public static final String API_VERSION = "1";
  @NonNull public static final String SDK_VERSION = "0.5.0";
  @NonNull public static final String HOST = "ads.maxads.io";

  @NonNull private static ApiManager sApiManager;
  @NonNull private static DeviceInfo sDeviceInfo;
  @NonNull private static SessionDepthManager sSessionDepthManager;

  public static void initialize(@NonNull Application application) {
    sApiManager = new ApiManager();
    sDeviceInfo = new DeviceInfo(application.getApplicationContext());
    sSessionDepthManager = new SessionDepthManager(application);
  }

  @NonNull
  public static ApiManager getApiManager() {
    return sApiManager;
  }

  @NonNull
  public static DeviceInfo getsDeviceInfo() {
    return sDeviceInfo;
  }

  @NonNull
  public static SessionDepthManager getsSessionDepthManager() {
    return sSessionDepthManager;
  }
}
