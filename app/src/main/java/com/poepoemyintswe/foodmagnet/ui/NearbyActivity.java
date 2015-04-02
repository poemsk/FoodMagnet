package com.poepoemyintswe.foodmagnet.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.poepoemyintswe.foodmagnet.R;
import com.poepoemyintswe.foodmagnet.adapter.LocationAdapter;
import com.poepoemyintswe.foodmagnet.api.MapService;
import com.poepoemyintswe.foodmagnet.model.Data;
import com.poepoemyintswe.foodmagnet.model.Result;
import com.poepoemyintswe.foodmagnet.utils.CustomRestAdapter;
import com.poepoemyintswe.foodmagnet.utils.GPSTracker;
import com.poepoemyintswe.foodmagnet.utils.NetworkConnectivityCheck;
import java.util.ArrayList;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class NearbyActivity extends ActionBarActivity
    implements GoogleMap.OnMyLocationChangeListener {

  @InjectView(R.id.toolbar) Toolbar mToolbar;
  @InjectView(R.id.list) RecyclerView mRecyclerView;
  private GoogleMap mMap;
  private LocationAdapter adapter;
  private Circle mCircle;
  private Marker mMarker;

  private double mLatitude, mLongitude;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nearby);
    ButterKnife.inject(this);

    //toolbar
    setSupportActionBar(mToolbar);

    //initialize RecyclerView
    initRecyclerView();
    List<Result> results = new ArrayList<>();
    adapter = new LocationAdapter(results);
    mRecyclerView.setAdapter(adapter);

    //logging
    Timber.tag("NearbyActivity");

    //fetch data
    getNearbyShops();

    //initialize map
    setUpMapIfNeeded();
  }

  private void getNearbyShops() {
    if (GPSTracker.getInstance(this).canGetLocation()) {
      mLatitude = GPSTracker.getInstance(this).getLatitude();
      mLongitude = GPSTracker.getInstance(this).getLongitude();
      String location = mLatitude + "," + mLongitude;
      Timber.d(location);
      if (NetworkConnectivityCheck.getInstance(this).isConnected()) {
        MapService mapService =
            CustomRestAdapter.getInstance(this).normalRestAdapter().create(MapService.class);
        mapService.getNearbyShops(location, 100, "bakery|bar|cafe|food|restaurant",
            getString(R.string.google_maps_key), new Callback<Data>() {
              @Override public void success(Data data, Response response) {
                adapter.addAll(data.results);

                Timber.d("Response status :" + response.getStatus());
              }

              @Override public void failure(RetrofitError error) {

              }
            });
      }
    }
  }

  @Override public void onResume() {
    super.onResume();
    setUpMapIfNeeded();
  }

  private void setUpMapIfNeeded() {

    // Do a null check to confirm that we have not already instantiated the map.
    if (mMap == null) {
      // Try to obtain the map from the SupportMapFragment.
      mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
      mMap.setOnMyLocationChangeListener(this);
      // Check if we were successful in obtaining the map.
      if (mMap != null) {
        //setUpMap();
        drawMarkerWithCircle(new LatLng(mLatitude, mLongitude));
      }
    }
  }

  private void setUpMap() {
    mMap.addMarker(new MarkerOptions().position(new LatLng(mLatitude, mLongitude)).title("Marker"));
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 12.0f));
  }

  public void initRecyclerView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    layoutManager.scrollToPosition(0);
    layoutManager.setSmoothScrollbarEnabled(true);
    mRecyclerView.setLayoutManager(layoutManager);
  }

  @Override public void onMyLocationChange(Location location) {
    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    if (mCircle == null || mMarker == null) {
      drawMarkerWithCircle(latLng);
    } else {
      updateMarkerWithCircle(latLng);
    }
  }

  private void updateMarkerWithCircle(LatLng position) {
    mCircle.setCenter(position);
    mMarker.setPosition(position);
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 17.0f));
  }

  private void drawMarkerWithCircle(LatLng position) {
    double radiusInMeters = 100.0;
    int strokeColor = 0xffff0000; //red outline
    int shadeColor = 0x44ff0000; //opaque red fill

    CircleOptions circleOptions = new CircleOptions().center(position)
        .radius(radiusInMeters)
        .fillColor(shadeColor)
        .strokeColor(strokeColor)
        .strokeWidth(4);
    mCircle = mMap.addCircle(circleOptions);

    MarkerOptions markerOptions = new MarkerOptions().position(position);
    mMarker = mMap.addMarker(markerOptions);
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 17.0f));
  }
}
