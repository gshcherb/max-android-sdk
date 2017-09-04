package io.maxads.ads.base.model;

import android.support.annotation.NonNull;

public class Winner {
  @NonNull private final String mCreativeType;

  public Winner(@NonNull String creativeType) {
    mCreativeType = creativeType;
  }

  @NonNull
  public String getCreativeType() {
    return mCreativeType;
  }
}
