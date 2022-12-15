/*
 * BusStop Model which stores the bus stop information
 * by Didier Ngabo*/
package com.tartantransporttracker.driver.models;

import com.google.firebase.firestore.DocumentId;

public class BusStop {
    @DocumentId
    private String id;
    private String busStopName;
    private Route route;
    private int position;

    public BusStop() {
    }

    public BusStop(String _busStopName, Route _route, int _position) {
        busStopName = _busStopName;
        route = _route;
        position = _position;
    }

    public String getId() {
        return id;
    }

    public void setId(String _id) {
        id = _id;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String _busStopName) {
        busStopName = _busStopName;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route _route) {
        route = _route;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int _position) {
        position = _position;
    }
}
