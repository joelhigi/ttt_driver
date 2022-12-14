package com.tartantransporttracker.driver.managers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.driver.models.BusStop;
import com.tartantransporttracker.driver.repository.BusRepository;
import com.tartantransporttracker.driver.repository.UserRepository;

public class BusStopManager {
    private static volatile BusStopManager instance;
    private BusRepository busRepository;

    public BusStopManager() {
        busRepository = busRepository.getInstance();
    }

    public  static BusStopManager getInstance(){
        BusStopManager result = instance;
        if(result != null){
            return result;
        }
        synchronized (UserRepository.class){
            if(instance == null){
                instance = new BusStopManager();
            }
            return instance;
        }
    }

    public void createBusStop(BusStop busStop){
        busRepository.createBusStop(busStop);
    }

    public Task<QuerySnapshot> findAllBusStops(){
        return busRepository.findAll();
    }

    public Task<BusStop> getBusStop(String id){
        return  busRepository.getBusStop(id).continueWith(task ->
                task.getResult().toObject(BusStop.class));
    }

    public void deleteBusStop(String id){
        busRepository.deleteBusStop(id);
    }

    public void updateBusStop(String id,BusStop busStop){busRepository.updateBusStop(id,busStop);}
}
