package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/3/25.
 */
public class BankAccountVoResp<T> extends BaseInfoVo {

    @SerializedName("BankAccount")
    T bankAccount;

    public T getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(T bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public String toString() {
        return "BankAccountVoResp{" +
                "bankAccount=" + bankAccount +
                '}';
    }
}
