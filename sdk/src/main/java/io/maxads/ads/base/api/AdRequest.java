package io.maxads.ads.base.api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdRequest {
  @NonNull private transient final String mAdUnitId;

  @SerializedName("v")
  @Expose
  @NonNull private final String mVersion;

  private AdRequest(@NonNull String adUnitId, @NonNull String version) {
    mAdUnitId = adUnitId;
    mVersion = version;
  }

  @NonNull
  public String getAdUnitId() {
    return mAdUnitId;
  }

  public static class Builder {
    @NonNull private final String mAdUnitId;
    @NonNull private final String mVersion;

    public Builder(@NonNull String adUnitId, @NonNull String version) {
      mAdUnitId = adUnitId;
      mVersion = version;
    }

    public AdRequest build() {
      return new AdRequest(mAdUnitId, mVersion);
    }
  }
}
