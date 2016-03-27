package com.ticket.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuguofeng719 on 2016/3/25.
 */
public class BalanceChangeVoResp<T> extends BaseInfoVo {

    @SerializedName("BalanceChangeList")
    T balanceChangeList;

    public T getBalanceChangeList() {
        return balanceChangeList;
    }

    public void setBalanceChangeList(T balanceChangeList) {
        this.balanceChangeList = balanceChangeList;
    }

    @Override
    public String toString() {
        return "BalanceChangeVoResp{" +
                "balanceChangeList=" + balanceChangeList +
                '}';
    }
}
