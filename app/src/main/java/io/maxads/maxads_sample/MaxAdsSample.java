package io.maxads.maxads_sample;

import android.annotation.SuppressLint;
import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import io.maxads.ads.base.MaxAds;

public class MaxAdsSample extends Application {

  @SuppressLint("VisibleForTests")
  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    MaxAds.initialize(this, null, new StethoInterceptor());
  }
}
