package com.poepoemyintswe.foodmagnet.utils;

import android.app.Activity;
import android.content.Context;
import com.poepoemyintswe.foodmagnet.BuildConfig;
import com.poepoemyintswe.foodmagnet.Config;
import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by poepoe on 12/12/14.
 */
public class CustomRestAdapter {
  private static CustomRestAdapter customRestAdapter;

  private Context mContext;
  private OkHttpClient okHttpClient = new OkHttpClient();

  public CustomRestAdapter(Context mContext) {
    this.mContext = mContext;
  }

  public static synchronized CustomRestAdapter getInstance(Context mContext) {
    if (customRestAdapter == null) {
      customRestAdapter = new CustomRestAdapter(mContext);
    }
    return customRestAdapter;
  }

  public RestAdapter GsonRestAdapter() {

    RestAdapter restAdapter;
    if (BuildConfig.DEBUG) {
      restAdapter = new RestAdapter.Builder().setEndpoint(Config.BASE_URL)
          .setLogLevel(RestAdapter.LogLevel.BASIC)
          .setClient(new OkClient(okHttpClient))
          .setErrorHandler(new RetrofitErrorHandler((Activity) mContext))
          .build();
    } else {
      restAdapter = new RestAdapter.Builder().setEndpoint(Config.BASE_URL)
          .setClient(new OkClient(okHttpClient))
          .setErrorHandler(new RetrofitErrorHandler((Activity) mContext))
          .build();
    }

    return restAdapter;
  }

  public RestAdapter normalRestAdapter() {

    RestAdapter restAdapter;
    if (BuildConfig.DEBUG) {
      restAdapter = new RestAdapter.Builder().setEndpoint(Config.BASE_URL)
          .setLogLevel(RestAdapter.LogLevel.BASIC)
          .setClient(new OkClient(okHttpClient))
          .setErrorHandler(new RetrofitErrorHandler((Activity) mContext))
          .build();
    } else {
      restAdapter = new RestAdapter.Builder().setEndpoint(Config.BASE_URL)
          .setClient(new OkClient(okHttpClient))
          .setErrorHandler(new RetrofitErrorHandler((Activity) mContext))
          .build();
    }

    return restAdapter;
  }
}
