package io.maxads.ads.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RefreshTimer {
  @Nullable private Disposable mDisposable;

  public void start(long delaySeconds, @NonNull Consumer<Long> consumer) {
    stop();
    // Subscribes on Schedulers.computation() by default
    mDisposable = Observable.timer(delaySeconds, TimeUnit.SECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(consumer);
  }

  public void stop() {
    if (mDisposable != null) {
      mDisposable.dispose();
    }
  }
}
