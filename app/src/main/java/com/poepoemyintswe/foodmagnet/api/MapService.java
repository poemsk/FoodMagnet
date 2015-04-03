package com.poepoemyintswe.foodmagnet.api;

import com.poepoemyintswe.foodmagnet.model.Data;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

import static com.poepoemyintswe.foodmagnet.Config.END_POINT;
import static com.poepoemyintswe.foodmagnet.Config.KEY;
import static com.poepoemyintswe.foodmagnet.Config.LOCATION;
import static com.poepoemyintswe.foodmagnet.Config.RADIUS;
import static com.poepoemyintswe.foodmagnet.Config.TYPES;

/**
 * Created by poepoe on 2/4/15.
 */
public interface MapService {
  @GET(END_POINT) void getNearbyShops(@Query(LOCATION) String location,
      @Query(RADIUS) double radius, @Query(TYPES) String types, @Query(KEY) String key,
      Callback<Data> callback);
}
