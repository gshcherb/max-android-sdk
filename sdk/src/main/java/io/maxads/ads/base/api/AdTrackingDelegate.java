package io.maxads.ads.base.api;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.functions.Consumer;

public class AdTrackingDelegate {
  @NonNull private final ApiManager mApiManager;
  @NonNull private final List<String> mImpressionUrls;
  @NonNull private final List<String> mClickUrls;
  private boolean mImpressionTracked;
  private boolean mClickTracked;

  public AdTrackingDelegate(@NonNull ApiManager apiManager,
                            @NonNull List<String> impressionUrls,
                            @NonNull List<String> clickUrls) {
    mApiManager = apiManager;
    mImpressionUrls = impressionUrls;
    mClickUrls = clickUrls;
  }

  public void trackImpression() {
    if (mImpressionTracked) {
      return;
    }

    trackUrls(mImpressionUrls);
    mImpressionTracked = true;
  }

  public void trackClick() {
    if (mClickTracked) {
      return;
    }

    trackUrls(mClickUrls);
    mClickTracked = true;
  }

  private void trackUrls(@NonNull List<String> urls) {
    for (String url : urls) {
      mApiManager.trackUrl(url)
        .subscribe(
          new Consumer<Void>() {
            @Override
            public void accept(Void aVoid) throws Exception {
              // Log success
            }
          },
          new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
              // Log fail
            }
          }
        );
    }
  }
}
