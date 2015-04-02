package com.poepoemyintswe.foodmagnet.api;

import com.poepoemyintswe.foodmagnet.model.Data;
import retrofit.Callback;
import retrofit.http.GET;

import static com.poepoemyintswe.foodmagnet.Config.END_POINT;

/**
 * Created by poepoe on 2/4/15.
 */
public interface MapService {
  @GET(END_POINT) void getLessons(Callback<Data> callback);
}
