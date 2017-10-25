package io.maxads.maxads_sample;

import android.annotation.SuppressLint;
import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.Collections;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.util.Checks;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

public class MaxAdsSample extends Application {

  @SuppressLint("VisibleForTests")
  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    MaxAds.initialize(this,
      Collections.<Interceptor>singletonList(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)),
      Collections.<Interceptor>singletonList(new StethoInterceptor()));
    Checks.NoThrow.setStrictMode(BuildConfig.DEBUG);
  }
}
