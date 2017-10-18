package io.maxads.test_app;

import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.util.TestAdInterceptor;

public class TestApp extends Application {
  @NonNull private static TestAdInterceptor sTestAdInterceptor;

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    MaxAds.initialize(this);
    sTestAdInterceptor = new TestAdInterceptor();
    MaxAds.getApiManager().initializeApiService(sTestAdInterceptor, new StethoInterceptor());
  }

  @NonNull
  public static TestAdInterceptor getTestAdInterceptor() {
    return sTestAdInterceptor;
  }
}
