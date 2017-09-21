package io.maxads.ads.base.model;

import android.support.annotation.NonNull;

import java.util.List;

public class Ad {
  @NonNull private final String mCreative;
  @NonNull private final String mPrebidKeywords;
  @NonNull private final Integer mRefresh;
  @NonNull private final List<String> mImpressionUrls;
  @NonNull private final List<String> mClickUrls;
  @NonNull private final List<String> mSelectUrls;
  @NonNull private final List<String> mErrorUrls;
  @NonNull private final Winner mWinner;

  public Ad(@NonNull String creative, @NonNull String prebidKeywords, @NonNull Integer refresh,
            @NonNull List<String> impressionUrls, @NonNull List<String> clickUrls, @NonNull List<String> selectUrls,
            @NonNull List<String> errorUrls, @NonNull Winner winner) {
    mCreative = creative;
    mPrebidKeywords = prebidKeywords;
    mRefresh = refresh;
    mImpressionUrls = impressionUrls;
    mClickUrls = clickUrls;
    mSelectUrls = selectUrls;
    mErrorUrls = errorUrls;
    mWinner = winner;
  }

  @NonNull
  public String getCreative() {
    return mCreative;
  }
}
