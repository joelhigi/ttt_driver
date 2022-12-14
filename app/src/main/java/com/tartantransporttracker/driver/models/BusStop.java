package com.tartantransporttracker.driver.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;

public class BusStop implements Parcelable {
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

    protected BusStop(Parcel in) {
        id = in.readString();
        busStopName = in.readString();
        position = in.readInt();
    }

    public static final Creator<BusStop> CREATOR = new Creator<BusStop>() {
        @Override
        public BusStop createFromParcel(Parcel in) {
            return new BusStop(in);
        }

        @Override
        public BusStop[] newArray(int size) {
            return new BusStop[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(busStopName);
        parcel.writeInt(position);
    }
}
