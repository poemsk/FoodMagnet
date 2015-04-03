package com.poepoemyintswe.foodmagnet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.bumptech.glide.Glide;
import com.poepoemyintswe.foodmagnet.Config;
import com.poepoemyintswe.foodmagnet.R;
import com.poepoemyintswe.foodmagnet.adapter.TagAdapter;
import com.poepoemyintswe.foodmagnet.model.Result;
import com.poepoemyintswe.foodmagnet.ui.widget.FlowLayout;
import timber.log.Timber;

public class PlaceDetailActivity extends ActionBarActivity {
  @InjectView(R.id.toolbar) Toolbar mToolbar;
  @InjectView(R.id.vicinity) TextView mAddressTextView;
  @InjectView(R.id.icon) ImageView mPhotoImage;
  @InjectView(R.id.tag_list) FlowLayout mTagList;
  private Result result;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getResult();
    setContentView(R.layout.activity_place_detail);
    ButterKnife.inject(this);
    //toolbar
    toolbarSetup();

    inflateResultView();

    Timber.tag(PlaceDetailActivity.class.getSimpleName());
  }

  private void getResult() {
    Intent intent = getIntent();
    result = (Result) intent.getSerializableExtra("result");
  }

  private void toolbarSetup() {
    setSupportActionBar(mToolbar);
    getSupportActionBar().setTitle(result.name);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  private void inflateResultView() {
    try {
      Glide.with(this)
          .load(Config.PHOTO_URL + result.photos.get(0).photoReference)
          .placeholder(R.mipmap.ic_launcher)
          .into(mPhotoImage);
    } catch (NullPointerException e) {
      Timber.e(e.getMessage().toString());
    }

    mAddressTextView.setText(result.vicinity);
    new TagAdapter(this, mTagList, result.types);
  }
}
