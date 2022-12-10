package com.tartantransporttracker.ui.busStop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tartantransporttracker.R;
import com.tartantransporttracker.models.BusStop;
import com.tartantransporttracker.models.Route;

import java.nio.file.Files;
import java.util.List;

public class BusStopAdapter extends RecyclerView.Adapter<BusStopVH> {
    public List<BusStop> busStopViewList;

    public BusStopAdapter(List<BusStop> busStopViewList) {
        this.busStopViewList = busStopViewList;
    }

    @NonNull
    @Override
    public BusStopVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_stop_item, parent, false);
        return new BusStopVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull BusStopVH holder, int position) {
        if(busStopViewList!=null){
            holder.bStopName.setText(busStopViewList.get(position).getBusStopName());
//            holder.numberOfBusStops.setText(String.valueOf(busStopViewList.size()));
            Route route = busStopViewList.get(position).getRoute();
            if( route != null)
            {
                holder.routeName.setText(route.getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return busStopViewList.size();
    }
}
