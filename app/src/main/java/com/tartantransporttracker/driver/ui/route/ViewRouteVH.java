package com.tartantransporttracker.driver.ui.route;

import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tartantransporttracker.driver.R;
import com.tartantransporttracker.driver.managers.RouteManager;
import com.tartantransporttracker.driver.models.Route;

class ViewRouteVH extends RecyclerView.ViewHolder {

    TextView routeName;

    public ViewRouteAdapter viewRouteAdapter;
    private RouteManager routeManager = RouteManager.getInstance();

    public ViewRouteVH(@NonNull View itemView) {
        super(itemView);
        routeName = itemView.findViewById(R.id.textView6);
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
