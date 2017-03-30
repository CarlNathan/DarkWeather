package com.example.carludren.darkweather.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carludren.darkweather.R;
import com.example.carludren.darkweather.Weather.Hour;

import butterknife.BindView;

/**
 * Created by carludren on 3/29/17.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Hour[] mHours;

    public HourAdapter(Hour[] hours) {
        mHours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);
        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {

        public TextView mTimeLabel;
        public ImageView mIconImageView;
        public TextView mSummaryLabel;
        public TextView mTempLabel;

        public HourViewHolder(View itemView) {
            super(itemView);
            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mIconImageView = (ImageView) itemView.findViewById(R.id.hourlyIconImageView);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.hourlySummaryLabel);
            mTempLabel = (TextView) itemView.findViewById(R.id.hourlyTempLabel);
        }

        public void bindHour(Hour hour) {
            mTimeLabel.setText(hour.getHour());
            mIconImageView.setImageResource(hour.getIconId());
            mSummaryLabel.setText(hour.getSummary());
            mTempLabel.setText(hour.getTemperature() + "");
        }
    }
}
