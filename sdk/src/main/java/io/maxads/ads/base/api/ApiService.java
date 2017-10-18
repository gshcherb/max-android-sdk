package io.maxads.ads.base.api;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {
  @NonNull String AD_REQUEST_PATH = "/ads/req/";

  @POST(AD_REQUEST_PATH + "{adUnitId}")
  Observable<AdResponse> getAd(@Path("adUnitId") String adUnitId, @Body AdRequest adRequest);

  @GET
  Observable<Void> trackUrl(@Url String url);

  @POST("/events/client-error")
  Observable<Void> trackError(@Body ErrorRequest errorRequest);
}
