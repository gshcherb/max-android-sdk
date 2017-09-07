package io.maxads.ads.base.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdResponse {
  @SerializedName("creative")
  @Expose
  public String creative;

  @SerializedName("prebid_keywords")
  @Expose
  public String prebidKeywords;

  @SerializedName("refresh")
  @Expose
  public Integer refresh;

  @SerializedName("impression_urls")
  @Expose
  public List<String> impressionUrls;

  @SerializedName("click_urls")
  @Expose
  public List<String> clickUrls;

  @SerializedName("select_urls")
  @Expose
  public List<String> selectUrls;

  @SerializedName("error_urls")
  @Expose
  public List<String> errorUrls;

  @SerializedName("winner")
  @Expose
  public WinnerResponse winner;
}