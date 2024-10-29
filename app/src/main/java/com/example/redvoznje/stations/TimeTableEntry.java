package com.example.redvoznje.stations;

public class TimeTableEntry {
    private String trainID;
    private String departureTime;
    private String arrivalTime;
    private String travelTime;
    private String rangOfTrain;
    private String departureDate;
    private String arrivalDate;


    public TimeTableEntry(String trainID, String departureTime, String arrivalTime, String travelTime, String rangOfTrain) {
        this.trainID = trainID;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.travelTime = travelTime;
        this.rangOfTrain = rangOfTrain;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getTrainID() {
        return trainID;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public String getRangOfTrain() {
        return rangOfTrain;
    }
}
