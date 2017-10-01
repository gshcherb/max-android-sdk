package io.maxads.ads.base.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MaxAdsLog {
  @NonNull private static final String TAG = "MAXAds";

  public static void d(@Nullable String msg) {
    d(msg, null);
  }

  public static void w(@Nullable String msg) {
    w(msg, null);
  }

  public static void e(@Nullable String msg) {
    e(msg, null);
  }

  public static void d(@Nullable String msg, @Nullable Throwable throwable) {
    Log.d(TAG, msg, throwable);
  }

  public static void w(@Nullable String msg, @Nullable Throwable throwable) {
    Log.w(TAG, msg, throwable);
  }

  public static void e(@Nullable String msg, @Nullable Throwable throwable) {
    Log.e(TAG, msg, throwable);
  }
}
