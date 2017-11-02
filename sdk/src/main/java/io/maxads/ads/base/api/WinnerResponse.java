package io.maxads.ads.base.api;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WinnerResponse {
  @SerializedName("creative_type")
  @Expose
  @NonNull public String creativeType;
}
