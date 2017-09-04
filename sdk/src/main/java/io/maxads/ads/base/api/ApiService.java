package io.maxads.ads.base.api;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
  @POST("/ads/req/{adUnitId}")
  Observable<AdResponse> getAd(@Path("adUnitId") String adUnitId, @Body AdRequest adRequest);
}
