package io.maxads.ads.base.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.maxads.ads.base.MaxAds;
import io.maxads.ads.base.model.Ad;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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

          return Ad.from(adRequest.getAdUnitId(), adResponse);
        }
      });
  }

  public void trackUrl(@NonNull String url) {
    mApiService.trackUrl(url)
      .subscribeOn(Schedulers.io())
      .retryWhen(new ExponentialBackoff(Jitter.DEFAULT, 2, 30, TimeUnit.SECONDS, 100))
      .subscribe();
  }

  public void trackError(@NonNull String message) {
    new ErrorRequestFactory()
      .createErrorRequest(message)
      .subscribe(new Consumer<ErrorRequest>() {
        @Override
        public void accept(ErrorRequest errorRequest) throws Exception {
          mApiService.trackError(errorRequest)
            .subscribeOn(Schedulers.io())
            .retryWhen(new ExponentialBackoff(Jitter.DEFAULT, 2, 30, TimeUnit.SECONDS, 100))
            .subscribe();
        }
      });
  }
}
