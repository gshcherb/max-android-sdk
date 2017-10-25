package io.maxads.ads.base.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Winner {
  public enum CreativeType {
    HTML,
    VAST3,
    EMPTY;

    public static CreativeType from(@Nullable String creativeType) {
      if ("html".equalsIgnoreCase(creativeType)) {
        return HTML;
      } else if ("vast3".equalsIgnoreCase(creativeType)) {
        return VAST3;
      } else {
        return EMPTY;
      }
    }
  }

  @NonNull private final CreativeType mCreativeType;

  public Winner(@NonNull CreativeType creativeType) {
    mCreativeType = creativeType;
  }

  @NonNull
  public CreativeType getCreativeType() {
    return mCreativeType;
  }
}
