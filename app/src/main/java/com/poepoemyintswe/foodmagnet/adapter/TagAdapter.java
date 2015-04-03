package com.poepoemyintswe.foodmagnet.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.poepoemyintswe.foodmagnet.R;
import com.poepoemyintswe.foodmagnet.ui.widget.FlowLayout;
import java.util.List;

/**
 * Created by poepoe on 3/4/15.
 */
public class TagAdapter {
  private FlowLayout flowLayout;
  private Context mContext;
  private List<String> tags;

  public TagAdapter(Context mContext, FlowLayout flowLayout, List<String> tags) {
    this.mContext = mContext;
    this.flowLayout = flowLayout;
    this.tags = tags;
    initializeLayout();
  }

  public void initializeLayout() {
    for (String tag : tags) {
      ViewGroup.LayoutParams layoutParams =
          new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
              LinearLayout.LayoutParams.WRAP_CONTENT);
      TextView textView = new TextView(mContext);
      textView.setLayoutParams(layoutParams);
      textView.setTextAppearance(mContext, R.style.tagStyle);
      textView.setPadding(10, 10, 10, 10);
      textView.setText("#" + tag);
      textView.setBackgroundResource(R.drawable.transparent_button);
      flowLayout.addView(textView);
    }
  }
}
