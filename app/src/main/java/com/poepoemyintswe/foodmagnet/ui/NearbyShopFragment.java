package com.poepoemyintswe.foodmagnet.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.poepoemyintswe.foodmagnet.R;

/**
 * Created by poepoe on 2/4/15.
 */
public class NearbyShopFragment extends Fragment {
  private static final int REQUEST_PLACE_PICKER = 1;
  @InjectView(R.id.place_name) TextView mName;
  @InjectView(R.id.place_address) TextView mAddress;
  @InjectView(R.id.place_attribute) TextView mAttributions;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  //private void initPlacePicker() {
  //  try {
  //    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
  //    Intent intent = intentBuilder.build(getActivity());
  //    startActivityForResult(intent, REQUEST_PLACE_PICKER);
  //  } catch (GooglePlayServicesRepairableException e) {
  //    GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), getActivity(), 0);
  //  } catch (GooglePlayServicesNotAvailableException e) {
  //    Toast.makeText(getActivity(), "Google Play Services is not available.", Toast.LENGTH_LONG)
  //        .show();
  //  }
  //}

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
    ButterKnife.inject(this, rootView);
    return rootView;
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == REQUEST_PLACE_PICKER && resultCode == Activity.RESULT_OK) {

      // The user has selected a place. Extract the name and address.
      final Place place = PlacePicker.getPlace(data, getActivity());

      final CharSequence name = place.getName();
      final CharSequence address = place.getAddress();
      String attributions = PlacePicker.getAttributions(data);
      if (attributions == null) {
        attributions = "";
      }

      mName.setText(name);
      mAddress.setText(address);
      mAttributions.setText(Html.fromHtml(attributions));
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }
}
