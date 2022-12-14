package com.tartantransporttracker.driver.managers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.driver.models.Route;
import com.tartantransporttracker.driver.repository.RouteRepository;
import com.tartantransporttracker.driver.repository.UserRepository;

public class RouteManager {
    private static volatile RouteManager instance;
    private RouteRepository routeRepository;

    public RouteManager() {
        routeRepository = routeRepository.getInstance();
    }

    public  static RouteManager getInstance(){
        RouteManager result = instance;
        if(result != null){
            return result;
        }
        synchronized (UserRepository.class){
            if(instance == null){
                instance = new RouteManager();
            }
            return instance;
        }
    }

    public void createRoute(Route route){
        routeRepository.createRoute(route);
    }

    public Task<QuerySnapshot> findAllRoutes(){
        return routeRepository.findAll();
    }

    public Task<Route> getRoute(String id){
        return  routeRepository.getRoute(id).continueWith(task ->
                task.getResult().toObject(Route.class));
    }

    public void updateRoute(String id, Route route){routeRepository.updateRoute(id, route);}

    public void deleteRoute(String id){
        routeRepository.deleteRoute(id);
    }

}
