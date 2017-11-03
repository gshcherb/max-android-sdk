package io.maxads.ads.base.api;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.util.MaxAdsLog;

public class AdTrackingDelegate {
  private enum Type {
    SELECTED("selected"),
    IMPRESSION("impression"),
    CLICK("click");

    @NonNull private final String mType;

    Type(@NonNull String type) {
      mType = type;
    }


    @Override
    public String toString() {
      return mType;
    }
  }

  @NonNull private final ApiClient mApiClient;
  @NonNull private final List<String> mSelectedUrls;
  @NonNull private final List<String> mImpressionUrls;
  @NonNull private final List<String> mClickUrls;
  private boolean mSelectedTracked;
  private boolean mImpressionTracked;
  private boolean mClickTracked;

  public AdTrackingDelegate(@NonNull List<String> selectedUrls,
                            @NonNull List<String> impressionUrls,
                            @NonNull List<String> clickUrls) {
    this(MaxAds.getApiClient(), selectedUrls, impressionUrls, clickUrls);
  }

  @VisibleForTesting
  AdTrackingDelegate(@NonNull ApiClient apiClient,
                     @NonNull List<String> selectedUrls,
                     @NonNull List<String> impressionUrls,
                     @NonNull List<String> clickUrls) {
    mApiClient = apiClient;
    mSelectedUrls = selectedUrls;
    mImpressionUrls = impressionUrls;
    mClickUrls = clickUrls;
  }

  public void trackSelected() {
    if (mSelectedTracked) {
      return;
    }

    trackUrls(mSelectedUrls, Type.SELECTED);
    mSelectedTracked = true;
  }

  public void trackImpression() {
    if (mImpressionTracked) {
      return;
    }

    trackUrls(mImpressionUrls, Type.IMPRESSION);
    mImpressionTracked = true;
  }

  public void trackClick() {
    if (mClickTracked) {
      return;
    }

    trackUrls(mClickUrls, Type.CLICK);
    mClickTracked = true;
  }

  public void trackError(@NonNull String errorMessage) {
    mApiClient.trackError(errorMessage);
  }

  private void trackUrls(@NonNull List<String> urls, @NonNull final Type type) {
    for (final String url : urls) {
      MaxAdsLog.d("Tracking " + type.toString() + " url: " + url);
      mApiClient.trackUrl(url);
    }
  }
}
