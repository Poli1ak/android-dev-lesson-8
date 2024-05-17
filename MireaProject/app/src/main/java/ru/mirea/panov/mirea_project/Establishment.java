package ru.mirea.panov.mirea_project;

import org.osmdroid.util.GeoPoint;

public class Establishment {

    private String name;
    private String description;
    private GeoPoint geoPoint;

    public Establishment(String name, String description, GeoPoint geoPoint) {
        this.name = name;
        this.description = description;
        this.geoPoint = geoPoint;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }
}
