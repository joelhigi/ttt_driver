package com.tartantransporttracker.driver.ui.route;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tartantransporttracker.driver.R;
import com.tartantransporttracker.driver.models.Route;

import java.util.List;

public class ViewRouteAdapter extends RecyclerView.Adapter<ViewRouteVH>{
    List<Route> routes;
    public ViewRouteAdapter(List<Route> routeItem)
    {
        this.routes = routeItem;
    }

    @NonNull
    @Override
    public ViewRouteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new ViewRouteVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewRouteVH holder, int position) {

        holder.routeName.setText(routes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }
}
