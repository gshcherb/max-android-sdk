package io.maxads.ads.base.util;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

public class Views {
  public static void removeFromParent(@Nullable View view) {
    if (view == null || view.getParent() == null) {
      return;
    }

    if (view.getParent() instanceof ViewGroup) {
      ((ViewGroup) view.getParent()).removeView(view);
    }
  }
}
