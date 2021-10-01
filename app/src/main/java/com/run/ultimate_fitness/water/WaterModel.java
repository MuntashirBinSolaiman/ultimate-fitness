package com.run.ultimate_fitness.water;

public class WaterModel {

    private int id;
    private String currentDateString;
    private int cups_of_waterC;
    private int et_size_of_cup;

    public WaterModel(int id, String currentDateString, int cups_of_waterC){//, int et_size_of_cup) {
        this.id = id;
        this.currentDateString = currentDateString;
        this.cups_of_waterC = cups_of_waterC;
        //this.et_size_of_cup = et_size_of_cup;

    }

    //toString


    @Override
    public String toString() {
        return "Water Tracker Entries: " + " " +
                "id: " +  id + " " +
                ", Current Date:"  + " " + currentDateString +
                ", Cups Of Water:" + " " + cups_of_waterC;// +
                //", Size Of Cup in (ml):" + et_size_of_cup ;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return currentDateString;
    }

    public void setDate(String currentDateString) {
        this.currentDateString = currentDateString;
    }

    public int getCups_of_waterC() {
        return cups_of_waterC;
    }

    public void setCups_of_waterC(int cups_of_waterC) {
        this.cups_of_waterC = cups_of_waterC;
    }

    /*public int getSize_of_cup() {
        return et_size_of_cup;
    }

    public void setSize_of_cup(int et_size_of_cup) {
        this.et_size_of_cup = et_size_of_cup;
    }*/
}
