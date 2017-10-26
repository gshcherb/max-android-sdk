package io.maxads.ads.base.util;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.atomic.AtomicInteger;

public class Views {
  @NonNull private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

  public static void removeFromParent(@Nullable View view) {
    if (view == null || view.getParent() == null) {
      return;
    }

    if (view.getParent() instanceof ViewGroup) {
      ((ViewGroup) view.getParent()).removeView(view);
    }
  }

  public static int generateViewId() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
      // Borrowed from Android's View.java implementation for API > 16
      for (;;) {
        final int result = sNextGeneratedId.get();
        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
        int newValue = result + 1;
        if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
        if (sNextGeneratedId.compareAndSet(result, newValue)) {
          return result;
        }
      }
    } else {
      return View.generateViewId();
    }
  }
}
