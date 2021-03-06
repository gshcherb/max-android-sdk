package io.maxads.ads.base.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import io.maxads.ads.base.api.WinnerResponse;

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

  public static Winner from(@Nullable WinnerResponse winnerResponse) {
    return new Winner(CreativeType.from(winnerResponse == null ? null : winnerResponse.creativeType));
  }

  @VisibleForTesting
  public Winner(@NonNull CreativeType creativeType) {
    mCreativeType = creativeType;
  }

  @NonNull
  public CreativeType getCreativeType() {
    return mCreativeType;
  }
}
