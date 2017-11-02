package io.maxads.ads.base.api;

import android.support.annotation.NonNull;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.util.TimeZone;

import io.maxads.ads.base.DeviceInfo;
import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.SessionDepthManager;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class ErrorRequestFactory {

  @NonNull private final DeviceInfo mDeviceInfo;
  @NonNull private final SessionDepthManager mSessionDepthManager;

  public ErrorRequestFactory() {
    mDeviceInfo = MaxAds.getDeviceInfo();
    mSessionDepthManager = MaxAds.getSessionDepthManager();
  }

  @NonNull
  public Observable<ErrorRequest> createErrorRequest(@NonNull final String message) {
    return mDeviceInfo.getAdvertisingInfo()
        .map(new Function<AdvertisingIdClient.Info, ErrorRequest>() {
          @Override
          public ErrorRequest apply(AdvertisingIdClient.Info info) throws Exception {
            return new ErrorRequest.Builder(
                message,
                MaxAds.API_VERSION,
                MaxAds.SDK_VERSION,
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
