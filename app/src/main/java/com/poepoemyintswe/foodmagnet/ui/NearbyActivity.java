package com.poepoemyintswe.foodmagnet.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.poepoemyintswe.foodmagnet.utils.SharePref;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import java.util.ArrayList;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;
import tr.xip.errorview.ErrorView;
import tr.xip.errorview.RetryListener;

public class NearbyActivity extends ActionBarActivity
    implements GoogleMap.OnMyLocationChangeListener {

  @InjectView(R.id.toolbar) Toolbar mToolbar;
  @InjectView(R.id.list) RecyclerView mRecyclerView;
  @InjectView(R.id.lat_long) TextView mLatLongTextView;
  @InjectView(R.id.progress_bar) ProgressWheel mProgressWheel;
  @InjectView(R.id.nearby) TextView nearbyRange;
  @InjectView(R.id.sliding_layout) SlidingUpPanelLayout mLayout;
  @InjectView(R.id.error_view) ErrorView mErrorView;
  @InjectView(R.id.card) CardView mCardView;

  private GoogleMap mMap;
  private LocationAdapter adapter;
  private Circle mCircle;
  private Marker mMarker;
  private String location;

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
    mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
    mRecyclerView.setAdapter(adapter);

    nearbyRange.setText(String.format(getResources().getString(R.string.nearby),
        SharePref.getInstance(NearbyActivity.this).getRange()));

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
    getNearbyShops(SharePref.getInstance(NearbyActivity.this).getRange());

    //initialize map
    setUpMapIfNeeded();

    mErrorView.setOnRetryListener(new RetryListener() {
      @Override public void onRetry() {
        getNearbyShops(SharePref.getInstance(NearbyActivity.this).getRange());
      }
    });
  }

  private void getNearbyShops(double range) {
    if (NetworkConnectivityCheck.getInstance(this).isConnected()) {
      showHideProgressBar(true);
      showHideErrorView(false);
      MapService mapService =
          CustomRestAdapter.getInstance(this).normalRestAdapter().create(MapService.class);
      mapService.getNearbyShops(location, range, "bakery|bar|cafe|food|restaurant",
          getString(R.string.google_maps_key), new Callback<Data>() {
            @Override public void success(Data data, Response response) {
              showHideProgressBar(false);
              if (data.results.size() > 0) {
                adapter.addAll(data.results);
                for (int i = 0; i < data.results.size(); i++) {
                  Result result = data.results.get(i);
                  Geometry geometry = data.results.get(i).geometry;
                  mMap.addMarker(new MarkerOptions().position(
                      new LatLng(geometry.location.lat, geometry.location.lng)).title(result.name));
                }
              } else {
                adapter.clearAll();
                showHideErrorView(true);
              }

              Timber.d("Response status :" + response.getStatus());
            }

            @Override public void failure(RetrofitError error) {
              showHideErrorView(true);
              showHideProgressBar(false);
            }
          });
    } else {
      showHideErrorView(true);
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
    mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0
            : recyclerView.getChildAt(0).getTop();
        if (mLayout.isTouchEnabled()) mLayout.setEnabled(topRowVerticalPosition >= 0);
      }
    });
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
        .radius(SharePref.getInstance(NearbyActivity.this).getRange())
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

  private void showHideErrorView(boolean show) {
    mErrorView.setVisibility(show ? View.VISIBLE : View.GONE);
    mCardView.setVisibility(show ? View.GONE : View.VISIBLE);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_nearby, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_range) {
      openRangeChangeDialog();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void openRangeChangeDialog() {
    View dialogView = View.inflate(this, R.layout.dialog_range_changed, null);
    final EditText editText = (EditText) dialogView.findViewById(R.id.range_fill_text);
    editText.setText(Integer.toString(SharePref.getInstance(this).getRange()));
    AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(R.string.change_range)
        .setView(dialogView)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            int range = Integer.parseInt(editText.getText().toString().trim());
            SharePref.getInstance(NearbyActivity.this).setRange(range);
            nearbyRange.setText(String.format(getResources().getString(R.string.nearby),
                SharePref.getInstance(NearbyActivity.this).getRange()));
            adapter.clearAll();
            getNearbyShops(range);
            mCircle.remove();
            drawMarkerWithCircle(new LatLng(mLatitude, mLongitude));
          }
        })
        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
    Dialog dialog = builder.create();
    dialog.show();
  }
}
