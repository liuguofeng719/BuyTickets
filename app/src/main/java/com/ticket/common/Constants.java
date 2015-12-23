package com.ticket.common;

/**
 * Created by liuguofeng719 on 2015/12/10.
 */
public class Constants {

    public static class comm {
        //时间选择页面
        public static final int PICKER_SUCCESS = 10;
        public static final int START_CITY_SUCCESS = 20;
        public static final int END_CITY_SUCCESS = 30;
        public static final int PASSENGER_SUCCESS = 40;//乘客
    }

    /**
     * 注册
     */
    public static class reg {
        //总的毫秒数
        public static final long millisInFuture = 60000;
        //每次多少毫秒数
        public static final long countDownInterval = 1000;
    }

    /**
     * 支付宝
     */
    public static class tpay {
        // 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
        public static final String PARTNER = "2088021315718372";
        // 商户收款的支付宝账号
        public static final String SELLER = "yang.jingbo@dylkj.cn";
        // 商户（RSA）私钥(注意一定要转PKCS8格式，否则在Android4.0及以上系统会支付失败)
        public static final String RSA_PRIVATE = "";
        // 支付宝（RSA）公钥用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。
        public static final String RSA_ALIPAY_PUBLIC = "";
    }

    /**
     * 微信支付
     */
    public static class wxpay {
        /**
         * 服务号相关信息
         */
        public final static String APPID = "wxf8982470f270c06e";//服务号的应用号
    }
}
