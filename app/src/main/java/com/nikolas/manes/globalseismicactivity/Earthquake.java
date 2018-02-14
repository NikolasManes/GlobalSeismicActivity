package com.nikolas.manes.globalseismicactivity;

/**
 * Created by Nikolas on 12/1/2017.
 */

public class Earthquake {
    private static final String LOG_TAG = Earthquake.class.getSimpleName();

    private String mLocation;
    private double mMagnitude;
    private long mTime;
    private String mUrl;
    private static final String LOCATION_SEPARATOR = " of ";

    public Earthquake(String location, double magnitude, long time, String url) {
        mLocation = location;
        mMagnitude = magnitude;
        mTime = time;
        mUrl = url;
    }

    public String[] getLocation() {
        String[] Location = new String[2];
        if (mLocation.contains(LOCATION_SEPARATOR)){
            Location = mLocation.split(LOCATION_SEPARATOR);
            Location[0] = Location[0]  + " of";
        }
        else {
            Location[0] = "Near the ";
            Location[1] = mLocation;
        }
        return Location;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public long getTime() {
        return mTime;
    }

    public String getUrl() {
        return mUrl;
    }
}
