package com.tartantransporttracker.driver.ui.busStop;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.tartantransporttracker.driver.R;
import com.tartantransporttracker.driver.managers.BusStopManager;
import com.tartantransporttracker.driver.models.BusStop;

import java.io.Serializable;

public class BusStopVH extends RecyclerView.ViewHolder implements  Serializable{
    private BusStopManager busStopManager = BusStopManager.getInstance();
    private BusStopAdapter busStopAdapter;
    TextView bStopName;
    TextView bStopId;
    TextView routeName;
    TextView numberOfBusStops;

    private BusStopView busStopView;
    public BusStopVH(View itemView)
    {
        super(itemView);
        bStopName = itemView.findViewById(R.id.busStopName);
        routeName = itemView.findViewById(R.id.routeName);
        numberOfBusStops = itemView.findViewById(R.id.numberOfBusStops);
        itemView.findViewById(R.id.busStop_deleteBtn).setOnClickListener(view ->
        {
            BusStop thisRoute = null;
            for(BusStop bstop : busStopAdapter.busStopViewList)
            {
                if( bstop.getBusStopName().equals(bStopName.getText().toString()))
                {
                    thisRoute = bstop;
                }
            }
            confirmBusStopDeletion(itemView, thisRoute.getId());
        });
        itemView.findViewById(R.id.updateBusStopBtn).setOnClickListener(view -> {
            Intent intent = new Intent(itemView.getContext(), CreateBusStopActivity.class);
            try{
                for(BusStop bstop : busStopAdapter.busStopViewList)
                {
                    if( bstop.getBusStopName().equals(bStopName.getText().toString()))
                    {
                        intent.putExtra("id", bstop);
                        itemView.getContext().startActivity(intent);
                    }
                }
            }catch (Exception ex)
            {
                Toast.makeText(view.getContext(), "Message: Unable to update bus stop, try again!!" , Toast.LENGTH_LONG).show();
            }

        });
    }
    public BusStopVH linkAdapter(BusStopAdapter adapter) {
        this.busStopAdapter = adapter;
        return this;
    }
    public void confirmBusStopDeletion(View view, String busStopId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

        alert.setTitle("Delete");
        alert.setMessage("Are you sure you want to delete?\nBus stop:" + bStopName.getText()+"\nBus Stop id:"+busStopId);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    busStopManager.deleteBusStop(busStopId);
                    TextView numberOfBusStops = view.getRootView().findViewById(R.id.numberOfBusStops);
                    int bsNom = Integer.valueOf(numberOfBusStops.getText().toString())-1;
                    numberOfBusStops.setText(String.valueOf(bsNom));
                    busStopAdapter.busStopViewList.remove(getAbsoluteAdapterPosition());
                    busStopAdapter.notifyItemRemoved(getAdapterPosition());
                }catch (Exception ex)
                {
                    Toast.makeText(view.getContext(), "Error: Unable to delete Bus Stop" , Toast.LENGTH_LONG).show();
                }
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
