package com.tartantransporttracker.driver.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.util.List;

public class Route {
    @DocumentId
    private String id;
    private String name;
    private Timestamp ts;

    private List<User> students;

    public Route() {
    }

    public Route(String routeName) {
        name = routeName;
    }

    public String getName() {
        return name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> _users) {
        students = _users;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp newTS) {
        newTS = ts;
    }

    @Override
    public String toString() {
        return name;
    }
}
