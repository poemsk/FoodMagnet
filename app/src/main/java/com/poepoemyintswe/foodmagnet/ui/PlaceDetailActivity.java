package com.poepoemyintswe.foodmagnet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.poepoemyintswe.foodmagnet.R;
import com.poepoemyintswe.foodmagnet.model.Result;
import timber.log.Timber;

public class PlaceDetailActivity extends ActionBarActivity {
  @InjectView(R.id.toolbar) Toolbar mToolbar;

  private Result result;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getResult();
    setContentView(R.layout.activity_place_detail);
    ButterKnife.inject(this);
    //toolbar
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle(result.name);
    getResult();

    Timber.tag(PlaceDetailActivity.class.getSimpleName());
  }

  private void getResult() {
    Intent intent = getIntent();
    result = (Result) intent.getSerializableExtra("result");
  }
  //@Override public boolean onCreateOptionsMenu(Menu menu) {
  //  // Inflate the menu; this adds items to the action bar if it is present.
  //  getMenuInflater().inflate(R.menu.menu_place_detail, menu);
  //  return true;
  //}
  //
  //@Override public boolean onOptionsItemSelected(MenuItem item) {
  //  // Handle action bar item clicks here. The action bar will
  //  // automatically handle clicks on the Home/Up button, so long
  //  // as you specify a parent activity in AndroidManifest.xml.
  //  int id = item.getItemId();
  //
  //  //noinspection SimplifiableIfStatement
  //  if (id == R.id.action_settings) {
  //    return true;
  //  }
  //
  //  return super.onOptionsItemSelected(item);
  //}
}
