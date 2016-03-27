package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/3/25.
 */
public class BalanceVo extends BaseInfoVo {

    @SerializedName("Balance")
    private String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BalanceVo{" +
                "balance='" + balance + '\'' +
                '}';
    }
}
