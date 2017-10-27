package io.maxads.ads.base;

import android.app.Application;
import android.support.annotation.NonNull;

import com.jenzz.appstate.AppState;
import com.jenzz.appstate.adapter.rxjava2.RxAppStateMonitor;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.functions.Consumer;

/**
 * Session depth starts at 0 and is incremented for each ad request attempt regardless of whether a response is received
 * or not. A single session is defined as the app being in the foreground. When the app is backgrounded, the session
 * depth counter starts over again.
 */
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
