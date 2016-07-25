package com.ticket.bean;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/25.
 */
public class LatLng implements Serializable {
    public double latitude;
    public double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
