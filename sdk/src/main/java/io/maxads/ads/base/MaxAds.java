package io.maxads.ads.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.Collections;
import java.util.List;

import io.maxads.ads.BuildConfig;
import io.maxads.ads.base.api.ApiClient;
import io.maxads.ads.base.util.Checks;
import okhttp3.Interceptor;

public class MaxAds {
  @NonNull public static final String API_VERSION = "1";
  @NonNull public static final String SDK_VERSION = "0.5.0";
  @NonNull public static final String HOST = "ads.maxads.io";

  @NonNull private static ApiClient sApiClient;
  @SuppressLint("StaticFieldLeak")
  @NonNull private static DeviceInfo sDeviceInfo;
  @NonNull private static SessionDepthManager sSessionDepthManager;
  @NonNull private static AdCache sAdCache;
  private static boolean sInitialized;

  /**
   * This method must be called to initialize the SDK before request ads.
   */
  public static void initialize(@NonNull Application application) {
    initialize(application, Collections.<Interceptor>emptyList(), Collections.<Interceptor>emptyList());
  }

  /**
   * For testing and debugging purposes only.
   */
  @VisibleForTesting
  public static void initialize(@NonNull Application application,
                                @NonNull List<Interceptor> applicationInterceptors,
                                @NonNull List<Interceptor> networkInterceptors) {
    sApiClient = new ApiClient(applicationInterceptors, networkInterceptors);
    sDeviceInfo = new DeviceInfo(application.getApplicationContext());
    sSessionDepthManager = new SessionDepthManager(application);
    sAdCache = new AdCache();
    sInitialized = true;
  }

  @NonNull
  public static ApiClient getApiClient() {
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
  
  public static boolean isInitialized() {
    return sInitialized;
  }
}
