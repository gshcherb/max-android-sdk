package io.maxads.ads.base.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import io.maxads.ads.base.AdCache;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.RefreshTimer;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.util.Checks;
import io.maxads.ads.base.util.InitializationHelper;
import io.maxads.ads.base.util.MaxAdsLog;
import io.reactivex.functions.Consumer;

public class RequestManager {
  public static final int DEFAULT_REFRESH_TIME_SECONDS = 60;

  public interface RequestListener {
    void onRequestSuccess(@NonNull Ad ad);
    void onRequestFail(@NonNull Throwable throwable);
  }

  @NonNull private final ApiClient mApiClient;
  @NonNull private final AdCache mAdCache;
  @NonNull private final AdRequestFactory mAdRequestFactory;
  @NonNull private final RefreshTimer mRefreshTimer;
  @NonNull private final InitializationHelper mInitializationHelper;
  @Nullable private String mAdUnitId;
  @Nullable private RequestListener mRequestListener;
  private boolean mIsDestroyed;

  public RequestManager() {
    this(MaxAds.getApiManager(), MaxAds.getAdCache(), new AdRequestFactory(), new RefreshTimer(), new InitializationHelper());
  }

  @VisibleForTesting
  RequestManager(@NonNull ApiClient apiClient,
                 @NonNull AdCache adCache,
                 @NonNull AdRequestFactory adRequestFactory,
                 @NonNull RefreshTimer refreshTimer,
                 @NonNull InitializationHelper initializationHelper) {
    mApiClient = apiClient;
    mAdCache = adCache;
    mAdRequestFactory = adRequestFactory;
    mRefreshTimer = refreshTimer;
    mInitializationHelper = initializationHelper;
  }

  public void setRequestListener(@Nullable RequestListener requestListener) {
    mRequestListener = requestListener;
  }

  public void setAdUnitId(@Nullable String adUnitId) {
    mAdUnitId = adUnitId;
  }

  public void requestAd() {
    if (!Checks.NoThrow.checkArgument(mInitializationHelper.isInitialized(), "MaxAds SDK has not been initialized. " +
      "Please call MaxAds#initialize in your application's onCreate method.")) {
      return;
    }

    if (!Checks.NoThrow.checkNotNull(mAdUnitId, "adUnitId cannot be null")) {
      return;
    }

    if (!Checks.NoThrow.checkArgument(!mIsDestroyed, "RequestManager has been destroyed")) {
      return;
    }

    mAdRequestFactory.createAdRequest(mAdUnitId)
      .subscribe(new Consumer<AdRequest>() {
        @Override
        public void accept(AdRequest adRequest) throws Exception {
          requestAdFromApi(adRequest);
        }
      });
  }

  @VisibleForTesting
  void requestAdFromApi(@NonNull final AdRequest adRequest) {
    MaxAdsLog.d("Requesting ad for ad unit id: " + adRequest.getAdUnitId());
    mApiClient.getAd(adRequest)
      .subscribe(new Consumer<Ad>() {
        @Override
        public void accept(@NonNull Ad ad) throws Exception {
          if (mIsDestroyed) {
            return;
          }

          MaxAdsLog.d("Received ad response for ad unit id: " + adRequest.getAdUnitId());
          mAdCache.put(adRequest.getAdUnitId(), ad);
          if (mRequestListener != null) {
            mRequestListener.onRequestSuccess(ad);
          }
        }
      }, new Consumer<Throwable>() {
        /**
         * Handles failed network requests and empty responses
         */
        @Override
        public void accept(Throwable throwable) throws Exception {
          if (mIsDestroyed) {
            return;
          }

          MaxAdsLog.w("Failed to receive ad response for ad unit id: " + adRequest.getAdUnitId(), throwable);
          if (mRequestListener != null) {
            mRequestListener.onRequestFail(throwable);
          }
        }
      });
  }

  public void startRefreshTimer(long delaySeconds) {
    if (!Checks.NoThrow.checkArgument(!mIsDestroyed, "RequestManager has been destroyed")) {
      return;
    }

    delaySeconds = delaySeconds > 0 ? delaySeconds : DEFAULT_REFRESH_TIME_SECONDS;
    mRefreshTimer.start(delaySeconds, new Consumer<Long>() {
      @Override
      public void accept(Long aLong) throws Exception {
        requestAd();
      }
    });
  }

  public void stopRefreshTimer() {
    mRefreshTimer.stop();
  }

  public void destroy() {
    mRefreshTimer.stop();
    mRequestListener = null;
    mIsDestroyed = true;
  }
}
