package io.maxads.test_app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.util.TestAdInterceptor;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

public class TestApp extends Application {
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
  }

  @NonNull
  public static TestAdInterceptor getTestAdInterceptor() {
    return sTestAdInterceptor;
  }
}
