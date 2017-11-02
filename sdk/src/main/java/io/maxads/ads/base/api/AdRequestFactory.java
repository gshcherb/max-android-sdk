package io.maxads.ads.base.api;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import io.maxads.ads.base.DeviceInfo;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.SessionDepthManager;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class AdRequestFactory {
  @NonNull private final DeviceInfo mDeviceInfo;
  @NonNull private final SessionDepthManager mSessionDepthManager;

  public AdRequestFactory() {
    this(MaxAds.getDeviceInfo(), MaxAds.getSessionDepthManager());
  }

  @VisibleForTesting
  AdRequestFactory(@NonNull DeviceInfo deviceInfo, @NonNull SessionDepthManager sessionDepthManager) {
    mDeviceInfo = deviceInfo;
    mSessionDepthManager = sessionDepthManager;
  }

  @NonNull
  public Observable<AdRequest> createAdRequest(@NonNull final String adUnitId) {
    return mDeviceInfo.getAdvertisingInfo()
      .map(new Function<AdvertisingIdClient.Info, AdRequest>() {
        @Override
        public AdRequest apply(AdvertisingIdClient.Info info) throws Exception {
          return new AdRequest.Builder(
            adUnitId,
            MaxAds.API_VERSION,
            MaxAds.SDK_VERSION,
            mDeviceInfo.getAppVersion(),
            info.getId(),
            info.isLimitAdTrackingEnabled(),
            "",
            mDeviceInfo.getTimeZoneShortDisplayName(),
            mDeviceInfo.getLocale().toString(),
            mDeviceInfo.getOrientation().toString(),
            mDeviceInfo.getScreenWidthPx(),
            mDeviceInfo.getScreenHeightPx(),
            mDeviceInfo.getBrowserAgent(),
            mDeviceInfo.getModel(),
            mDeviceInfo.getConnectivity().toString(),
            mDeviceInfo.getCarrierName(),
            mSessionDepthManager.getSessionDepth())
            .build();
        }
      });
  }
}
