package io.maxads.ads.base.api;

import android.support.annotation.NonNull;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import io.maxads.ads.base.DeviceInfo;
import io.maxads.ads.base.MaxAds;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class AdRequestFactory {
  @NonNull private final DeviceInfo mDeviceInfo;

  public AdRequestFactory() {
    mDeviceInfo = MaxAds.getsDeviceInfo();
  }

  @NonNull
  public Observable<AdRequest> createAdRequest(@NonNull final String adUnitId) {
    return mDeviceInfo.getAdvertisingInfo().map(new Function<AdvertisingIdClient.Info, AdRequest>() {
      @Override
      public AdRequest apply(AdvertisingIdClient.Info info) throws Exception {
        return new AdRequest.Builder(adUnitId, MaxAds.API_VERSION, info.getId(), info.isLimitAdTrackingEnabled(), "",
          "", "", "", 1, 1, "", "", "", "").build();
      }
    });
  }
}
