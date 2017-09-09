package com.nordstar.afehr.syncpad;

/**
 * Created by Alexander Fehr on 2017-09-09.
 */

public class user {
    private String displayName;
    private String email;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public user(String _displayName, String _email){
        displayName = _displayName;
        email = _email;
    }
}
