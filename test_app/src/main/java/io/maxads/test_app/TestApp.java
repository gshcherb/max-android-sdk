package io.maxads.test_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.mraid.internal.MRAIDLog;
import io.maxads.ads.base.util.Checks;
import io.maxads.ads.base.util.TestAdInterceptor;
import io.maxads.ads.interstitial.vast.util.VASTLog;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

public class TestApp extends MultiDexApplication {
  @NonNull private static TestAdInterceptor sTestAdInterceptor;

  @SuppressLint("VisibleForTests")
  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    final List<Interceptor> applicationInterceptors = new ArrayList<>(2);
    sTestAdInterceptor = new TestAdInterceptor();
    applicationInterceptors.add(sTestAdInterceptor);
    applicationInterceptors.add(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
    MaxAds.initialize(this, applicationInterceptors, Collections.<Interceptor>singletonList(new StethoInterceptor()));
    Checks.NoThrow.setStrictMode(BuildConfig.DEBUG);
    MRAIDLog.setLoggingLevel(MRAIDLog.LOG_LEVEL.verbose);
    VASTLog.setLoggingLevel(VASTLog.LOG_LEVEL.verbose);
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @NonNull
  public static TestAdInterceptor getTestAdInterceptor() {
    return sTestAdInterceptor;
  }
}
