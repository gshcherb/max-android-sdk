package io.maxads.ads.base;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;

public class RefreshTimer {
  private boolean mIsStopped = true;

  public Observable<Long> start(long delaySeconds) {
    // Subscribes on Schedulers.computation() by default
    mIsStopped = false;
    return Observable.timer(delaySeconds, TimeUnit.SECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .takeWhile(new Predicate<Long>() {
        @Override
        public boolean test(Long aLong) throws Exception {
          return !mIsStopped;
        }
      });
  }

  public void stop() {
    mIsStopped = true;
  }
}
