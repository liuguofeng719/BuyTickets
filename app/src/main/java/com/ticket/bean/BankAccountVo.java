package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuguofeng719 on 2016/3/25.
 */
public class BankAccountVo implements Serializable {

    @SerializedName("bankName")
    private String bankName;
    @SerializedName("bankAccount")
    private String bankAccount;
    @SerializedName("bankRealName")
    private String bankRealName;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankRealName() {
        return bankRealName;
    }

    public void setBankRealName(String bankRealName) {
        this.bankRealName = bankRealName;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "bankName='" + bankName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", bankRealName='" + bankRealName + '\'' +
                '}';
    }
}
