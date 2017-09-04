package io.maxads.ads.base.api;

import android.support.annotation.NonNull;

import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.model.Winner;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
  @NonNull private final ApiService apiService;

  public ApiManager() {
    final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("http://ads.maxads.io")
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(new OkHttpClient.Builder()
//        .addNetworkInterceptor(new StethoInterceptor())
        .build())
      .build();
    apiService = retrofit.create(ApiService.class);
  }

  public Observable<Ad> getAd(@NonNull final AdRequest adRequest) {
    return apiService.getAd(adRequest.getAdUnitId(), adRequest)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .map(new Function<AdResponse, Ad>() {
        @Override
        public Ad apply(AdResponse adResponse) throws Exception {
          return new Ad(adResponse.creative, adResponse.prebidKeywords, adResponse.refresh,
            adResponse.impressionUrls, adResponse.clickUrls, adResponse.selectUrls,
            adResponse.errorUrls, new Winner(adResponse.winner.creativeType));
        }
      });
  }
}
