package com.poepoemyintswe.foodmagnet.ui;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
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
import com.pnikosis.materialishprogress.ProgressWheel;
import com.poepoemyintswe.foodmagnet.R;
import com.poepoemyintswe.foodmagnet.adapter.LocationAdapter;
import com.poepoemyintswe.foodmagnet.api.MapService;
import com.poepoemyintswe.foodmagnet.model.Data;
import com.poepoemyintswe.foodmagnet.model.Geometry;
import com.poepoemyintswe.foodmagnet.model.Result;
import com.poepoemyintswe.foodmagnet.ui.widget.DividerItemDecoration;
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
  @InjectView(R.id.lat_long) TextView mLatLongTextView;
  @InjectView(R.id.progress_bar) ProgressWheel mProgressWheel;
  @InjectView(R.id.nearby) TextView nearbyRange;
  private GoogleMap mMap;
  private LocationAdapter adapter;
  private Circle mCircle;
  private Marker mMarker;
  private String location;

  private double mLatitude, mLongitude;

  private double range = 100.0;

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
    mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
    mRecyclerView.setAdapter(adapter);
    nearbyRange.setText(String.format(getResources().getString(R.string.nearby), ((int) range)));

    if (GPSTracker.getInstance(this).canGetLocation()) {
      mLatitude = GPSTracker.getInstance(this).getLatitude();
      mLongitude = GPSTracker.getInstance(this).getLongitude();
      location = mLatitude + "," + mLongitude;
      mLatLongTextView.setText("(" + location + ")");
      Timber.d(location);
    }

    //logging
    Timber.tag("NearbyActivity");

    //fetch data
    getNearbyShops();

    //initialize map
    setUpMapIfNeeded();
  }

  private void getNearbyShops() {
    if (NetworkConnectivityCheck.getInstance(this).isConnected()) {
      showHideProgressBar(true);
      MapService mapService =
          CustomRestAdapter.getInstance(this).normalRestAdapter().create(MapService.class);
      mapService.getNearbyShops(location, range, "bakery|bar|cafe|food|restaurant",
          getString(R.string.google_maps_key), new Callback<Data>() {
            @Override public void success(Data data, Response response) {
              showHideProgressBar(false);
              adapter.addAll(data.results);
              for (int i = 0; i < data.results.size(); i++) {
                Result result = data.results.get(i);
                Geometry geometry = data.results.get(i).geometry;
                mMap.addMarker(new MarkerOptions().position(
                    new LatLng(geometry.location.lat, geometry.location.lng)).title(result.name));
              }

              Timber.d("Response status :" + response.getStatus());
            }

            @Override public void failure(RetrofitError error) {
              showHideProgressBar(false);
            }
          });
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
      if (mMap != null) {
        mMap.setOnMyLocationChangeListener(this);
        //setUpMap();
        drawMarkerWithCircle(new LatLng(mLatitude, mLongitude));
      }
    }
  }

  public void initRecyclerView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    layoutManager.scrollToPosition(0);
    layoutManager.setSmoothScrollbarEnabled(true);
    mRecyclerView.setLayoutManager(layoutManager);
  }

  @Override public void onMyLocationChange(Location location) {
    Timber.d("Location Change Listener");
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
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 17.5f));
  }

  private void drawMarkerWithCircle(LatLng position) {
    int strokeColor = getResources().getColor(R.color.primary); //red outline
    int shadeColor = getResources().getColor(R.color.light_blue); //opaque red fill

    CircleOptions circleOptions = new CircleOptions().center(position)
        .radius(range)
        .fillColor(shadeColor)
        .strokeColor(strokeColor)
        .strokeWidth(2);
    mCircle = mMap.addCircle(circleOptions);

    MarkerOptions markerOptions =
        new MarkerOptions().position(position).draggable(true).title("Current Location");
    mMarker = mMap.addMarker(markerOptions);
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 17.5f));
  }

  private void showHideProgressBar(boolean show) {
    mProgressWheel.setVisibility(show ? View.VISIBLE : View.GONE);
  }
}
