package com.tartantransporttracker.ui.route;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tartantransporttracker.R;
import com.tartantransporttracker.managers.BusStopManager;
import com.tartantransporttracker.managers.RouteManager;
import com.tartantransporttracker.models.Route;

class ViewRouteVH extends RecyclerView.ViewHolder {
    private BusStopManager busStopManager = BusStopManager.getInstance();
    public ViewRouteAdapter viewRouteAdapter;
    private RouteManager routeManager = RouteManager.getInstance();
    TextView routeName;
    TextView fromVenue;
    Button btnUpdateRoute;

    public ViewRouteVH(@NonNull View itemView) {
        super(itemView);
        routeName = itemView.findViewById(R.id.textView6);
        fromVenue = itemView.findViewById(R.id.fromVenue);

        itemView.findViewById(R.id.deleteBtn).setOnClickListener(view ->
        {
            Route thisRoute = null;
            for(Route route : viewRouteAdapter.routes)
            {
                if( route.getName().equals(routeName.getText().toString()))
                {
                    thisRoute = route;
                }
            }
            confirmRouteDeletion(itemView, thisRoute.getId());
        });
        itemView.findViewById(R.id.updateRouteItemBtn).setOnClickListener(view -> {
            Route thisRoute = null;
            Intent intent = new Intent(itemView.getContext(), UpdateRouteActivity.class);
            for(Route route : viewRouteAdapter.routes)
            {
                if( route.getName().equals(routeName.getText().toString()))
                {
                    intent.putExtra("id", route.getId());
                    itemView.getContext().startActivity(intent);
                }
            }


        });
        fromVenue.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

            alert.setTitle("Bus Stops:");
            if(fromVenue.getText().equals(""))
            {
                alert.setMessage("No bus stop found!");
            }else
            {
                alert.setMessage(fromVenue.getText());
            }
            alert.show();
        });
    }

    public ViewRouteVH linkAdapter(ViewRouteAdapter adapter) {
        this.viewRouteAdapter = adapter;
        return this;
    }

    public void confirmRouteDeletion(View view, String routeId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete?\nRoute Name:" + routeName.getText()+"\nRoute: id:"+routeId);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                routeManager.deleteRoute(routeId);
                viewRouteAdapter.routes.remove(getAbsoluteAdapterPosition());
                viewRouteAdapter.notifyItemRemoved(getAdapterPosition());
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
