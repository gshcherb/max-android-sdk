package io.maxads.ads.base.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.RefreshTimer;
import io.maxads.ads.base.model.Ad;
import io.reactivex.functions.Consumer;

public class RequestManager {
  public interface RequestListener {
    void onRequestSuccess(@NonNull Ad ad);
    void onRequestFail(@NonNull Throwable throwable);
  }

  public interface TimerListener {
    void onTimerComplete();
  }

  @NonNull private final ApiClient mApiClient;
  @NonNull private final AdRequestFactory mAdRequestFactory;
  @NonNull private final RefreshTimer mRefreshTimer;
  @Nullable private RequestListener mRequestListener;
  @Nullable private TimerListener mTimerListener;

  public RequestManager() {
    mApiClient = MaxAds.getApiManager();
    mAdRequestFactory = new AdRequestFactory();
    mRefreshTimer = new RefreshTimer();
  }

  public void setRequestListener(@Nullable RequestListener requestListener) {
    mRequestListener = requestListener;
  }

  public void setTimerListener(@Nullable TimerListener timerListener) {
    mTimerListener = timerListener;
  }

  public void requestAd(@NonNull String adUnitId) {
    mAdRequestFactory.createAdRequest(adUnitId)
      .subscribe(new Consumer<AdRequest>() {
        @Override
        public void accept(AdRequest adRequest) throws Exception {
          requestAdFromApi(adRequest);
        }
      });
  }

  private void requestAdFromApi(@NonNull AdRequest adRequest) {
    mApiClient.getAd(adRequest)
      .subscribe(new Consumer<Ad>() {
        @Override
        public void accept(Ad ad) throws Exception {
          // TODO (steffan): cache ad here
          if (mRequestListener != null) {
            mRequestListener.onRequestSuccess(ad);
          }
        }
      }, new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
          if (mRequestListener != null) {
            mRequestListener.onRequestFail(throwable);
          }
        }
      });
  }

  public void startTimer(long delaySeconds) {
    mRefreshTimer.start(delaySeconds)
      .subscribe(new Consumer<Long>() {
        @Override
        public void accept(Long aLong) throws Exception {
          if (mTimerListener != null) {
            mTimerListener.onTimerComplete();
          }
        }
      });
  }

  public void destroy() {
    mRefreshTimer.stop();
  }
}
