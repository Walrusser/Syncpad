package com.nordstar.afehr.syncpad;

/**
 * Created by Alexander Fehr on 2017-08-06.
 */

public class note {
    private String title;
    private String subtitle;
    private String id;

    public note(String _title, String _subtitle, String _id){
        title = _title;
        subtitle = _subtitle;
        id = _id;
    }

    public String getTitle(){
        return title;
    }

    public String getSubtitle(){
        return subtitle;
    }

    public String getId(){
        return id;
    }

}
