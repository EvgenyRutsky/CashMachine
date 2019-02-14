package com.example.dbryuzgin.myapplication;

import java.io.Serializable;

public class GraphHandler implements Serializable {

    private String date;
    private double total;

    private GraphHandler(){
    }

    GraphHandler(String Date, double Total) {
        this.date = Date;
        this.total = Total;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
