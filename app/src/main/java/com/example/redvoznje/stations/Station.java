package com.example.redvoznje.stations;

public class Station {
    private final String name;
    private final String id;

    public Station(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
