package io.maxads.ads.base.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.model.Ad;
import io.maxads.ads.base.model.Winner;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
  @NonNull private final ApiService mApiService;

  public ApiClient(@Nullable Interceptor applicationInterceptor, @Nullable Interceptor networkInterceptor) {
    final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    if (applicationInterceptor != null) {
      builder.addInterceptor(applicationInterceptor);
    }
    if (networkInterceptor != null) {
      builder.addNetworkInterceptor(networkInterceptor);
    }

    final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("http://" + MaxAds.HOST)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(builder.build())
      .build();
    mApiService = retrofit.create(ApiService.class);
  }

  public Observable<Ad> getAd(@NonNull final AdRequest adRequest) {
    MaxAds.getSessionDepthManager().incrementSessionDepth();
    return mApiService.getAd(adRequest.getAdUnitId(), adRequest)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .retryWhen(new ExponentialBackoff(Jitter.DEFAULT, 1, TimeUnit.SECONDS, 5))
      .map(new Function<AdResponse, Ad>() {
        @Override
        public Ad apply(AdResponse adResponse) throws Exception {
          return new Ad(adRequest.getAdUnitId(), adResponse.creative, adResponse.prebidKeywords, adResponse.refresh,
            adResponse.impressionUrls, adResponse.clickUrls, adResponse.selectedUrls,
            adResponse.errorUrls, new Winner(adResponse.winner.creativeType));
        }
      });
  }

  public Observable<Void> trackUrl(@NonNull String url) {
    return mApiService.trackUrl(url)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .retryWhen(new ExponentialBackoff(Jitter.DEFAULT, 1, TimeUnit.SECONDS, 5));
  }

  public Observable<Void> trackError(@NonNull String message) {
    return new ErrorRequestFactory()
        .createErrorRequest(message)
        .flatMap(new Function<ErrorRequest, Observable<Void>>() {
          @Override
          public Observable<Void> apply(ErrorRequest errorRequest) throws Exception {
            return mApiService.trackError(errorRequest);
          }
        });
  }
}
