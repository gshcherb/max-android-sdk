package io.maxads.ads.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

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

  public static void initialize(@NonNull Application application) {
    initialize(application, null, null);
  }

  @VisibleForTesting
  public static void initialize(@NonNull Application application, @Nullable Interceptor applicationInterceptor,
                                @Nullable Interceptor networkInterceptor) {
    sApiClient = new ApiClient(applicationInterceptor, networkInterceptor);
    sDeviceInfo = new DeviceInfo(application.getApplicationContext());
    sSessionDepthManager = new SessionDepthManager(application);
    Checks.NoThrow.setStrictMode(BuildConfig.DEBUG);
    sAdCache = new AdCache();
    sInitialized = true;
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
  
  public static boolean isInitialized() {
    return sInitialized;
  }
}
