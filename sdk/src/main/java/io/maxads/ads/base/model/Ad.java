package io.maxads.ads.base.model;

import android.support.annotation.NonNull;

import java.util.List;

public class Ad {
  @NonNull private final String mAdUnitId;
  @NonNull private final String mCreative;
  @NonNull private final String mPrebidKeywords;
  @NonNull private final Integer mRefreshTimeSeconds;
  @NonNull private final List<String> mImpressionUrls;
  @NonNull private final List<String> mClickUrls;
  @NonNull private final List<String> mSelectedUrls;
  @NonNull private final List<String> mErrorUrls;
  @NonNull private final Winner mWinner;

  public Ad(@NonNull String adUnitId, @NonNull String creative, @NonNull String prebidKeywords,
            @NonNull Integer refreshTimeSeconds, @NonNull List<String> impressionUrls, @NonNull List<String> clickUrls,
            @NonNull List<String> selectedUrls, @NonNull List<String> errorUrls, @NonNull Winner winner) {

    mAdUnitId = adUnitId;
    mCreative = creative;
    mPrebidKeywords = prebidKeywords;
    mRefreshTimeSeconds = refreshTimeSeconds;
    mImpressionUrls = impressionUrls;
    mClickUrls = clickUrls;
    mSelectedUrls = selectedUrls;
    mErrorUrls = errorUrls;
    mWinner = winner;
  }

  @NonNull
  public String getAdUnitId() {
    return mAdUnitId;
  }

  @NonNull
  public String getCreative() {
    return mCreative;
  }

  @NonNull
  public String getPrebidKeywords() {
    return mPrebidKeywords;
  }

  @NonNull
  public Integer getRefreshTimeSeconds() {
    return mRefreshTimeSeconds;
  }

  @NonNull
  public List<String> getImpressionUrls() {
    return mImpressionUrls;
  }

  @NonNull
  public List<String> getClickUrls() {
    return mClickUrls;
  }

  @NonNull
  public List<String> getSelectedUrls() {
    return mSelectedUrls;
  }

  @NonNull
  public Winner getWinner() {
    return mWinner;
  }
}
