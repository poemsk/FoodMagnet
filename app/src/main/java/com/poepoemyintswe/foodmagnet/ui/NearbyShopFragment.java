package com.poepoemyintswe.foodmagnet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.poepoemyintswe.foodmagnet.R;

/**
 * Created by poepoe on 2/4/15.
 */
public class NearbyShopFragment extends Fragment {
  private static final int REQUEST_PLACE_PICKER = 1;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onStart() {
    super.onStart();
    initPlacePicker();
  }

  private void initPlacePicker() {
    try {
      PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
      Intent intent = intentBuilder.build(getActivity());
      startActivityForResult(intent, REQUEST_PLACE_PICKER);
    } catch (GooglePlayServicesRepairableException e) {
      GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
    } catch (GooglePlayServicesNotAvailableException e) {
      Toast.makeText(getActivity(), "Google Play Services is not available.", Toast.LENGTH_LONG)
          .show();
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
    return rootView;
  }
}
