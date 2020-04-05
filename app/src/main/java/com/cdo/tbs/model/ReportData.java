package com.cdo.tbs.model;

import java.io.Serializable;

public class ReportData implements Serializable {
    private long id;
    private String date;
    private int unit;
    private int tons;
    private int margin;

    public ReportData(long id, String date, int unit, int tons, int margin) {
        this.id = id;
        this.date = date;
        this.unit = unit;
        this.tons = tons;
        this.margin = margin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getTons() {
        return tons;
    }

    public void setTons(int tons) {
        this.tons = tons;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
}
