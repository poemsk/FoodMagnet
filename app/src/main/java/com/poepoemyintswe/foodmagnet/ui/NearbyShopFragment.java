package com.poepoemyintswe.foodmagnet.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.poepoemyintswe.foodmagnet.R;

/**
 * Created by poepoe on 2/4/15.
 */
public class NearbyShopFragment extends Fragment {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
    ButterKnife.inject(this, rootView);
    return rootView;
  }
}
