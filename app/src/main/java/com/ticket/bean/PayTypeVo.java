package com.ticket.bean;

/**
 * Created by liuguofeng719 on 2016/4/13.
 */
public class PayTypeVo {

    private int resId;
    private String payName;
    private String type;
    private boolean isSelected;

    public PayTypeVo(int resId, String payName, String type, boolean isSelected) {
        this.resId = resId;
        this.payName = payName;
        this.type = type;
        this.isSelected = isSelected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {

        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }
}
