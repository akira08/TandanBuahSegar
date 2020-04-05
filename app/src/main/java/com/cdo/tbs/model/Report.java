package com.cdo.tbs.model;

import java.io.Serializable;

public class Report implements Serializable {
    private long id;
    private String title;
    private String dateCreated;

    public Report(long id, String title, String dateCreated){
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
