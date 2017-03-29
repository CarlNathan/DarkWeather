package com.example.carludren.darkweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carludren.darkweather.R;
import com.example.carludren.darkweather.Weather.Day;

/**
 * Created by carludren on 3/28/17.
 */

public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context, Day[] days) {
        mContext = context;
        mDays = days;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; //not going to use this
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);
            holder.tempLabel = (TextView) convertView.findViewById(R.id.tempTextView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Day day = mDays[position];

        holder.iconImageView.setImageResource(day.getIconId());
        holder.tempLabel.setText(day.getTemperatureMax()+ "");

        if (position == 0) {
            holder.dayLabel.setText("Today");
        } else {
            holder.dayLabel.setText(day.getDayOfWeek());
        }
        holder.imgTempCircle = (ImageView) convertView.findViewById(R.id.circleImageView);
        holder.imgTempCircle.setImageResource(R.drawable.bg_temperature);
        return convertView;
    }

    private static class ViewHolder {
        public ImageView iconImageView;
        public TextView dayLabel;
        public TextView tempLabel;
        public ImageView imgTempCircle;

    }
}
