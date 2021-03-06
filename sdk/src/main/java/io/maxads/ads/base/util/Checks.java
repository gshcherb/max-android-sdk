package io.maxads.ads.base.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Checks {
  @NonNull private static final String TAG = Checks.class.getSimpleName();

  public static void checkArgument(boolean expression, @NonNull String errorMessage) {
    checkArgumentImpl(expression, true, errorMessage);
  }

  public static void checkNotNull(@Nullable Object reference, @NonNull String errorMessage) {
    checkNotNullImpl(reference, true, errorMessage);
  }

  public static class NoThrow {
    private static boolean sStrictMode = false;

    public static void setStrictMode(boolean strictMode) {
      sStrictMode = strictMode;
    }

    public static boolean checkArgument(boolean expression, @NonNull String errorMessage) {
      return checkArgumentImpl(expression, sStrictMode, errorMessage);
    }

    public static boolean checkNotNull(@Nullable Object reference, @NonNull String errorMessage) {
      return checkNotNullImpl(reference, sStrictMode, errorMessage);
    }
  }

  private static boolean checkArgumentImpl(boolean expression, boolean isThrowable, @NonNull String errorMessage) {
    if (expression) {
      return true;
    }

    if (isThrowable) {
      throw new IllegalArgumentException(errorMessage);
    }

    MaxAdsLog.e(TAG, errorMessage);
    return false;
  }

  private static boolean checkNotNullImpl(@Nullable Object reference, boolean isThrowable,
                                          @NonNull String errorMessage) {
    if (reference != null) {
      return true;
    }

    if (isThrowable) {
      throw new NullPointerException(errorMessage);
    }

    MaxAdsLog.e(TAG, errorMessage);
    return false;
  }
}
