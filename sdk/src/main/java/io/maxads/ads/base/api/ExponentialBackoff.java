package io.maxads.ads.base.api;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class ExponentialBackoff implements Function<Observable<? extends Throwable>, Observable<Long>> {
  @NonNull private final Jitter mJitter;
  private final long mDelay;
  @NonNull private final TimeUnit mTimeUnit;
  private final int mRetries;

  /**
   * Exponential backoff that respects the equation: mDelay * mRetries ^ 2 * mJitter
   *
   * @param retries The max number of mRetries or -1 to for MAX_INT times.
   */
  public ExponentialBackoff(@NonNull Jitter jitter, long delay, @NonNull TimeUnit timeUnit, int retries) {
    mJitter = jitter;
    mDelay = delay;
    mTimeUnit = timeUnit;
    mRetries = retries > 0 ? retries : Integer.MAX_VALUE;
  }

  @Override public Observable<Long> apply(
    @NonNull Observable<? extends Throwable> observable) throws Exception {
    return observable
      .zipWith(Observable.range(1, mRetries), new BiFunction<Throwable, Integer, Integer>() {
          @Override
          public Integer apply(Throwable throwable, @NonNull Integer retryCount) throws Exception {
            return retryCount;
          }
        }).flatMap(new Function<Integer, ObservableSource<Long>>() {
        @Override
        public ObservableSource<Long> apply(Integer attemptNumber) throws Exception {
          return Observable.timer(getNewInterval(attemptNumber), mTimeUnit);
        }
      });
  }

  private long getNewInterval(int retryCount) {
    long newInterval = (long) (mDelay * Math.pow(retryCount, 2) * mJitter.get());
    if (newInterval < 0) {
      newInterval = Long.MAX_VALUE;
    }
    return newInterval;
  }
}
