package com.poepoemyintswe.foodmagnet.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.poepoemyintswe.foodmagnet.R;
import com.poepoemyintswe.foodmagnet.api.MapService;
import com.poepoemyintswe.foodmagnet.model.Data;
import com.poepoemyintswe.foodmagnet.utils.CustomRestAdapter;
import com.poepoemyintswe.foodmagnet.utils.GPSTracker;
import com.poepoemyintswe.foodmagnet.utils.NetworkConnectivityCheck;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class NearbyActivity extends FragmentActivity {

  @InjectView(R.id.title) TextView title;
  private GoogleMap mMap;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nearby);
    ButterKnife.inject(this);
    title.setText(getString(R.string.app_name));
    Timber.tag("NearbyActivity");
    setUpMapIfNeeded();
    getNearbyShops();
  }

  private void getNearbyShops() {
    if (GPSTracker.getInstance(this).canGetLocation()) {
      String location =
          GPSTracker.getInstance(this).getLatitude() + "," + GPSTracker.getInstance(this)
              .getLongitude();
      Timber.d(location);
      if (NetworkConnectivityCheck.getInstance(this).isConnected()) {
        MapService mapService =
            CustomRestAdapter.getInstance(this).normalRestAdapter().create(MapService.class);
        mapService.getNearbyShops(location, 100, "bakery|bar|cafe|food|restaurant",
            getString(R.string.google_maps_key), new Callback<Data>() {
              @Override public void success(Data data, Response response) {
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
      // Check if we were successful in obtaining the map.
      if (mMap != null) {
        setUpMap();
      }
    }
  }

  /**
   * This is where we can add markers or lines, add listeners or move the camera. In this case, we
   * just add a marker near Africa.
   * <p>
   * This should only be called once and when we are sure that {@link #mMap} is not null.
   */
  private void setUpMap() {
    mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
  }
}
