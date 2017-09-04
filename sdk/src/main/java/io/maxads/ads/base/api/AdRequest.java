package io.maxads.ads.base.api;

import android.support.annotation.NonNull;

public class AdRequest {
  @NonNull private transient final String adUnitId;
  @NonNull private final String v;

  private AdRequest(@NonNull String adUnitId, @NonNull String v) {
    this.adUnitId = adUnitId;
    this.v = v;
  }

  @NonNull
  public String getAdUnitId() {
    return adUnitId;
  }

  public static class Builder {
    @NonNull private final String adUnitId;
    @NonNull private final String v;

    public Builder(@NonNull String adUnitId, @NonNull String v) {
      this.adUnitId = adUnitId;
      this.v = v;
    }

    public AdRequest build() {
      return new AdRequest(adUnitId, v);
    }
  }
}
