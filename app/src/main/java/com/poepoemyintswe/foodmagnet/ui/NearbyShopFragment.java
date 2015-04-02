package com.poepoemyintswe.foodmagnet.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
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

/**
 * Created by poepoe on 2/4/15.
 */
public class NearbyShopFragment extends Fragment {
  private NearbyActivity mActivity;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = (NearbyActivity) getActivity();
    Timber.tag("NearbyShopFragment");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
    ButterKnife.inject(this, rootView);
    return rootView;
  }

  @Override public void onStart() {
    super.onStart();
    getNearbyShops();
  }

  private void getNearbyShops() {
    if (GPSTracker.getInstance(mActivity).canGetLocation()) {
      String location =
          GPSTracker.getInstance(mActivity).getLatitude() + "," + GPSTracker.getInstance(mActivity)
              .getLongitude();
      Timber.d(location);
      if (NetworkConnectivityCheck.getInstance(mActivity).isConnected()) {
        MapService mapService =
            CustomRestAdapter.getInstance(mActivity).normalRestAdapter().create(MapService.class);
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
}
