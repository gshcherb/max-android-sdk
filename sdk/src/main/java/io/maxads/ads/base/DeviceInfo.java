package io.maxads.ads.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeviceInfo {
  @NonNull private final Context mContext;

  public DeviceInfo(@NonNull Context context) {
    mContext = context;
  }

  /**
   * Attempt to use the play services advertising ID, but fall back on the old style Android ID.
   * https://developer.android.com/training/articles/user-data-ids.html
   * https://support.google.com/googleplay/android-developer/answer/6048248?hl=en
   * https://play.google.com/about/monetization-ads/ads/ad-id/
   * @return
   */
  @SuppressLint("HardwareIds")
  @NonNull
  public Observable<AdvertisingIdClient.Info> getAdvertisingInfo() {
    return Observable.defer(new Callable<ObservableSource<AdvertisingIdClient.Info>>() {
      @Override
      public ObservableSource<AdvertisingIdClient.Info> call() throws Exception {
        try {
          final AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
          return Observable.just(advertisingIdInfo);
        } catch (Exception ignored) {
        }
        return Observable.just(new AdvertisingIdClient.Info(
          Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID), false));
      }
    }).subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread());
  }
}
