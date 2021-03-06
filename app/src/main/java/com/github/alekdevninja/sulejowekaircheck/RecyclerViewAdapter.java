package com.github.alekdevninja.sulejowekaircheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.alekdevninja.sulejowekaircheck.sensorTools.Sensor;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Sensor> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, List<Sensor> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.sensor_view, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sensor sensor = mData.get(position);

        holder.sensorName.setText(String.valueOf(sensor.getSensorId()));

        //add the unit of measurement to the updated text
        String pm25WithUnits = sensor.getPm25Value() + " ug/m3 (PM2.5)";

        holder.pm25Value.setText(pm25WithUnits);

        holder.percentOfNorm.setText(sensor.getPm25Percentage());
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sensorName;
        TextView pm25Value;
        TextView percentOfNorm;

        ViewHolder(View itemView) {
            super(itemView);
            sensorName = itemView.findViewById(R.id.textView_sensorName);
            pm25Value = itemView.findViewById(R.id.textView_pm25value);
            percentOfNorm = itemView.findViewById(R.id.textView_percentageValue);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Sensor getItem(int id) {
        return mData.get(id);
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }
}