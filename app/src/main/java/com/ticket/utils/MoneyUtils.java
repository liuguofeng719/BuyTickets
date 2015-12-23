package com.ticket.utils;

import java.math.BigDecimal;

public class MoneyUtils {
    
    /**
     * 分换算成元，并保留2位小数
     * @param cent
     * @return cent=1001 则输出 10.01
     */
    public static String centToYuan(Integer cent) {
        return centToYuan((long) cent);
    }
    
    /**
     * 分换算成元，并保留2位小数
     * @param cent
     * @return cent=1001 则输出 10.01
     */
    public static String centToYuan(Long cent) {
        if (cent == null || cent <= 0) {
            return "0.00";
        }
        long yuan = cent / 100;
        long mod = cent % 100;
        long jiao = mod / 10;
        long fen = mod % 10;
        return yuan + "." + jiao + "" + fen;
    }
    
    /**
     * 元转分
     * 
     * @param v
     * @return
     */
    public static long yuanToCent(Double v) {
        BigDecimal bd = BigDecimal.valueOf(v);
        BigDecimal h = new BigDecimal(100);
        BigDecimal b = bd.multiply(h);
        return b.longValue();
    }

    /**
     * 元转分
     * 
     * @param v
     * @return
     */
    public static long yuanToCent(String v) {
        if (v == null || v.length() <= 0) {
            return 0;
        }
        try {
            Double val = Double.parseDouble(v);
            return yuanToCent(val);
        } catch (Exception e) {
            return 0;
        }
    }
}
