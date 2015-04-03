package com.poepoemyintswe.foodmagnet.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.bumptech.glide.Glide;
import com.poepoemyintswe.foodmagnet.R;
import com.poepoemyintswe.foodmagnet.model.Result;
import com.poepoemyintswe.foodmagnet.ui.PlaceDetailActivity;
import java.util.List;

/**
 * Created by poepoe on 2/4/15.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

  private List<Result> results;

  public LocationAdapter(List<Result> results) {
    setHasStableIds(true);
    this.results = results;
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_location, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
    holder.bindResult(results.get(position));
  }

  public void addAll(List<Result> data) {
    int startIndex = results.size();
    results.addAll(startIndex, data);
    notifyItemRangeInserted(startIndex, data.size());
    notifyDataSetChanged();
  }

  public void clearAll() {
    results.clear();
    notifyDataSetChanged();
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getItemCount() {
    return results.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @InjectView(R.id.icon) ImageView icon;
    @InjectView(R.id.name) TextView name;
    @InjectView(R.id.vicinity) TextView vicinity;
    @InjectView(R.id.opening) TextView opening;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.inject(this, view);
      itemView.setOnClickListener(this);
    }

    public void bindResult(Result result) {
      name.setText(result.name);
      vicinity.setText(result.vicinity);
      try {
        if (result.opening_hours.open_now) {
          opening.setText(
              (String.format(itemView.getResources().getString(R.string.opening), "Yes")));
        } else {
          opening.setText(
              (String.format(itemView.getResources().getString(R.string.opening), "No")));
        }
      } catch (NullPointerException e) {
        opening.setText((String.format(itemView.getResources().getString(R.string.opening),
            "Data is not provided")));
      }

      Glide.with(itemView.getContext()).load(result.icon).into(icon);
    }

    @Override public void onClick(View v) {
      Intent intent = new Intent(itemView.getContext(), PlaceDetailActivity.class);
      itemView.getContext().startActivity(intent);
    }
  }
}
