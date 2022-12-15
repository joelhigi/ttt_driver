/*
 * User Model which stores the driver information
 * by Didier Ngabo*/

package com.tartantransporttracker.driver.models;
/*
 * A class that acts as the model for all Users.
 * by Didier
 * */
import androidx.annotation.Nullable;

public class User {

    private String uid;
    private String username;
    private String role;
    @Nullable
    private String urlPicture;

    private Route preferredRoute;

    public User() {
    }

    public User(String _uid, String _username, @Nullable String _urlPicture) {
        uid = _uid;
        username = _username;
        urlPicture = _urlPicture;
    }

    public User(String _uid, String _username, String _role, @Nullable String _urlPicture) {
        uid = _uid;
        username = _username;
        role = _role;
        urlPicture = _urlPicture;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String _uid) {
        uid = _uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        username = _username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String _role) {
        role = _role;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String _urlPicture) {
        urlPicture = _urlPicture;
    }

    public Route getPreferredRoute() {
        return preferredRoute;
    }

    public void setPreferredRoute(Route preferredRoute) {
        this.preferredRoute = preferredRoute;
    }
}
