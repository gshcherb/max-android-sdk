package io.maxads.ads.base.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
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
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
  @NonNull private final ApiService mApiService;

  public ApiClient(@NonNull List<Interceptor> applicationInterceptors, @NonNull List<Interceptor> networkInterceptors) {
    final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    for (Interceptor applicationInterceptor : applicationInterceptors) {
      builder.addInterceptor(applicationInterceptor);
    }
    for (Interceptor networkInterceptor : networkInterceptors) {
      builder.addNetworkInterceptor(networkInterceptor);
    }

    final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("https://" + MaxAds.HOST)
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
      .retryWhen(new ExponentialBackoff(Jitter.DEFAULT, 2, 30, TimeUnit.SECONDS, 100))
      .map(new Function<Response<AdResponse>, Ad>() {
        @Nullable
        @Override
        public Ad apply(Response<AdResponse> response) throws Exception {
          final AdResponse adResponse = response.body();
          if (adResponse == null) {
            return null;
          }

          final WinnerResponse winner = adResponse.winner;
          final Winner.CreativeType creativeType = Winner.CreativeType.from(winner == null ? null : winner.creativeType);

          return new Ad(adRequest.getAdUnitId(), adResponse.creative, adResponse.prebidKeywords, adResponse.refresh,
            adResponse.impressionUrls, adResponse.clickUrls, adResponse.selectedUrls,
            adResponse.errorUrls, new Winner(creativeType));
        }
      });
  }

  public Observable<Response<Void>> trackUrl(@NonNull String url) {
    return mApiService.trackUrl(url)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .retryWhen(new ExponentialBackoff(Jitter.DEFAULT, 2, 30, TimeUnit.SECONDS, 100));
  }

  public Observable<Response<Void>> trackError(@NonNull String message) {
    return new ErrorRequestFactory()
        .createErrorRequest(message)
        .flatMap(new Function<ErrorRequest, Observable<Response<Void>>>() {
          @Override
          public Observable<Response<Void>> apply(ErrorRequest errorRequest) throws Exception {
            return mApiService.trackError(errorRequest);
          }
        });
  }
}
