package com.example.redvoznje.history;

public class HistoryEntry {
    private final String stationFrom, stationTo;
    private final String stationFromID, stationToID;

    public HistoryEntry(String stationFrom, String stationFromID, String stationTo, String stationToID) {
        this.stationFrom = stationFrom;
        this.stationFromID = stationFromID;
        this.stationTo = stationTo;
        this.stationToID = stationToID;
    }

    public String getStationFrom() {
        return stationFrom;
    }

    public String getStationTo() {
        return stationTo;
    }

    public String getStationFromID() {
        return stationFromID;
    }

    public String getStationToID() {
        return stationToID;
    }
}
