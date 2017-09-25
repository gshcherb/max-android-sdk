package io.maxads.ads.base;

import android.app.Application;
import android.support.annotation.NonNull;

import com.jenzz.appstate.AppState;
import com.jenzz.appstate.adapter.rxjava2.RxAppStateMonitor;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.functions.Consumer;

public class SessionDepthManager {
  @NonNull private final AtomicInteger mSessionDepth;

  public SessionDepthManager(@NonNull final Application application) {
    mSessionDepth = new AtomicInteger(0);
    RxAppStateMonitor.monitor(application)
    .subscribe(new Consumer<AppState>() {
      @Override
      public void accept(AppState appState) throws Exception {
        if (appState == AppState.BACKGROUND) {
          mSessionDepth.set(0);
        }
      }
    });
  }

  public int getSessionDepth() {
    return mSessionDepth.get();
  }

  public void incrementSessionDepth() {
    mSessionDepth.incrementAndGet();
  }
}
