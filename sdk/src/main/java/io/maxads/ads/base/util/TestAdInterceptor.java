package io.maxads.ads.base.util;

import android.support.annotation.Nullable;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import io.maxads.ads.base.api.AdResponse;
import io.maxads.ads.base.api.ApiService;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TestAdInterceptor implements Interceptor {
  @Nullable private AdResponse mAdResponse;

  public void setAdResponse(@Nullable AdResponse adResponse) {
    mAdResponse = adResponse;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request request = chain.request();
    if (mAdResponse == null || !request.url().encodedPath().contains(ApiService.AD_REQUEST_PATH)) {
      return chain.proceed(request);
    }

    return new Response.Builder()
      .code(200)
      .message("")
      .request(request)
      .protocol(Protocol.HTTP_1_0)
      .body(ResponseBody.create(MediaType.parse("application/json"), new GsonBuilder().create().toJson(mAdResponse)))
      .addHeader("content-type", "application/json")
      .build();
  }
}
