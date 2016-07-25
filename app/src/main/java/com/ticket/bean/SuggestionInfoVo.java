package com.ticket.bean;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/7/24.
 */
public class SuggestionInfoVo implements Serializable {

    public String key;
    public String city;
    public String district;
    public LatLng pt;
    public String uid;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public LatLng getPt() {
        return pt;
    }

    public void setPt(LatLng pt) {
        this.pt = pt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
