package io.maxads.maxads_sample;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import io.maxads.ads.base.MaxAds;

public class MaxAdsSample extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    MaxAds.initialize(this);
    MaxAds.getApiManager().initializeApiService(new StethoInterceptor());
  }
}
