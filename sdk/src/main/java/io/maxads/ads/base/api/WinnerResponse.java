package io.maxads.ads.base.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WinnerResponse {
  @SerializedName("creative_type")
  @Expose
  public String creativeType;
}
