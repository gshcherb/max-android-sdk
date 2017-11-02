package io.maxads.ads.base.api;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;

import io.maxads.ads.base.DeviceInfo;
import io.maxads.ads.base.SessionDepthManager;
import io.reactivex.functions.Consumer;

import static com.google.common.truth.Truth.assertThat;
import static io.reactivex.Observable.just;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AdRequestFactoryTest {
  @Mock private DeviceInfo mMockDeviceInfo;
  @Mock private SessionDepthManager mMockSessionDepthManager;
  private Gson mGson;

  @InjectMocks private AdRequestFactory mSubject;

  @Before
  public void setup() {
    initMocks(this);
    mGson = new Gson();
    when(mMockDeviceInfo.getAdvertisingInfo()).thenReturn(just(new AdvertisingIdClient.Info("ifa", true)));
    when(mMockDeviceInfo.getAppVersion()).thenReturn("appVersion");
    when(mMockDeviceInfo.getTimeZoneShortDisplayName()).thenReturn("timeZone");
    when(mMockDeviceInfo.getLocale()).thenReturn(Locale.ENGLISH);
    when(mMockDeviceInfo.getOrientation()).thenReturn(DeviceInfo.Orientation.LANDSCAPE);
    when(mMockDeviceInfo.getScreenWidthPx()).thenReturn(320);
    when(mMockDeviceInfo.getScreenHeightPx()).thenReturn(50);
    when(mMockDeviceInfo.getBrowserAgent()).thenReturn("browserAgent");
    when(mMockDeviceInfo.getModel()).thenReturn("model");
    when(mMockDeviceInfo.getConnectivity()).thenReturn(DeviceInfo.Connectivity.WIFI);
    when(mMockDeviceInfo.getCarrierName()).thenReturn("carrierName");
    when(mMockSessionDepthManager.getSessionDepth()).thenReturn(3);
  }
  @Test
  public void createAdRequest() throws Exception {
    mSubject.createAdRequest("adUnitId")
      .subscribe(new Consumer<AdRequest>() {
        @Override
        public void accept(AdRequest adRequest) throws Exception {
          final String json = mGson.toJson(adRequest);
          final Type type = new TypeToken<Map<String, Object>>(){}.getType();
          final Map<String, Object> map = mGson.fromJson(json, type);

          assertThat(adRequest.getAdUnitId()).isEqualTo("adUnitId");
          assertThat(map.get("v")).isEqualTo("1.0");
          assertThat(map.get("sdk_v")).isEqualTo("0.5.0");
          assertThat(map.get("app_v")).isEqualTo("appVersion");
          assertThat(map.get("ifa")).isEqualTo("ifa");
          assertThat(map.get("lmt")).isEqualTo(true);
          assertThat(map.get("vendor_id")).isEqualTo("vendorId");
          assertThat(map.get("tz")).isEqualTo("timeZone");
          assertThat(map.get("locale")).isEqualTo("en");
          assertThat(map.get("orientation")).isEqualTo("orientation");
          assertThat(map.get("w")).isEqualTo(320.0);
          assertThat(map.get("h")).isEqualTo(50.0);
          assertThat(map.get("browser_agent")).isEqualTo("browserAgent");
          assertThat(map.get("connectivity")).isEqualTo(DeviceInfo.Connectivity.WIFI.toString());
          assertThat(map.get("carrier")).isEqualTo("carrier");
          assertThat(map.get("session_depth")).isEqualTo(3.0);
        }
      });
  }
}
