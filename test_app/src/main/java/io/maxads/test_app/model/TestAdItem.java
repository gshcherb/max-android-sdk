package io.maxads.test_app.model;

import android.support.annotation.NonNull;

public class TestAdItem {
  @NonNull private final String mAdName;
  @NonNull private final String mAdPayload;

  public TestAdItem(@NonNull String adName, @NonNull String adPayload) {
    mAdName = adName;
    mAdPayload = adPayload;
  }

  @NonNull
  public String getAdName() {
    return mAdName;
  }

  @NonNull
  public String getAdPayload() {
    return mAdPayload;
  }
}
