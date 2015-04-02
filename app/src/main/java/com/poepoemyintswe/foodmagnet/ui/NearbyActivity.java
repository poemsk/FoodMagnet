package com.poepoemyintswe.foodmagnet.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.poepoemyintswe.foodmagnet.R;

public class NearbyActivity extends FragmentActivity {

  @InjectView(R.id.title) TextView title;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nearby);
    ButterKnife.inject(this);
    title.setText(getString(R.string.app_name));
  }
}
